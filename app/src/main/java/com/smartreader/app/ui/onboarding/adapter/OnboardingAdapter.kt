package com.smartreader.app.ui.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.smartreader.app.ui.onboarding.fragments.AgeFragment
import com.smartreader.app.ui.onboarding.fragments.CountryFragment
import com.smartreader.app.ui.onboarding.fragments.LanguageFragment
import com.smartreader.app.ui.onboarding.fragments.PurposeFragment

class OnboardingAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> CountryFragment()
        1 -> AgeFragment()
        2 -> PurposeFragment()
        3 -> LanguageFragment()
        else -> CountryFragment()
    }
}
