package com.example.beautybell.presentation.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.beautybell.databinding.ActivityLoginBinding
import com.example.beautybell.infra.utils.SharedPrefs
import com.example.beautybell.presentation.common.extension.showToast
import com.example.beautybell.presentation.common.utils.Constants
import com.example.beautybell.service.NavigationService
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private val navigator by lazy { NavigationService() }
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var callbackManager: CallbackManager

    @Inject
    lateinit var sharedPreferences: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callbackManager = CallbackManager.Factory.create()
        initView()
        initGoogleSign()
        initFacebookSign()
        logOutFacebookUser()
    }

    private fun initView() {
        binding.loginButton.setOnClickListener {
            navigator.startArtisanActivity(this)
            sharedPreferences.saveName("-")
            sharedPreferences.saveEmail("-")
            sharedPreferences.saveBirth("-")
            sharedPreferences.saveAvatar("")
        }
    }

    private fun initGoogleSign() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.signInButtonGoogle.setSize(SignInButton.SIZE_STANDARD)
        binding.signInButtonGoogle.setOnClickListener {
            val signInIntent: Intent = googleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, Constants.RC_SIGN_IN)
        }
    }

    private fun initFacebookSign() {
        binding.signInButtonFacebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
        }

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                getUserProfile(loginResult?.accessToken, loginResult?.accessToken?.userId)
            }
            override fun onCancel() { showToast(Constants.cancel) }
            override fun onError(exception: FacebookException) { showToast(Constants.error) }
        })
    }

    private fun logOutFacebookUser() {
        LoginManager.getInstance().logOut()
    }

    @SuppressLint("LongLogTag")
    fun getUserProfile(token: AccessToken?, userId: String?) {

        val parameters = Bundle()
        parameters.putString(
            "fields",
            "id, first_name, middle_name, last_name, name, picture, email"
        )

        GraphRequest(token,
            "/$userId/",
            parameters,
            HttpMethod.GET,
            GraphRequest.Callback { response ->
                val jsonObject = response.jsonObject

                if (BuildConfig.DEBUG) {
                    FacebookSdk.setIsDebugEnabled(true)
                    FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                }

                if (jsonObject.has("name")) {
                    val facebookName = jsonObject.getString("name")
                    sharedPreferences.saveBirth(facebookName)
                } else {
                    sharedPreferences.saveBirth("")
                }

                if (jsonObject.has("first_name")) {
                    val facebookFirstName = jsonObject.getString("first_name")
                    sharedPreferences.saveName(facebookFirstName)
                } else {
                    sharedPreferences.saveName("")
                }

                if (jsonObject.has("email")) {
                    val facebookEmail = jsonObject.getString("email")
                    sharedPreferences.saveEmail(facebookEmail)
                } else {
                    sharedPreferences.saveName("")
                }

                if (jsonObject.has("picture")) {
                    val facebookPictureObject = jsonObject.getJSONObject("picture")
                    if (facebookPictureObject.has("data")) {
                        val facebookDataObject = facebookPictureObject.getJSONObject("data")
                        if (facebookDataObject.has("url")) {
                            val facebookProfilePicURL = facebookDataObject.getString("url")
                            sharedPreferences.saveAvatar(facebookProfilePicURL)
                        }
                    }
                } else {
                    sharedPreferences.saveAvatar("")
                }

                navigator.startArtisanActivity(this)
            }).executeAsync()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            sharedPreferences.saveName(account.displayName!!)
            sharedPreferences.saveEmail(account.email!!)
            sharedPreferences.saveBirth(account.familyName!!)
            sharedPreferences.saveAvatar("")
            navigator.startArtisanActivity(this)
        } catch (e: ApiException) {
            showToast(Constants.fail)
        }
    }
}
