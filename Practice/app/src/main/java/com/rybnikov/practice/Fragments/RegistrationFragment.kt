package com.rybnikov.practice.Fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rybnikov.practice.Adapters.CartDishAdapter
import com.rybnikov.practice.Api.RestrauntApiClient
import com.rybnikov.practice.CurrentUser
import com.rybnikov.practice.Models.User
import com.rybnikov.practice.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegistrationFragment: Fragment(R.layout.registration_layout) {
    private var userCreated:Boolean = false
    private val client by lazy {
        this@RegistrationFragment.context?.let { RestrauntApiClient.create(it) }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val regFirstNameView: EditText = view.findViewById(R.id.regFirstnameText)
        val regLastView: EditText = view.findViewById(R.id.regLastnameText)
        val regMiddleView: EditText = view.findViewById(R.id.regMiddlenameText)
        val regEmailView: EditText = view.findViewById(R.id.regEmailText)
        val regPasswordView: EditText = view.findViewById(R.id.regPasswordText)
        val regButton: Button = view.findViewById(R.id.registrate_button)
        regButton.setOnClickListener {
            val fn = regFirstNameView.text.toString()
            val mn = regMiddleView.text.toString()
            val ln = regLastView.text.toString()
            val email = regEmailView.text.toString()
            val password = regPasswordView.text.toString()
            val user: User = User(ln, fn, mn, email, password)
            if (userIsValid(user)) {
                CoroutineScope(Dispatchers.IO).launch {
                    client?.userRegistration(
                        user
                    )
                        ?.subscribeOn(Schedulers.io())
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe(
                            { result ->
                                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                                CurrentUser.user = user
                                userCreated = true
                                (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container_view, DishesFragment()).commit()
                            },
                            { error ->
                                var errorMessage: String = ""
                                errorMessage = when (error) {
                                    is HttpException -> {
                                        when (error.code()) {
                                            401 -> "Необходимо авторизоваться"
                                            403 -> "Это действие нельзя выполнить"
                                            else -> "Неизвестная ошибка"
                                        }
                                    }
                                    else -> {
                                        error.localizedMessage
                                    }
                                }
                                println(errorMessage)
                                Toast.makeText(
                                    this@RegistrationFragment.context,
                                    errorMessage,
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        )
                }
            }
            else {
                val customLayout:View = layoutInflater.inflate(R.layout.alert_dialog_layout, null)
                val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                builder.setPositiveButton("Ок", null).setView(customLayout)
                builder.create().show()
            }
        }
    }

    private fun userIsValid(user: User): Boolean {
        var valid: Boolean = android.util.Patterns.
        EMAIL_ADDRESS.matcher(user.UserEmail).matches()&&user.UserName!=""
                &&user.UserSurname!=""&&user.UserPatr!=""&&user.UserPassword.length>=6
        return valid
    }


}
