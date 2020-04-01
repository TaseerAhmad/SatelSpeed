package com.minutecodes.satelspeed.activities.interfaces

import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView

interface BottomNavigationListener : BottomNavigationView.OnNavigationItemReselectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemReselected(item: MenuItem) {}
}