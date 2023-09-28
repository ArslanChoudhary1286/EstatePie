package com.jeuxdevelopers.estatepie.ui.activities.tenants

import android.Manifest
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
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.ActivityMainTenantsBinding
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.activities.auth.AuthActivity
import com.jeuxdevelopers.estatepie.ui.activities.managment.MainManagementViewModel
import com.jeuxdevelopers.estatepie.ui.dialogs.delete.DeleteAccountDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.logout.LogoutDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.ui.listeners.ManagementDrawerListener
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import com.jeuxdevelopers.estatepie.utils.TokenManager
import com.jeuxdevelopers.estatepie.utils.location.LocationRequest
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MainTenantsActivity : AppCompatActivity(), ManagementDrawerListener {

    private lateinit var binding: ActivityMainTenantsBinding

    var logoutDialog: LogoutDialog? = null

    @Inject
    lateinit var tokenManager: TokenManager

    var deleteAccountDialog: DeleteAccountDialog? = null

    private val mainViewModel: MainTenantsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainTenantsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logoutDialog = LogoutDialog(this)
        deleteAccountDialog = DeleteAccountDialog(this)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val bottomNavView: BottomNavigationView = binding.contentMainTenant.bottomNav
        val navController = findNavController(R.id.nav_host_fragment_content_main_tenants)

        initCollectors()

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
//                it.icon.setTintMode(PorterDuff.Mode.SRC)

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
        NavigationUI.setupWithNavController(bottomNavView, navController)

//        val options = NavOptions.Builder()
//            .setLaunchSingleTop(true)
//            .setEnterAnim(R.anim.fade_in)
//            .setExitAnim(R.anim.fade_out)
//            .setPopEnterAnim(R.anim.fade_out)
//            .setPopExitAnim(R.anim.fade_in)
//            .setPopUpTo(navController.graph.getStartDestination(), false)
//            .build()

//        bottomNavView.setOnNavigationItemSelectedListener { item ->
//            when(item.itemId) {
//                R.id.tenantsDashboardFragment -> {
//                    navController.navigate(R.id.tenantsDashboardFragment,null,options)
//                }
//                R.id.tenantsExploreFragment -> {
//                    navController.navigate(R.id.tenantsExploreFragment,null,options)
//                }
//                R.id.tenantsBillingFragment -> {
//                    navController.navigate(R.id.tenantsBillingFragment,null,options)
//                }
//                R.id.tenantsRequestFragment -> {
//                    navController.navigate(R.id.tenantsRequestFragment,null,options)
//                }
//                R.id.tenantsProfileFragment -> {
//                    navController.navigate(R.id.tenantsProfileFragment,null,options)
//                }
//            }
//            true
//        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.tenantsDashboardFragment -> showBottomNavigationView(bottomNavView)
                R.id.tenantsExploreFragment -> showBottomNavigationView(bottomNavView)
                R.id.tenantsBillingFragment -> showBottomNavigationView(bottomNavView)
                R.id.tenantsRequestFragment -> showBottomNavigationView(bottomNavView)
                R.id.tenantsProfileFragment -> showBottomNavigationView(bottomNavView)
                else -> {

                    hideBottomNavigationView(bottomNavView)
                }
            }
        }

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(this)
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

    override fun onBackPressed() {
        super.onBackPressed()
//        bottomNav.getMenu().getItem(0).setChecked(true);

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

//    private fun logoutCurrentUser() {
//
//        tokenManager.saveCurrentUser(null)
//        gotoLoginActivity()
//    }

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

                        ToastUtility.errorToast(this@MainTenantsActivity, "Logout success")
                        // Sign out successful
                        // Perform any additional actions after sign out
                    } else {
                        ToastUtility.errorToast(this@MainTenantsActivity, "Logout failed.")
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

}