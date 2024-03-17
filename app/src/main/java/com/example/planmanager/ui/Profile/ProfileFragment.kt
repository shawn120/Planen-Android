package com.example.planmanager.ui.Profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.example.planmanager.R

class ProfileFragment : Fragment() {

    private lateinit var textView: TextView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var clientId: String // Declare clientId property

    companion object {
        private const val TAG = "ProfileFragment"
        private const val RC_SIGN_IN = 123
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        textView = view.findViewById(R.id.textView)
        clientId = getString(R.string.andriod_client) // Initialize clientId here
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId) // Use the client ID here
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        // Set click listener for the button
        view.findViewById<View>(R.id.button).setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                // Signed in successfully, show authenticated UI.
                textView.text = "Authentication done.\nUsername is ${account?.displayName}"
            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            }
        }
    }
}



//import android.content.Intent
//import android.content.IntentSender
//import android.content.IntentSender.SendIntentException
//import androidx.activity.result.IntentSenderRequest
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import com.google.android.gms.auth.api.identity.BeginSignInRequest
//import com.google.android.gms.auth.api.identity.BeginSignInResult
//import com.google.android.gms.auth.api.identity.Identity
//import com.google.android.gms.auth.api.identity.SignInCredential
//import androidx.activity.result.contract.ActivityResultContracts
//import com.google.android.gms.auth.api.identity.SignInClient
//import com.google.android.gms.common.api.ApiException
//import com.google.android.gms.tasks.OnFailureListener
//import com.google.android.gms.tasks.OnSuccessListener
//import com.example.planmanager.R
//import com.google.android.material.button.MaterialButton
//
//class ProfileFragment : Fragment(R.layout.fragment_profile) {
//
//    private lateinit var textView: TextView
//    private lateinit var oneTapClient: SignInClient
//    private lateinit var signInRequest: BeginSignInRequest
//
//    private val signInLauncher = registerForActivityResult(
//        ActivityResultContracts.StartIntentSenderForResult()
//    ) { result ->
//        onActivityResult(REQ_ONE_TAP, result.resultCode, result.data)
//    }
//
//
//    companion object {
//        private const val TAG = "ProfileFragment"
//        private const val REQ_ONE_TAP = 100
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_profile, container, false)
//        textView = view.findViewById(R.id.textView)
//
//        val button = view.findViewById<MaterialButton>(R.id.button)
//        button.setOnClickListener {
//            buttonGoogleSignIn()
//        }
//
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        oneTapClient = Identity.getSignInClient(requireContext())
//        signInRequest = BeginSignInRequest.builder()
//            .setPasswordRequestOptions(
//                BeginSignInRequest.PasswordRequestOptions.builder()
//                    .setSupported(true)
//                    .build()
//            )
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId("358827406368-u6uknvfvrh8lfalrae8biobjjp604igi.apps.googleusercontent.com")
//                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(false)
//                    .build()
//            )
//            // Automatically sign in when exactly one credential is retrieved.
//            .setAutoSelectEnabled(true)
//            .build()
//    }
//
//    fun buttonGoogleSignIn() {
//        oneTapClient.beginSignIn(signInRequest)
//            .addOnSuccessListener(requireActivity(), OnSuccessListener<BeginSignInResult> { result ->
//                try {
//                    val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
//                    signInLauncher.launch(intentSenderRequest)
//                } catch (e: SendIntentException) {
//                    Log.e(TAG, "Couldn't start One Tap UI: " + e.localizedMessage)
//                }
//            })
//            .addOnFailureListener(requireActivity(), OnFailureListener { e ->
//                // No saved credentials found. Launch the One Tap sign-up flow, or
//                // do nothing and continue presenting the signed-out UI.
//                Log.d(TAG, e.localizedMessage)
//            })
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            REQ_ONE_TAP -> try {
//                val credential = oneTapClient.getSignInCredentialFromIntent(data)
//                val idToken = credential.googleIdToken
//                val username = credential.id
//                val password = credential.password
//                textView.text = "Authentication done.\nUsername is $username"
//                if (idToken != null) {
//                    // Got an ID token from Google. Use it to authenticate
//                    // with your backend.
//                    Log.d(TAG, "Got ID token.")
//                } else if (password != null) {
//                    // Got a saved username and password. Use them to authenticate
//                    // with your backend.
//                    Log.d(TAG, "Got password.")
//                }
//            } catch (e: ApiException) {
//                textView.text = e.toString()
//            }
//        }
//    }
//}



//import android.content.Intent
//import android.content.IntentSender
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import com.google.android.gms.auth.api.identity.BeginSignInRequest
//import com.google.android.gms.auth.api.identity.Identity
//import com.google.android.gms.auth.api.identity.SignInClient
//import com.google.android.gms.common.api.ApiException
//import com.google.android.gms.tasks.OnFailureListener
//import com.google.android.gms.tasks.OnSuccessListener
//import com.example.planmanager.databinding.FragmentProfileBinding
//
//class ProfileFragment : Fragment() {
//
//    private lateinit var textView: TextView
//    private lateinit var oneTapClient: SignInClient
//    private lateinit var signInRequest: BeginSignInRequest
//    private var _binding: FragmentProfileBinding? = null
//    private val binding get() = _binding!!
//
//    companion object {
//        private const val REQ_ONE_TAP = 100
//        private const val TAG = "ProfileFragment"
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentProfileBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        textView = binding.textProfile
//        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
//        profileViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//
//        oneTapClient = Identity.getSignInClient(requireContext())
//        signInRequest = BeginSignInRequest.builder()
//            .setPasswordRequestOptions(
//                BeginSignInRequest.PasswordRequestOptions.builder()
//                    .setSupported(true)
//                    .build()
//            )
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    .setServerClientId("358827406368-u6uknvfvrh8lfalrae8biobjjp604igi.apps.googleusercontent.com")
//                    .setFilterByAuthorizedAccounts(false)
//                    .build()
//            )
//            .setAutoSelectEnabled(true)
//            .build()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    fun buttonGoogleSignIn(view: View) {
//        oneTapClient.beginSignIn(signInRequest)
//            .addOnSuccessListener(requireActivity(), OnSuccessListener { result ->
//                try {
//                    startIntentSenderForResult(
//                        result.pendingIntent.intentSender, REQ_ONE_TAP,
//                        null, 0, 0, 0
//                    )
//                } catch (e: IntentSender.SendIntentException) {
//                    Log.e(TAG, "Couldn't start One Tap UI: " + e.localizedMessage)
//                }
//            })
//            .addOnFailureListener(requireActivity(), OnFailureListener { e ->
//                Log.d(TAG, e.localizedMessage)
//            })
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            REQ_ONE_TAP -> try {
//                val credential = oneTapClient.getSignInCredentialFromIntent(data)
//                val idToken = credential.googleIdToken
//                val username = credential.id
//                val password = credential.password
//                textView.text = "Authentication done.\nUsername is $username"
//                if (idToken != null) {
//                    Log.d(TAG, "Got ID token.")
//                } else if (password != null) {
//                    Log.d(TAG, "Got password.")
//                }
//            } catch (e: ApiException) {
//                textView.text = e.toString()
//            }
//        }
//    }
//}


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
//            .setServerClientId("358827406368-u6uknvfvrh8lfalrae8biobjjp604igi.apps.googleusercontent.com")
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
//
//    Button(onClick = onClick) {
//        Text("Sign in with Google")
//    }
//}

