package com.example.planmanager.ui.Profile

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.fragment.app.Fragment
import androidx.compose.ui.platform.LocalContext
import com.example.planmanager.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.example.planmanager.databinding.FragmentProfileBinding
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import java.security.MessageDigest
import java.util.UUID
import androidx.compose.ui.platform.ComposeView



class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                googleSignIn()
            }
        }
    }

}

@Composable
private fun googleSignIn (){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val onClick: () -> Unit = {
        val credentialManager = CredentialManager.create(context)

        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold(""){Str, it -> Str + "%02x".format((it))}

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("358827406368-co1lln88rsvr6rgl14s6t22mf4gc8ck3.apps.googleusercontent.com")
            .setNonce(hashedNonce)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )
                val credential = result.credential

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                val googleIdToken = googleIdTokenCredential.idToken

                Log.i(TAG, googleIdToken)

                Toast.makeText(context, "SUCCESSFUL SIGN IN", Toast.LENGTH_SHORT).show()
            }catch(e: GetCredentialException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }catch(e: GoogleIdTokenParsingException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }

        }
    }
    //button
    Button(onClick = onClick) {
        Text("Sign in with Google")
    }
}