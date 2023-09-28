package com.jeuxdevelopers.estatepie.ui.activities.managment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ActivityMainManagementBinding
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.activities.auth.AuthActivity
import com.jeuxdevelopers.estatepie.ui.dialogs.delete.DeleteAccountDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.logout.LogoutDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.listeners.ManagementDrawerListener
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainManagementActivity : AppCompatActivity(), ManagementDrawerListener {

    private lateinit var binding: ActivityMainManagementBinding

    private val mainViewModel: MainManagementViewModel by viewModels()

    var logoutDialog: LogoutDialog? = null

    var deleteAccountDialog: DeleteAccountDialog? = null

    @Inject
    lateinit var tokenManager: TokenManager

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logoutDialog = LogoutDialog(this)
        deleteAccountDialog = DeleteAccountDialog(this)
//        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val bottomNavView: BottomNavigationView = binding.contentMainManagement.bottomNav
        navController = findNavController(R.id.nav_host_fragment_content_main_management)

        // Set the start destination programmatically
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.management_navigation)
        if (tokenManager.getCurrentUser()?.user_plan == 1)  graph.setStartDestination(R.id.propertiesFragment)
        else  graph.setStartDestination(R.id.managementDashboardFragment)
        navController.graph = graph

        navView.menu.forEach {
            it.actionView = createActionView(it.itemId)
            if (it.itemId == R.id.drw_logout) {
                Timber.d("MainMange -> set red for logout")
                it.icon.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.red
                    )
                )
                //it.icon.setTintMode(PorterDuff.Mode.SRC)

            }
        }

        navView.menu.findItem(R.id.drw_logout).setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.drw_logout -> {

//                    drawerLayout.close()

                    logoutDialog!!.showDialogue()
                    logoutDialog!!.getYesView()?.setOnClickListener {

                        logoutCurrentUser()
                        logoutDialog!!.hideDialogue()

                    }

                    logoutDialog!!.getNoView()?.setOnClickListener {

                        logoutDialog!!.hideDialogue()

                    }

                    return@setOnMenuItemClickListener true
                }
                else -> {

                    return@setOnMenuItemClickListener false
                }
            }
        }

        navView.menu.findItem(R.id.deleteButton).setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.deleteButton -> {

                    deleteAccountDialog!!.showDialogue()
                    deleteAccountDialog!!.getYesView()?.setOnClickListener {

                        mainViewModel.deleteUserAccount()
                        deleteAccountDialog!!.hideDialogue()

                    }

                    deleteAccountDialog!!.getNoView()?.setOnClickListener {

                        deleteAccountDialog!!.hideDialogue()

                    }

                    return@setOnMenuItemClickListener true
                }
                else -> {

                    return@setOnMenuItemClickListener false
                }
            }
        }

        navView.setupWithNavController(navController)
        bottomNavView.setupWithNavController(navController)
//        NavigationUI.setupWithNavController(bottomNavView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.managementDashboardFragment -> showBottomNavigationView(bottomNavView)
                R.id.propertiesFragment -> showBottomNavigationView(bottomNavView)
                R.id.tenantsFragment -> showBottomNavigationView(bottomNavView)
                R.id.billingFragment -> showBottomNavigationView(bottomNavView)
                R.id.requestsFragment -> showBottomNavigationView(bottomNavView)
                R.id.unPaidBillsByTenantIdFragment2 -> showBottomNavigationView(bottomNavView)
                R.id.paidBillsByTenantIdFragment -> showBottomNavigationView(bottomNavView)
                else -> {
                    hideBottomNavigationView(bottomNavView)
                }
            }
        }

        initBilling()
        initCollectors()

//        mainViewModel.updateArticlesCount()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()

    }

    private fun initBilling() {

        mainViewModel.initBillingClient(this)
        mainViewModel.startBillingConnection()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(this)
        collectLatestLifecycleFlow(mainViewModel.planUpdateState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(this, it.message)
                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Resend verification code.")
                    Timber.d("Login")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.toString()}")

                    ToastUtility.successToast(this, it.result?.toString())

                }
            }
        }

        collectLatestLifecycleFlow(mainViewModel.deleteAccountUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(this, "Error : ${it.message}, ${it.result.toString()}")
                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Loading...")
                    Timber.d("request delete account")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    ToastUtility.successToast(this, "Account deleted successfully.")

                    gotoLoginActivity()

                }
            }
        }

    }

    private fun hideBottomNavigationView(view: BottomNavigationView) {
        view.animate().translationY(view.height.toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                }
            })
    }

    private fun showBottomNavigationView(view: BottomNavigationView) {
        view.animate().translationY(0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.VISIBLE
                }
            })
    }

    private fun logoutCurrentUser() {

        val options: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.web_client_id))
//                .requestId()
                .requestEmail().requestProfile().build()
        val googleSignInClient = GoogleSignIn.getClient(this, options)

        if (googleSignInClient != null
            && mainViewModel.getCurrentUser()?.details?.is_social_login == 1
        ){

            googleSignInClient.signOut()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        tokenManager.saveCurrentUser(null)
                        tokenManager.saveToken("")
                        gotoLoginActivity()

                        ToastUtility.errorToast(this@MainManagementActivity, "Logout success")
                        // Sign out successful
                        // Perform any additional actions after sign out
                    } else {
                        ToastUtility.errorToast(this@MainManagementActivity, "Logout failed.")
                        // Sign out failed
                        // Handle the failure or display an error message
                    }
                }

        }else{

            tokenManager.saveCurrentUser(null)
            tokenManager.saveToken("")
            gotoLoginActivity()

        }



    }

    private fun gotoLoginActivity() {

        Intent(this, AuthActivity::class.java).apply {
            startActivity(this)
            finish()
        }


    }


    private fun createActionView(itemId: Int): View {
        val image = ImageView(this)
        image.setImageResource(R.drawable.ic_arrow_forward)
        if (itemId == R.id.drw_logout) {
            image.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red))
        }
        return image
    }

    override fun openDrawer() {
        binding.drawerLayout.open()
    }

    override fun closeDrawer() {
        binding.drawerLayout.close()
    }


}