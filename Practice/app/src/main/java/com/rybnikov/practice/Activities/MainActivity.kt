package com.rybnikov.practice.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.rybnikov.practice.CurrentUser
import com.rybnikov.practice.Fragments.*
import com.rybnikov.practice.Models.User
import com.rybnikov.practice.R
import org.w3c.dom.Text

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
lateinit var drawer:DrawerLayout
lateinit var profileName:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawer= findViewById(R.id.drawer_layout)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.support_toolbar)
        this.setSupportActionBar(toolbar)
        val navigationView:NavigationView = findViewById(R.id.nav_menu)
        navigationView.setNavigationItemSelectedListener(this)
        val view = navigationView.getHeaderView(0)
        profileName = view.findViewById(R.id.profile_nav_name)

        profileName.setOnClickListener {
            if (CurrentUser.user.UserID==0)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, LoginFragment()).commit()
            drawer.closeDrawer(Gravity.LEFT)

        }
        val menubutton:ImageButton = findViewById(R.id.showmenubutton)
        menubutton.setOnClickListener { shommenu() }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_dishes -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, DishesFragment()).commit()
            R.id.nav_cart -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, CartFragment()).commit()
            R.id.nav_profile ->
                goToProfile()
            R.id.nav_about ->supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, AboutFragment()).commit()
            R.id.nav_contacts ->supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, ContactsFragment()).commit()
            R.id.nav_logout -> {
                CurrentUser.user = User("", "","","", "")
                profileName.text = "Вход"
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, LoginFragment()).commit()
            }
            R.id.nav_orders -> if (CurrentUser.user.UserID==0)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, RequestLoginFragment()).commit()
            else supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, OrdersFragment()).commit()


        }
        drawer.closeDrawer(Gravity.LEFT)
        return true
    }

    fun goToProfile() {
        if (CurrentUser.user.UserID==0)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, RequestLoginFragment()).commit()
        else supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, ProfileFragment()).commit()
    }

    fun showCart(view: View) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, CartFragment()).commit()
    }
    fun shommenu(){
        drawer.openDrawer(Gravity.LEFT)
    }
    fun showFilters(view: View){

    }
}

