package com.jeuxdevelopers.estatepie.ui.fragments.intro

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentIntroBinding
import com.jeuxdevelopers.estatepie.enums.DeviceType
import com.jeuxdevelopers.estatepie.enums.Platforms
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.activities.explore.ExploreAdsActivity
import com.jeuxdevelopers.estatepie.ui.activities.managment.MainManagementActivity
import com.jeuxdevelopers.estatepie.ui.activities.tenants.MainTenantsActivity
import com.jeuxdevelopers.estatepie.ui.dialogs.progress.ProgressDialog
import com.jeuxdevelopers.estatepie.ui.dialogs.signup.SignUpBottomSheetF
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.AppConsts
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
class IntroFragment : Fragment() {

    private lateinit var binding: FragmentIntroBinding
    private val viewModel: IntroViewModel by viewModels()
    private lateinit var signInClient: GoogleSignInClient
//    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var progressDialog: ProgressDialog

    private lateinit var callbackManager: CallbackManager

    @Inject
    lateinit var tokenManager: TokenManager

    private var account: GoogleSignInAccount? = null

    private var socialTypeGoogle: Boolean = true

    var clientId: String = ""
    var name: String = ""
    var email: String = ""
    var token: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initProgressDialog()
        initCollectors()
        initGoogleClient()
        initFb()
        initClickListeners()
        initResultLauncher()

        getCurrentLocation()

    }

    private fun getCurrentLocation() {

        Dexter.withContext(requireContext()).withPermissions(

            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION

        ).withListener(object : MultiplePermissionsListener {

            override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                if (report.areAllPermissionsGranted()) {


                    LocationRequest.getCurrentLocation(
                        requireContext(),
                        object : LocationRequest.FetchLocationListener {

                            override fun onLocationNotAvailable() {

                                ToastUtility.errorToast(
                                    requireContext(), R.string.current_location_not_available
                                )

                            }

                            override fun onLocationRequestComplete() {


                            }

                            override fun onLocationPermissionError() {

                                ToastUtility.errorToast(
                                    requireContext(), R.string.access_location_permission_required
                                )

                            }

                            override fun onLocationProviderNotEnable() {

                                ToastUtility.errorToast(
                                    requireContext(), R.string.location_services_are_not_enabled
                                )

                            }

                            override fun onLocationAvailable(latLng: LatLng) {

                                tokenManager.saveLatitude(latLng.latitude.toString())
                                tokenManager.saveLongitude(latLng.longitude.toString())

                            }

                        })

                } else {

                    ToastUtility.errorToast(
                        requireContext(), getString(R.string.access_location_permission_required)
                    )

                }

            }

            override fun onPermissionRationaleShouldBeShown(
                list: MutableList<PermissionRequest>,
                permissionToken: PermissionToken
            ) {

                permissionToken.continuePermissionRequest()
            }

        }).check()

    }

    private fun initFb() {

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {

            override fun onSuccess(result: LoginResult) {
//                handleFacebookAccessToken(result.accessToken);
                token = result.accessToken.token
                Log.d("FACEBOOKDATA", "success")

                val graphRequest = GraphRequest.newMeRequest(result?.accessToken){ obj, response ->

                    try {
                        if (obj!!.has("id")){
                            Log.d("FACEBOOKDATA", obj.getString("name"));
                            Log.d("FACEBOOKDATA", obj.getString("email"));
                            Log.d("FACEBOOKDATA", obj.getString("picture"));

                            clientId = obj.getString("id")
                            name = obj.getString("name")
                            email = obj.getString("email")

                            viewModel.checkSocialUserRequest.client_id = clientId
                            viewModel.checkSocialUserRequest.platform = Platforms.Facebook.toString()
                            viewModel.checkSocialLogin()

                        }
                    }catch (e: Exception){
                        ToastUtility.errorToast(requireContext(), ""+e.message)
                    }
                }
                // LoginResult?
                val param = Bundle()
                param.putString("fields" , "name,email,id,picture.type(large)")
                graphRequest.parameters = param
                graphRequest.executeAsync()

            }

            override fun onCancel() {
                // App code
                Log.d("FACEBOOKDATA", "cancel")
                ToastUtility.successToast(requireContext(), "request cancel")


            }

            override fun onError(exception: FacebookException) {
                // App code
                Log.d("FACEBOOKDATA", "error" + exception.localizedMessage)
                ToastUtility.successToast(requireContext(), "error" + exception.localizedMessage)

            }

        })
    }

    private fun launchFacebookLogin() {

        socialTypeGoogle = false

        //login callback
        LoginManager.getInstance()
//            .logInWithReadPermissions(this, listOf("email","public_profile","user_gender"))
            .logInWithReadPermissions(this, listOf("email","public_profile"))

    }

    private fun initClickListeners() {

        binding.btnManagementSignup.setOnClickListener {
            SignUpBottomSheetF(
                onEmailSignUp = {
                    findNavController().navigate(R.id.action_introFragment_to_signUpFragment)
                },
                onAppleSignUp = {},
                onFacebookSignUp = {
                    launchFacebookLogin()
                    viewModel.socialRequest.business_name = "user"
                    viewModel.socialRequest.role_id = 4

                },
                onGoogleSignUp = {
                    launchGoogleLogin()
                    viewModel.socialRequest.business_name = "user"
                    viewModel.socialRequest.role_id = 4
                }
            ).show(childFragmentManager, null)
        }

        binding.btnTenantSignup.setOnClickListener {
            SignUpBottomSheetF(
                onEmailSignUp = {
                    findNavController().navigate(R.id.action_introFragment_to_tenantsSignUpFragment)
                },
                onAppleSignUp = {},
                onFacebookSignUp = {
                    launchFacebookLogin()
                    viewModel.socialRequest.role_id = 5
                },
                onGoogleSignUp = {
                    launchGoogleLogin()
                    viewModel.socialRequest.role_id = 5
                }
            ).show(childFragmentManager, null)

        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_loginFragment)
        }

        binding.btnExploreAds.setOnClickListener {
            Intent(requireContext(), ExploreAdsActivity::class.java).apply {
                startActivity(this)
            }
        }

    }

    private fun initProgressDialog() {

        progressDialog = ProgressDialog(requireContext())

    }

    private fun initGoogleClient() {
        val options: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestId()
            .requestEmail()
            .build()

        signInClient = GoogleSignIn.getClient(requireActivity(), options)
    }

    private fun launchGoogleLogin() {

        socialTypeGoogle = true

        val account = GoogleSignIn.getLastSignedInAccount(requireContext())

        if (account != null) {
            // User is already signed in, so sign them out first
            val googleSignInClient = GoogleSignIn.getClient(requireActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN)
            googleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
                // After signing out, launch the sign-in process
                resultLauncher.launch(googleSignInClient.signInIntent)
            }
        } else {
            // User is not signed in, directly launch the sign-in process
            resultLauncher.launch(signInClient.signInIntent)
        }

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.socialLoginUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), it.message)
                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Login")
                    Timber.d("Login")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

