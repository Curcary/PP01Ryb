package com.rybnikov.practice.Fragments

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rybnikov.practice.Adapters.DishAdapter
import com.rybnikov.practice.Api.RestrauntApiClient
import com.rybnikov.practice.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DishesFragment:Fragment(R.layout.dishes_fragment) {
    private val client by lazy {
        this@DishesFragment.context?.let { RestrauntApiClient.create(it) }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: ListView = view.findViewById(R.id.dishesListView)
        CoroutineScope(Dispatchers.IO).launch {
            client?.getDishes(
            )
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { result ->
                        val adapter: DishAdapter? = this@DishesFragment.context?.let { DishAdapter(it,R.layout.dish_item, result) }
                        recyclerView.adapter = adapter
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
                        Toast.makeText(this@DishesFragment.context, errorMessage, Toast.LENGTH_LONG).show()
                    }
                )
        }
    }
    fun showFilterDialog() {

    }
}