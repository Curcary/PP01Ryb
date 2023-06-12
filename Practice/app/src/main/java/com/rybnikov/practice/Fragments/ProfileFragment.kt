package com.rybnikov.practice.Fragments

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rybnikov.practice.Adapters.DishAdapter
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

class ProfileFragment: Fragment(R.layout.profile_layout) {
    private val client by lazy {
        this@ProfileFragment.context?.let { RestrauntApiClient.create(it) }
    }
    fun redactuser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            client?.userUpdate(
                user
            )
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { result ->
                        Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
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
                        Toast.makeText(this@ProfileFragment.context, errorMessage, Toast.LENGTH_LONG).show()
                    }
                )
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val surname:EditText = view.findViewById(R.id.profileSurname)
        val name:EditText = view.findViewById(R.id.profileName)
        val patr:EditText = view.findViewById(R.id.profilePatr)
        val email:EditText = view.findViewById(R.id.profileEmail)
        val oldPass:EditText = view.findViewById(R.id.profileOldPassword)
        val newPass:EditText = view.findViewById(R.id.profileNewPassword)
        val confirmPassword:EditText = view.findViewById(R.id.profilePasswordConfirmation)
        val saveButton: Button = view.findViewById(R.id.profileSaveButton)
        surname.text = Editable.Factory().newEditable(CurrentUser.user.UserSurname)
        name.text =Editable.Factory().newEditable(CurrentUser.user.UserName)
        patr.text =Editable.Factory().newEditable(CurrentUser.user.UserPatr)
        email.text = Editable.Factory().newEditable(CurrentUser.user.UserEmail)
        saveButton.setOnClickListener{
            if (oldPass.text.toString() == CurrentUser.user.UserPassword) {
                if (newPass.text.toString()!="") {
                    if (newPass.text.toString() == confirmPassword.text.toString()) {
                        CurrentUser.user.UserPassword = newPass.text.toString()
                    }
                    else Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_LONG).show()
                }
                else {

                }
                redactuser(CurrentUser.user)
            }
            else Toast.makeText(context, "Старый пароль введён неверно", Toast.LENGTH_LONG).show()
        }
    }
}