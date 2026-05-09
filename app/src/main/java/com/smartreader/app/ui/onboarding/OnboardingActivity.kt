package com.smartreader.app.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.smartreader.app.App
import com.smartreader.app.databinding.ActivityOnboardingBinding
import com.smartreader.app.ui.main.MainActivity
import com.smartreader.app.ui.onboarding.adapter.OnboardingAdapter
import kotlinx.coroutines.launch

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    val viewModel: OnboardingViewModel by viewModels {
        OnboardingViewModel.Factory((application as App).appPreferences)
    }

    private lateinit var adapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupButtons()
        updateUi(0)
    }

    private fun setupViewPager() {
        adapter = OnboardingAdapter(this)
        binding.viewPager.apply {
            this.adapter = adapter
            isUserInputEnabled = false          // navigation only via buttons
            offscreenPageLimit  = 1
        }

        // Dot indicators
        TabLayoutMediator(binding.dotsIndicator, binding.viewPager) { _, _ -> }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) = updateUi(position)
        })
    }

    private fun setupButtons() {
        binding.btnNext.setOnClickListener  { handleNext()  }
        binding.btnBack.setOnClickListener  { handleBack()  }
        binding.btnSkip.setOnClickListener  { skipOnboarding() }
    }

    private fun handleNext() {
        val pos = binding.viewPager.currentItem
        if (!viewModel.isPageValid(pos)) {
            Toast.makeText(this, viewModel.validationMessage(pos), Toast.LENGTH_SHORT).show()
            return
        }
        if (pos < adapter.itemCount - 1) {
            binding.viewPager.currentItem = pos + 1
        } else {
            completeOnboarding()
        }
    }

    private fun handleBack() {
        val pos = binding.viewPager.currentItem
        if (pos > 0) binding.viewPager.currentItem = pos - 1
    }

    private fun updateUi(position: Int) {
        val isFirst = position == 0
        val isLast  = position == adapter.itemCount - 1

        binding.btnBack.visibility = if (isFirst) View.INVISIBLE else View.VISIBLE
        binding.btnNext.text       = if (isLast) getString(com.smartreader.app.R.string.btn_get_started)
                                     else getString(com.smartreader.app.R.string.btn_next)
        // Show step label
        binding.tvStep.text        = "${position + 1} / ${adapter.itemCount}"

        // Update the page title from string resources
        val titles = listOf(
            getString(com.smartreader.app.R.string.onboarding_title_country),
            getString(com.smartreader.app.R.string.onboarding_title_age),
            getString(com.smartreader.app.R.string.onboarding_title_purpose),
            getString(com.smartreader.app.R.string.onboarding_title_language)
        )
        binding.tvTitle.text       = titles.getOrElse(position) { "" }
    }

    private fun completeOnboarding() {
        lifecycleScope.launch {
            viewModel.saveOnboarding()
            startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun skipOnboarding() {
        // Allow skipping — they can redo from Settings
        lifecycleScope.launch {
            viewModel.saveOnboarding()
            startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))
            finish()
        }
    }
}