//                    tokenManager.saveToken(it.result?.data?.user!!.access_token)
                    Timber.d("Token -> ${it.result?.data?.user!!.access_token}")

                    it.result.data.user.let { user ->

                        if (user.details.is_verified == 1){
//                            user.role_id == 3 ||
                            if(user.role_id == 5){

                                if (user.is_verify.isEmpty()){
                                    gotoTenantsScreen()
                                }else{

//                                    viewModel.resendCodeRequest.verification_email = user.is_verify
//                                    binding.btnResendCode.visibility = View.VISIBLE
                                    ToastUtility.errorToast(requireContext(), "As of yet, you have not verified your account.")

                                }

                            }else if (user.role_id == 4){

                                if (user.is_verify.isEmpty())

                                    gotoManagementScreen()

                                else{

//                                    viewModel.resendCodeRequest.verification_email = user.is_verify
//                                    binding.btnResendCode.visibility = View.VISIBLE
                                    ToastUtility.errorToast(requireContext(), "As of yet, you have not verified your account.")

                                }

                            }else{
                                ToastUtility.errorToast(requireContext(), "Invalid Role.")

                            }

                        }else{
                            ToastUtility.errorToast(requireContext(), "As of yet, you have not verified your account.")

                        }


                    }

                }
            }
        }


        collectLatestLifecycleFlow(viewModel.checkSocialLoginUiState){
            when(it){
                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), it.message)
                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Checking User status.")
                    Timber.d("Login")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    if (socialTypeGoogle){
                        // signup or login as google
                        if (it.result?.data == true){
                            signInSocial(account?.email.toString(), account?.idToken.toString(), account?.id.toString(), Platforms.Google.name)
                        }else{
                            signUpAs(account?.displayName.toString(),account?.email.toString(), account?.idToken.toString(), account?.id.toString(), Platforms.Google.name)
                        }

                    }else{

                        // signup or login as facebook
                        if (it.result?.data == true){
                            signInSocial(email, token, clientId, Platforms.Facebook.name)
                        }else{
                            signUpAs(name,email, token, clientId, Platforms.Facebook.name)
                        }
                    }

                }
            }
        }

    }

    private fun signInSocial(email: String,idToken: String, clientId: String, socialType: String ) {

        viewModel.socialRequest.token = idToken
        viewModel.socialRequest.email = email
        viewModel.socialRequest.device_token = AppConsts.DEVICE_TOKEN
        viewModel.socialRequest.device_type = DeviceType.android.name
        viewModel.socialRequest.platform = socialType
        viewModel.socialRequest.client_id = clientId

        viewModel.socialLogin()

    }

    private fun signUpAs(name: String, email: String,idToken: String, clientId: String, socialType: String) {


        viewModel.socialRequest.token = idToken
        viewModel.socialRequest.email = email
        viewModel.socialRequest.device_token = AppConsts.DEVICE_TOKEN
        viewModel.socialRequest.device_type = DeviceType.android.name
        viewModel.socialRequest.platform = socialType
        viewModel.socialRequest.client_id = clientId
        viewModel.socialRequest.username = name

        viewModel.socialLogin()

    }

    private fun gotoManagementScreen() {

        Intent(requireContext(), MainManagementActivity::class.java).apply {
            startActivity(this)
            activity?.finish()
        }
    }

    private fun gotoTenantsScreen() {

        Intent(requireContext(), MainTenantsActivity::class.java).apply {
            startActivity(this)
            activity?.finish()
        }


    }

    private fun initResultLauncher() {

        resultLauncher = registerForActivityResult(

            ActivityResultContracts.StartActivityForResult()

        ) { result: ActivityResult ->

            // Sending Google Login Activity Result
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(result.data)

            handleResult(task)

        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            account =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                val idToken = account!!.idToken
                var clientId = account!!.id.toString()

                viewModel.checkSocialUserRequest.client_id = clientId
                viewModel.checkSocialUserRequest.platform = Platforms.Google.toString()
                viewModel.checkSocialLogin()

            }
        } catch (e: ApiException){
            ToastUtility.errorToast(requireContext(), "error:"+e.message)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


}