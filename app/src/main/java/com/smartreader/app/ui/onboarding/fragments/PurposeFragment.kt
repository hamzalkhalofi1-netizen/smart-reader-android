package com.smartreader.app.ui.onboarding.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import com.smartreader.app.databinding.FragmentPurposeBinding
import com.smartreader.app.ui.onboarding.OnboardingViewModel
import kotlinx.coroutines.launch

class PurposeFragment : Fragment() {

    private var _binding: FragmentPurposeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingViewModel by activityViewModels()

    private val options = listOf(
        "manga"        to "📚 Manga",
        "manhwa"       to "📖 Manhwa",
        "manhua"       to "🎌 Manhua",
        "novels"       to "📝 Novels",
        "light_novels" to "💡 Light Novels",
        "webtoons"     to "🌐 Webtoons",
        "comics"       to "🦸 Comics",
        "doujinshi"    to "🖼️ Doujinshi"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPurposeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        options.forEach { (key, label) ->
            val chip = Chip(requireContext()).apply {
                text        = label
                isCheckable = true
                isChecked   = viewModel.purposes.value.contains(key)
                setOnCheckedChangeListener { _, _ -> viewModel.togglePurpose(key) }
            }
            binding.chipGroupPurpose.addView(chip)
        }

        // Reflect external ViewModel changes (e.g. back-navigation)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.purposes.collect { selected ->
                    for (i in 0 until binding.chipGroupPurpose.childCount) {
                        val chip = binding.chipGroupPurpose.getChildAt(i) as? Chip ?: continue
                        val key  = options.getOrNull(i)?.first ?: continue
                        if (chip.isChecked != selected.contains(key))
                            chip.isChecked = selected.contains(key)
                    }
                }
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
