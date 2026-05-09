package com.smartreader.app.ui.onboarding.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.smartreader.app.databinding.FragmentAgeBinding
import com.smartreader.app.ui.onboarding.OnboardingViewModel

class AgeFragment : Fragment() {

    private var _binding: FragmentAgeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.age.value > 0)
            binding.etAge.setText(viewModel.age.value.toString())

        binding.etAge.addTextChangedListener {
            viewModel.setAge(it.toString().toIntOrNull() ?: 0)
        }

        // Quick-range chips
        val ranges = listOf("Under 13" to 12, "13 – 17" to 15,
                            "18 – 24" to 20, "25 – 34" to 28,
                            "35 – 49" to 40, "50 +" to 55)
        ranges.forEach { (label, sample) ->
            val chip = com.google.android.material.chip.Chip(requireContext()).apply {
                text = label
                isCheckable = true
                setOnCheckedChangeListener { _, checked ->
                    if (checked) {
                        viewModel.setAge(sample)
                        binding.etAge.setText(sample.toString())
                    }
                }
            }
            binding.chipGroupAge.addView(chip)
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
