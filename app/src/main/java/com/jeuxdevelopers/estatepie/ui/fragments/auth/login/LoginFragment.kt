package com.jeuxdevelopers.estatepie.ui.fragments.auth.login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import com.google.android.gms.tasks.Task
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.DialogSignupTypeBinding
import com.jeuxdevelopers.estatepie.databinding.FragmentLoginBinding
import com.jeuxdevelopers.estatepie.enums.DeviceType
import com.jeuxdevelopers.estatepie.enums.Platforms
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.activities.managment.MainManagementActivity
import com.jeuxdevelopers.estatepie.ui.activities.tenants.MainTenantsActivity
import com.jeuxdevelopers.estatepie.ui.dialogs.signup.SignUpBottomSheetF
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    @Inject
    lateinit var tokenManager: TokenManager

    private val viewModel: LoginViewModel by viewModels()

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    private lateinit var signInClient: GoogleSignInClient

    private lateinit var callbackManager: CallbackManager

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
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

        initCollectors()

        initGoogleClient()

        initFb()

        initResultLauncher()

//        printHashKey(requireContext())

    }

    private fun initGoogleClient() {

        val options: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestId()
                .requestEmail().requestProfile().build()

        signInClient = GoogleSignIn.getClient(requireContext(), options)

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

    private fun signUpAs(name: String, email: String,idToken: String, clientId: String, socialType: String) {

        val binding = DialogSignupTypeBinding.inflate(layoutInflater,null, false)
        val dialog = Dialog(requireContext(), android.R.style.Theme_Material_Dialog_Alert)
        dialog.setContentView(binding.root)

        dialog.show()

        binding.tvManagement.setOnClickListener {
            viewModel.socialRequest.role_id = 4
            viewModel.socialRequest.token = idToken
            viewModel.socialRequest.email = email
            viewModel.socialRequest.device_token = AppConsts.DEVICE_TOKEN
            viewModel.socialRequest.device_type = DeviceType.android.name
            viewModel.socialRequest.platform = socialType
            viewModel.socialRequest.client_id = clientId
            viewModel.socialRequest.business_name = "User"
            viewModel.socialRequest.username = name

            viewModel.socialLogin()
        }
        binding.tvTenants.setOnClickListener {
            viewModel.socialRequest.role_id = 5
            viewModel.socialRequest.token = idToken
            viewModel.socialRequest.email = email
            viewModel.socialRequest.device_token = AppConsts.DEVICE_TOKEN
            viewModel.socialRequest.device_type = DeviceType.android.name
            viewModel.socialRequest.platform = socialType
            viewModel.socialRequest.client_id = clientId
            viewModel.socialRequest.username = name

            viewModel.socialLogin()

        }

        binding.dialogCross.setOnClickListener{
            dialog.dismiss()
        }


    }

    private fun launchGoogleLogin() {

        socialTypeGoogle = true

        val account = GoogleSignIn.getLastSignedInAccount(requireContext())

        if (account != null) {
            // User is already signed in, so sign them out first
            val googleSignInClient = GoogleSignIn.getClient(requireActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN)
            googleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
                // After signing out, launch the sign-in process
                resultLauncher?.launch(googleSignInClient.signInIntent)
            }
        } else {
            // User is not signed in, directly launch the sign-in process
            resultLauncher?.launch(signInClient.signInIntent)
        }

    }

    private fun launchFacebookLogin() {

        socialTypeGoogle = false

        //login callback
        LoginManager.getInstance()
//            .logInWithReadPermissions(this, listOf("email","public_profile","user_gender"))
            .logInWithReadPermissions(this, listOf("email","public_profile"))

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.loginUiState){
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

                                     viewModel.resendCodeRequest.verification_email = user.is_verify
                                     binding.btnResendCode.visibility = View.VISIBLE
                                     ToastUtility.errorToast(requireContext(), "As of yet, you have not verified your account.")

                                 }

                            }else if (user.role_id == 4){

                                if (user.is_verify.isEmpty())

                                    gotoManagementScreen()

                                else{

                                    viewModel.resendCodeRequest.verification_email = user.is_verify
                                    binding.btnResendCode.visibility = View.VISIBLE
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

        collectLatestLifecycleFlow(viewModel.resendCodeUiState){
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
                    waitingDialog.show(status = "Resend verification code.")
                    Timber.d("Login")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    binding.btnResendCode.visibility = View.INVISIBLE
                    ToastUtility.successToast(requireContext(), it.result?.message)

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

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.btnSignUp.setOnClickListener {
            SignUpBottomSheetF(
                onEmailSignUp = {
                    findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
                },
                onAppleSignUp = {},
                onFacebookSignUp = {
                    launchFacebookLogin()
                },
                onGoogleSignUp = {
                    launchGoogleLogin()
                }
            ).show(childFragmentManager, null)
        }

        binding.btnForgot.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        binding.btnLogin.setOnClickListener {

            validateInputs()
//            printHashKey(requireContext())
        }

        binding.btnResendCode.setOnClickListener {

            viewModel.resendCode()

        }
    }

    fun printHashKey(pContext: Context) {
        try {
            val info: PackageInfo = pContext.getPackageManager()
                .getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey: String = String(Base64.encode(md.digest(), 0))
                Timber.d("printHashKey() Hash Key: " + hashKey)
            }
        } catch (e: NoSuchAlgorithmException) {
            Timber.d( "printHashKey()", e)
        } catch (e: Exception) {
            Timber.d( "printHashKey()", e)
        }
    }

    private fun validateInputs() {

        val email = binding.etEmailAddress.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty()|| !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmailAddress.error = "Email is empty or invalid."

        }  else if (password.isEmpty() || password.length < 6) {
            binding.etEmailAddress.error = null
            binding.etPassword.error = "Password is too short or empty."
        }  else {
            binding.etEmailAddress.error = null
            binding.etPassword.error = null

            viewModel.request.email = email
            viewModel.request.password = password
            viewModel.request.device_type = DeviceType.android.name

            viewModel.loginUser()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}