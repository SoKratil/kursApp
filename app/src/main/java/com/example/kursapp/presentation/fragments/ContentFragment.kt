package com.example.kursapp.presentation.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.View

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController

import com.example.kursapp.databinding.FragmentContentBinding
import com.google.android.material.navigation.NavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.navigateUp
import com.example.kursapp.R

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

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Если устройство в горизонтальной ориентации, скрываем верхнюю панель
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            supportActionBar?.hide()
        }


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
                R.id.dbResViewFragment,
                ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            setToolbarTitle(when (destination.id) {
                R.id.mainlist -> "Главная"
                R.id.buildfragment -> "Конструктор ПК"
                R.id.dbResViewFragment -> "Сохраненные сборки"
                else -> "Продукт"
            })
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                // Обработка нажатия на элемент "mainlist"
                R.id.mainlist, R.id.buildfragment, R.id.dbResViewFragment -> {
                    navController.navigate(menuItem.itemId)
                    drawerLayout.closeDrawer(GravityCompat.START)

                    true
                }

                R.id.theme_switch -> {
                    // Получаем текущий режим ночи
                    val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                    // Определяем новый режим ночи
                    val newNightMode = when (currentNightMode) {
                        Configuration.UI_MODE_NIGHT_YES -> {
                            AppCompatDelegate.MODE_NIGHT_NO
                        }
                        Configuration.UI_MODE_NIGHT_NO -> {
                            AppCompatDelegate.MODE_NIGHT_YES
                        }
                        else -> {
                            // В случае, если система не определила режим ночи, можно просто сменить на противоположный
                            AppCompatDelegate.MODE_NIGHT_YES
                        }
                    }
                    // Устанавливаем новый режим ночи
                    AppCompatDelegate.setDefaultNightMode(newNightMode)
                    // Применяем изменения темы
                    delegate.applyDayNight()
                    // Сообщаем об успешном изменении темы
                    Toast.makeText(this, "Тема изменена", Toast.LENGTH_SHORT).show()
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


