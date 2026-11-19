package com.example.als_pocky.onboarding6

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

// Progres 6 (P11): mengatur fragment mana yang tampil di tiap halaman ViewPager2
class OnboardingAdapter(
    activity: OnboardingActivity,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}