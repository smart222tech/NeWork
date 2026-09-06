package ru.netology.nmedia

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.auth.AuthViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.top_app_menu, menu)
                
                val state = viewModel.authState.value
                val authenticated = state is AuthState.Authenticated
                
                menu.findItem(R.id.login).isVisible = !authenticated
                menu.findItem(R.id.register).isVisible = !authenticated
                menu.findItem(R.id.profile).isVisible = authenticated
                menu.findItem(R.id.logout).isVisible = authenticated
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.login -> {
                        navController.navigate(R.id.loginFragment)
                        true
                    }
                    R.id.register -> {
                        navController.navigate(R.id.registrationFragment)
                        true
                    }
                    R.id.profile -> {
                        val state = viewModel.authState.value
                        if (state is AuthState.Authenticated) {
                            val bundle = Bundle().apply { putLong("userId", state.id) }
                            navController.navigate(R.id.profileFragment, bundle)
                        }
                        true
                    }
                    R.id.logout -> {
                        viewModel.logout()
                        true
                    }
                    else -> false
                }
            }
        })

        lifecycleScope.launchWhenStarted {
            viewModel.authState.collectLatest {
                invalidateOptionsMenu()
            }
        }
    }
}
