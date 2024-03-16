package com.example.planmanager.ui.login

import android.os.Bundle
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import android.widget.Toast
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.planmanager.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.button.MaterialButton
import com.google.android.gms.common.api.ApiException
import com.example.planmanager.ui.Today.TodayFragment
import com.example.planmanager.ui.Profile.ProfileFragment



class LoginFragment : Fragment() {
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var googleBtn: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        googleBtn = view.findViewById(R.id.google_btn)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(requireContext(), gso)

        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (acct != null) {
            navigateToStart()
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
        startActivityForResult(signInIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                task.getResult(ApiException::class.java)
                navigateToStart()
            } catch (e: ApiException) {
                Toast.makeText(requireContext(). applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToStart() {
        val intent = Intent(requireContext(), ProfileFragment::class.java)
        startActivity(intent)
    }


}


