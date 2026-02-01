package com.keresman.flatearth

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.appcompat.app.AlertDialog
import com.keresman.flatearth.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleTransition()
        binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initHamburgerMenu()
        initNavigation()
    }

    private fun initHamburgerMenu() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> { toggleDrawer(); true }
        R.id.miExit -> { exitApp(); true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun toggleDrawer() = with(binding.drawerLayout)  {
        if (isDrawerOpen(GravityCompat.START))
            closeDrawers()
        else
            openDrawer(GravityCompat.START)
    }

    private fun exitApp() = AlertDialog.Builder(this).apply {
        setTitle(R.string.exit)
        setMessage(getString(R.string.really_exit_app))
        setIcon(R.drawable.exit)
        setNegativeButton(getString(R.string.cancel), null)
        setPositiveButton(getString(R.string.ok)) { _, _ -> finish() }
        show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.host_menu, menu)
        return true
    }

    private fun initNavigation() {
        val navController = findNavController(this, R.id.navController)
        NavigationUI.setupWithNavController(binding.navView, navController)
    }

    private fun handleTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }
    }
}