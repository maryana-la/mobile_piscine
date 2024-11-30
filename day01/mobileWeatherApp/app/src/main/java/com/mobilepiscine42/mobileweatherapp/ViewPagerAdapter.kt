package com.mobilepiscine42.mobileweatherapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Currently()
            1 -> Today()
            2 -> Weekly()
            else -> Currently()
        }
    }

    override fun getItemCount(): Int {
        return 3 // Number of tabs
    }
}