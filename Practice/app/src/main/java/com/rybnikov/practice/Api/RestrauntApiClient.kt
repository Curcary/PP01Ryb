package com.rybnikov.practice.Api

import android.content.Context
import com.rybnikov.practice.Models.*
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RestrauntApiClient {

    // Category
    @GET("Categories")
    fun getArticles(): Observable<List<Category>>

    // Orders

    @GET("UsersOrders/{user_id}")
    fun getUsersOrders(@Path("user_id") id: Int): Observable<List<Order>>

    @GET("OrderedDishes/{id}/{date}")
    fun getOrderedDishesByID(@Path("id") id: Int, @Path("date") date:String): Observable<List<OrderedDish>>

    @POST("Orders")
    fun makeOrder(@Body newOrder:Order): Observable<Message>


    @POST("Email/Send/{email}")
    fun sendEmail(@Path("email") email: String, @Body order: Order): Observable<Message>

    // User
    @GET("Users/{email}/{password}")
    fun login(@Path("email") email: String, @Path("password") password: String): Observable<User>

    @POST("Users/Create")
    fun userRegistration(@Body newUser: User): Observable<Message>

    @PUT("Users/Redact")
    fun userUpdate(@Body updatedUser: User): Observable<Message>

    //Dishes
    @GET("Dishes/0")
    fun getDishes(): Observable<List<Dish>>

    @GET("Dishes/{id}")
    fun getDish(@Path("id") id:Int):Dish

    @GET("Dishes/getByIDs/{list}")
    fun getDishesByIDs(@Path("list") ids: String): Observable<ArrayList<Dish>>

    companion object {
        fun create(context: Context): RestrauntApiClient {
            val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(ApiInterceptor(context))
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl("https://api.gfenixl.me/Rybn/")
                .build()

            return retrofit.create(RestrauntApiClient::class.java);
        }

    }
}