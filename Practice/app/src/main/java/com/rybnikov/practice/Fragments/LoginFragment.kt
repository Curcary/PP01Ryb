package com.rybnikov.practice.Fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.rybnikov.practice.Adapters.DishAdapter
import com.rybnikov.practice.Api.RestrauntApiClient
import com.rybnikov.practice.CurrentUser
import com.rybnikov.practice.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginFragment: Fragment(R.layout.login_fragment) {
    lateinit var loginView:EditText
    lateinit var passwordView:EditText
    lateinit var rememberMeCheckBox:CheckBox
    lateinit var prefs: SharedPreferences
    private val client by lazy {
        this@LoginFragment.context?.let { RestrauntApiClient.create(it) }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginView = view.findViewById(R.id.authLoginText)
        prefs =  view.context.getSharedPreferences(
            AppCompatActivity.PERFORMANCE_HINT_SERVICE,
            AppCompatActivity.MODE_PRIVATE
        )


        val navigationView: NavigationView = view.rootView.findViewById(R.id.nav_menu)
        val headerview = navigationView.getHeaderView(0)
        val profileName: TextView = headerview.findViewById(R.id.profile_nav_name)

        passwordView = view.findViewById(R.id.authPasswordText)
        rememberMeCheckBox = view.findViewById(R.id.rememberMeSwitch)
        val login = prefs.getString("login", "")
        val password = prefs.getString("password", "")
        if (login!="") {
            passwordView.text.insert(0, password)
            loginView.text.insert(0, login)
        }
        val loginButton:Button = view.findViewById(R.id.loginButton)


        loginButton.setOnClickListener{
            val login = loginView.text.toString()
            val password = passwordView.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                client?.login(
                    login, password
                )
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(
                        { result ->
                            if (result.UserID!=0) {
                                CurrentUser.user = result
                                if (rememberMeCheckBox.isChecked){
                                    val editor =prefs.edit()
                                    editor.putString("login", login)
                                    editor.putString("password", password)
                                    editor.commit()
                                }
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container_view, DishesFragment()).commit()
                                if (CurrentUser.user.UserID!=0) {
                                    val user = CurrentUser.user
                                    profileName.text = user.UserSurname+" "+user.UserName[0]+". "+ user.UserPatr[0]+"."
                                }
                            }
                            else Toast.makeText(context, "Неправильный пароль или эл почта", Toast.LENGTH_LONG).show()

                        },
                        { error ->
                            var errorMessage: String = ""
                            errorMessage = when (error) {
                                is HttpException ->{
                                    when (error.code()) {
                                        401 ->"Необходимо авторизоваться"
                                        403-> "Это действие нельзя выполнить"
                                        else -> "Неизвестная ошибка"
                                    }
                                }
                                else -> {
                                    error.localizedMessage
                                }
                            }
                            println(errorMessage)
                            Toast.makeText(this@LoginFragment.context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    )
            }
        }
    val regButton:Button = view.findViewById(R.id.registrationButton)
        regButton.setOnClickListener{
            (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, RegistrationFragment()).commit()
        }
    }


}