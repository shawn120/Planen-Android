package com.example.planmanager.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.planmanager.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.example.planmanager.ui.Profile.ProfileFragment

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var googleBtn: ImageView
    private lateinit var clientId: String // Declare clientId property

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        clientId = getString(R.string.andriod_client) // Initialize clientId here

        googleBtn = view.findViewById(R.id.google_btn)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId) // Use the client ID here
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(requireContext(), gso)


        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (acct != null) {
            navigateToProfileFragment()
        }

        googleBtn.setOnClickListener {
            signIn()
        }

        val username: TextView = view.findViewById(R.id.username)
        val password: TextView = view.findViewById(R.id.password)
        val loginBtn: MaterialButton = view.findViewById(R.id.loginbtn)

        // temp way for log in
        loginBtn.setOnClickListener {
            if (username.text.toString() == "admin" && password.text.toString() == "admin") {
                // correct
                Toast.makeText(requireContext(), "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show()
            } else {
                // incorrect
                Toast.makeText(requireContext(), "LOGIN FAILED !!!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun signIn() {
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun navigateToProfileFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToProfileFragment()
        findNavController().navigate(action)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)
                navigateToProfileFragment()
            } catch (e: ApiException) {
                Toast.makeText(requireContext().applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 1000
    }
}
