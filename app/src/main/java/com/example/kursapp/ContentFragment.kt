package com.example.kursapp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kursapp.databinding.FragmentContentBinding
import com.google.android.material.navigation.NavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.navigateUp

class ContentFragment : AppCompatActivity() {
    private lateinit var binding: FragmentContentBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration

    private fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentContentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Настройка тулбара
        setSupportActionBar(binding.appBarLayout.toolbar)
        drawerLayout = binding.drawerLayout
        navView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_real) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mainlist,
                R.id.buildfragment,

                ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            setToolbarTitle(when (destination.id) {
                R.id.mainlist -> "Главная"
                R.id.buildfragment -> "Конструктор ПК"
                else -> "Продукт"
            })
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                // Обработка нажатия на элемент "mainlist"
                R.id.mainlist, R.id.buildfragment-> {
                    navController.navigate(menuItem.itemId)
                    drawerLayout.closeDrawer(GravityCompat.START)

                    true
                }



                R.id.menu_item3 -> {
                    // Добавьте здесь код для обработки нажатия на элемент "saved_builds"
                    Toast.makeText(this, "Сохраненные сборки Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                // Добавьте обработку других элементов, если необходимо
                else -> false
            }
        }
    }

        override fun onSupportNavigateUp(): Boolean {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_real) as NavHostFragment
            val navController = navHostFragment.navController
            return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
        }
    }


