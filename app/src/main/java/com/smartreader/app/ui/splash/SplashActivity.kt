package com.smartreader.app.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.smartreader.app.App
import com.smartreader.app.databinding.ActivitySplashBinding
import com.smartreader.app.ui.main.MainActivity
import com.smartreader.app.ui.onboarding.OnboardingActivity
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val viewModel: SplashViewModel by viewModels {
        SplashViewModel.Factory(this, (application as App).appPreferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Keep the splash screen visible while we're checking state
        splashScreen.setKeepOnScreenCondition {
            viewModel.state.value == SplashState.Loading
        }

        binding.btnRetry.setOnClickListener {
            binding.noConnectionGroup.visibility = View.GONE
            binding.loadingGroup.visibility      = View.VISIBLE
            viewModel.checkAndNavigate()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        SplashState.Loading       -> showLoading()
                        SplashState.NoConnection  -> showNoConnection()
                        SplashState.GoToOnboarding -> navigate(OnboardingActivity::class.java)
                        SplashState.GoToMain      -> navigate(MainActivity::class.java)
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.loadingGroup.visibility      = View.VISIBLE
        binding.noConnectionGroup.visibility = View.GONE
        binding.barsAnimation.stopAnimation()
    }

    private fun showNoConnection() {
        binding.loadingGroup.visibility      = View.GONE
        binding.noConnectionGroup.visibility = View.VISIBLE
        binding.barsAnimation.startAnimation()
    }

    private fun <T> navigate(cls: Class<T>) {
        startActivity(Intent(this, cls))
        finish()
    }
}
