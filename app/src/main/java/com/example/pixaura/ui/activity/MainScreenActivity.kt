package com.example.pixaura.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.pixaura.R
import com.example.pixaura.data.model.NavItem
import com.example.pixaura.databinding.ActivityMainScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var navController: NavController
    private var currentIndex = 0
    private val PERMISSION_REQUEST_CODE = 1001

    private val REQUIRED_PERMISSIONS = mutableListOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
            add(Manifest.permission.READ_MEDIA_IMAGES)
        }
    }.toTypedArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestNecessaryPermissions()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController


        setupCustomBottomBar()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> updateBottomBarUI(binding.customBottomBar, 0).also { currentIndex = 0 }
                R.id.categoryFragment -> updateBottomBarUI(binding.customBottomBar, 1).also { currentIndex = 1 }
                R.id.searchFragment -> updateBottomBarUI(binding.customBottomBar, 2).also { currentIndex = 2 }
                R.id.accountFragment -> updateBottomBarUI(binding.customBottomBar, 3).also { currentIndex = 3 }
                else -> {} // Sub-screens do not affect bottom bar UI
            }
        }
    }

    private fun setupCustomBottomBar() {
        val navItems = listOf(
            NavItem(0, R.drawable.home, "Home", R.id.homeFragment),
            NavItem(1, R.drawable.baseline_category_24, "Category", R.id.categoryFragment),
            NavItem(2, R.drawable.search, "Search", R.id.searchFragment),
            NavItem(3, R.drawable.account, "Profile", R.id.accountFragment)
        )

        val inflater = layoutInflater
        val bar = binding.customBottomBar
        bar.removeAllViews()

        navItems.forEachIndexed { index, item ->
            val itemView = inflater.inflate(R.layout.bottom_nav_item, bar, false)

            val icon = itemView.findViewById<ImageView>(R.id.icon)
            val title = itemView.findViewById<TextView>(R.id.title)

            icon.setImageResource(item.iconRes)
            title.text = item.title

            itemView.setOnClickListener {
                if (currentIndex != index) {
                    navController.navigate(item.destinationId)
                    updateBottomBarUI(bar, index)
                    currentIndex = index
                }
            }

            bar.addView(itemView)
        }

        updateBottomBarUI(bar, 0)
    }

    fun navigateToTab(destinationId: Int) {
        val index = when (destinationId) {
            R.id.homeFragment -> 0
            R.id.categoryFragment -> 1
            R.id.searchFragment -> 2
            R.id.accountFragment -> 3
            else -> return
        }

        if (currentIndex != index) {
            navController.navigate(destinationId)
            updateBottomBarUI(binding.customBottomBar, index)
            currentIndex = index
        }
    }

    private fun updateBottomBarUI(bar: LinearLayout, selectedIndex: Int) {
        for (i in 0 until bar.childCount) {
            val item = bar.getChildAt(i)
            val icon = item.findViewById<ImageView>(R.id.icon)
            val title = item.findViewById<TextView>(R.id.title)

            val selected = i == selectedIndex
            val color = if (selected) getColor(R.color.black) else getColor(R.color.white)

            icon.setColorFilter(color)
            title.setTextColor(color)

            icon.animate()
                .scaleX(if (selected) 1.2f else 1f)
                .scaleY(if (selected) 1.2f else 1f)
                .setDuration(150)
                .start()

            title.animate()
                .alpha(if (selected) 1f else 0.6f)
                .setDuration(150)
                .start()
        }
    }
    private fun requestNecessaryPermissions() {
        val permissionsToRequest = REQUIRED_PERMISSIONS.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        val currentId = navController.currentDestination?.id
        if (currentId == R.id.homeFragment) {
            super.onBackPressed()
        } else {
            navController.popBackStack()
        }
    }

}

