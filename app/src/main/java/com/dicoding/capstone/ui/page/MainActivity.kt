package com.dicoding.capstone.ui.page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.capstone.R
import com.dicoding.capstone.ui.page.fragment.HistoryFragment
import com.dicoding.capstone.ui.page.fragment.HomeFragment
import com.dicoding.capstone.ui.page.fragment.ProfilFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_home -> selectedFragment = HomeFragment.newInstance(getUsername())
                R.id.nav_history -> selectedFragment = HistoryFragment()
                R.id.nav_profile -> selectedFragment = ProfilFragment()
                // Tambahkan fragment lain sesuai kebutuhan
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, it)
                    .commit()
            }
            true
        }

        // Set default fragment
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.nav_home
        }
    }

    private fun getUsername(): String {
        return intent.getStringExtra("username") ?: ""
    }
}
