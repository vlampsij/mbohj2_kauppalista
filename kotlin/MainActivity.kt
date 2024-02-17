package com.example.kauppalistatodo

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(ShoppingList())

        val botNav : BottomNavigationView = findViewById(R.id.nav_view)

        botNav.setOnItemSelectedListener{

            when (it.itemId){
                R.id.navigation_shoppinglist -> replaceFragment(ShoppingList())
                R.id.navigation_notes -> replaceFragment(Notes())
                R.id.navigation_about -> replaceFragment(About())
                else ->{

                }
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}
