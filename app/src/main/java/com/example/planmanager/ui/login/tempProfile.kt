package com.example.planmanager.ui.login.tempProfile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.planmanager.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
class tempProfile: Fragment() {

    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var signOutBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

            name = view.findViewById(R.id.name)
            email = view.findViewById(R.id.email)
            signOutBtn = view.findViewById(R.id.signout)

            gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

            gsc = GoogleSignIn.getClient(requireContext(), gso)

            val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
            if (acct != null) {
                val personName = acct.displayName
                val personEmail = acct.email
                name.text = personName
                email.text = personEmail
            }

            signOutBtn.setOnClickListener {
                signOut()
            }

            return view
        }

        private fun signOut() {
            gsc.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(requireContext(), tempProfile::class.java))
                }
            }
        }
    }
