package com.example.planmanager.ui.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.planmanager.databinding.FragmentProfileBinding
import android.content.Intent
import android.widget.Button
import com.example.planmanager.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class ProfileFragment : Fragment() {

    // added for google sign in
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var signOutBtn: Button
    // ends here

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textProfile
        profileViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        //added
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
        //return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Added sign out functionality
    private fun signOut() {
        gsc.signOut().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(requireContext(), ProfileFragment::class.java))
            }
        }
    }
}