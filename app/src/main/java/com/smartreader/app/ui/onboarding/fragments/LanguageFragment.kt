package com.smartreader.app.ui.onboarding.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.smartreader.app.databinding.FragmentLanguageBinding
import com.smartreader.app.ui.onboarding.OnboardingViewModel

class LanguageFragment : Fragment() {

    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingViewModel by activityViewModels()

    private val languages = listOf(
        "ar" to "🇸🇦 العربية (Arabic)",
        "en" to "🇬🇧 English",
        "fr" to "🇫🇷 Français",
        "de" to "🇩🇪 Deutsch",
        "es" to "🇪🇸 Español",
        "pt" to "🇧🇷 Português",
        "tr" to "🇹🇷 Türkçe",
        "ru" to "🇷🇺 Русский",
        "zh" to "🇨🇳 中文",
        "ja" to "🇯🇵 日本語",
        "ko" to "🇰🇷 한국어",
        "id" to "🇮🇩 Bahasa Indonesia"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        languages.forEach { (code, label) ->
            val chip = Chip(requireContext()).apply {
                text        = label
                isCheckable = true
                isChecked   = viewModel.targetLanguage.value == code
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        viewModel.setTargetLanguage(code)
                        uncheckOthers(this)
                    }
                }
            }
            binding.chipGroupLang.addView(chip)
        }
    }

    private fun uncheckOthers(selected: Chip) {
        for (i in 0 until binding.chipGroupLang.childCount) {
            val c = binding.chipGroupLang.getChildAt(i) as? Chip ?: continue
            if (c !== selected && c.isChecked) c.isChecked = false
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
