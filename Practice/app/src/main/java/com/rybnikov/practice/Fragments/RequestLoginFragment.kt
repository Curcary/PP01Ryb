package com.rybnikov.practice.Fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rybnikov.practice.R

class RequestLoginFragment : Fragment(R.layout.request_login_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginbutton: Button = view.findViewById(R.id.toAuthButton)
        loginbutton.setOnClickListener {
            (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, LoginFragment()).commit()
        }
    }
}