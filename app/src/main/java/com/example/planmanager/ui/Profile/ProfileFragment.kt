package com.example.planmanager.ui.Profile

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.planmanager.R
import com.example.planmanager.databinding.FragmentProfileBinding
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.planmanager.data.OAuthService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.example.planmanager.data.GoogleTokenResponse
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.databinding.DataBindingUtil


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var binding: FragmentProfileBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.setContentView(requireActivity(), R.layout.fragment_profile)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope("https://www.googleapis.com/auth/admin.directory.resource.calendar"))
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestServerAuthCode(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        binding?.singInWithGoogle?.setOnClickListener {
            mGoogleSignInClient?.signOut()
            mGoogleSignInClient?.signInIntent?.let {
                Log.w("klnflkadsnflaks", "signInLauncher")
                signInLauncher.launch(it)
            }
        }
    }

    private val signInLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            if (result.resultCode == RESULT_OK && data != null) {
                Log.w("klnflkadsnflaks", "resultCode ${result.resultCode}")
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            } else {
                // Handle unsuccessful sign-in
                Log.e("MainFragment", "Sign in failed")
                Toast.makeText(requireContext(), "Sign in failed", Toast.LENGTH_SHORT).show()
            }
        }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val authCode = account.serverAuthCode
            Log.w("klnflkadsnflaks", "authCode=" + authCode)
            getRefreshToken(authCode)
            binding?.apply {
                nameTextView.text = account.displayName.toString()
                emailTextView.text = account.email.toString()
            }
            binding?.profileImageView?.let { image ->
                Glide.with(requireContext())
                    .load(account.photoUrl.toString())
                    .into(image)
            }
        } catch (e: ApiException) {
            Log.w("klnflkadsnflaks", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun getRefreshToken(authCode: String?) {
        Log.w("klnflkadsnflaks", "authCode=" + authCode.toString())
        val retrofit = Retrofit.Builder()
            .baseUrl("https://oauth2.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(OAuthService::class.java)
        val call = service.getToken(
            code = authCode.toString(),
            clientId = getString(R.string.default_web_client_id),
            clientSecret = getString(R.string.client_secret),
            redirectUri = "",
            grantType = "authorization_code"
        )
        call.enqueue(object : Callback<GoogleTokenResponse> {
            override fun onResponse(
                call: Call<GoogleTokenResponse>,
                response: Response<GoogleTokenResponse>
            ) {
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    Log.w(
                        "klnflkadsnflaks",
                        "tokenResponse=" + tokenResponse?.accessToken.toString()
                    )
                    Log.w(
                        "klnflkadsnflaks",
                        "tokenResponse=" + tokenResponse?.refreshToken.toString()
                    )
                    Toast.makeText(
                        requireContext(),
                        tokenResponse?.scope.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.w("klnflkadsnflaks", "tokenResponse=" + tokenResponse?.scope.toString())
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<GoogleTokenResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }
}








//
//class ProfileFragment : Fragment(R.layout.fragment_profile) {
//    private var _binding: FragmentProfileBinding? = null
//    private val binding get() = _binding!!
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return ComposeView(requireContext()).apply {
//            setContent {
//                googleSignIn()
//            }
//        }
//    }
//
//}
//
//@Composable
//private fun googleSignIn (){
//    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//
//    val onClick: () -> Unit = {
//        val credentialManager = CredentialManager.create(context)
//
//        val rawNonce = UUID.randomUUID().toString()
//        val bytes = rawNonce.toByteArray()
//        val md = MessageDigest.getInstance("SHA-256")
//        val digest = md.digest(bytes)
//        val hashedNonce = digest.fold(""){Str, it -> Str + "%02x".format((it))}
//
//        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
//            .setFilterByAuthorizedAccounts(false)
//            .setServerClientId("358827406368-co1lln88rsvr6rgl14s6t22mf4gc8ck3.apps.googleusercontent.com")
//            .setNonce(hashedNonce)
//            .build()
//
//        val request: GetCredentialRequest = GetCredentialRequest.Builder()
//            .addCredentialOption(googleIdOption)
//            .build()
//
//        coroutineScope.launch {
//            try {
//                val result = credentialManager.getCredential(
//                    request = request,
//                    context = context
//                )
//                val credential = result.credential
//
//                val googleIdTokenCredential = GoogleIdTokenCredential
//                    .createFrom(credential.data)
//
//                val googleIdToken = googleIdTokenCredential.idToken
//
//                Log.i(TAG, googleIdToken)
//
//                Toast.makeText(context, "SUCCESSFUL SIGN IN", Toast.LENGTH_SHORT).show()
//            }catch(e: GetCredentialException) {
//                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
//            }catch(e: GoogleIdTokenParsingException) {
//                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
//            }
//
//        }
//    }
//    //button
//    Button(onClick = onClick) {
//        Text("Sign in with Google")
//    }
//}