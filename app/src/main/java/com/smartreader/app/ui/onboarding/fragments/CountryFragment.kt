package com.smartreader.app.ui.onboarding.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.smartreader.app.databinding.FragmentCountryBinding
import com.smartreader.app.ui.onboarding.OnboardingViewModel
import java.util.Locale

class CountryFragment : Fragment() {

    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val countries = Locale.getAvailableLocales()
            .mapNotNull { it.displayCountry.takeIf(String::isNotBlank) }
            .distinct().sorted()

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            countries
        )
        binding.etCountry.setAdapter(adapter)
        binding.etCountry.threshold = 1

        if (viewModel.country.value.isNotBlank())
            binding.etCountry.setText(viewModel.country.value)

        binding.etCountry.addTextChangedListener { viewModel.setCountry(it.toString()) }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
