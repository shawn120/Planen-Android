package com.example.planmanager.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.planmanager.R
import com.google.android.material.button.MaterialButton
import android.widget.Button
import android.widget.Toast

class LoginFragment : Fragment(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username: TextView = view.findViewById(R.id.username)
        val password: TextView = view.findViewById(R.id.password)

        val loginBtn: MaterialButton = view.findViewById(R.id.loginbtn)

        loginBtn.setOnClickListener {
            if (username.text.toString() == "admin" && password.text.toString() == "admin") {
                // correct
                Toast.makeText(requireContext(), "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show()
            } else {
                // incorrect
                Toast.makeText(requireContext(), "LOGIN FAILED !!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}