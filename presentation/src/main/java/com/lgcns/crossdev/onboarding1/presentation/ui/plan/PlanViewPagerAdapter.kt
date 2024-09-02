package com.lgcns.crossdev.onboarding1.presentation.ui.plan

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PlanViewPagerAdapter(
    private val fragments: ArrayList<Fragment>,
    fragment: Fragment
): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}