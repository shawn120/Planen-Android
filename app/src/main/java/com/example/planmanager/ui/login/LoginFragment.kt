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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)



        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (acct != null) {
            navigateToProfileFragment()
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

    private fun navigateToProfileFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToProfileFragment()
        findNavController().navigate(action)
    }

}
