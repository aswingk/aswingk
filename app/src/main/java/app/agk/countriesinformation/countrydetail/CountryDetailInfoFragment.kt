package app.agk.countriesinformation.countrydetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import app.agk.countriesinformation.TAG
import app.agk.countriesinformation.countryinfo.CountryViewModel
import app.agk.countriesinformation.countryinfo.DisplayCountryDetailsUiState
import app.agk.countriesinformation.databinding.CountryDetailInfoBinding
import app.agk.countriesinformation.utils.Injector
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class CountryDetailInfoFragment : Fragment() {
    private lateinit var binding : CountryDetailInfoBinding
    private val viewModel: CountryViewModel by viewModels { Injector.provideCountriesViewModelFactory(requireContext()) }
    private val args : CountryDetailInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CountryDetailInfoBinding.inflate(inflater)
        return binding.root
    }

    private fun updateUI(displayCountryDetailsUiState : DisplayCountryDetailsUiState) {

        if (displayCountryDetailsUiState.isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            return
        }

        binding.progressBar.visibility = View.GONE
        binding.capitalName.setText(displayCountryDetailsUiState.capital)
        binding.population.setText(displayCountryDetailsUiState.population)
        binding.area.setText(displayCountryDetailsUiState.area)
        binding.region.setText(displayCountryDetailsUiState.region)
        binding.subRegion.setText(displayCountryDetailsUiState.subregion)

        displayCountryDetailsUiState.userMessage?.let {
            Snackbar
                .make(requireView(), it, Snackbar.LENGTH_LONG)
                .show()
        }
    }

    private fun setCountryNameUI(countryName: String) {
        if (countryName.isEmpty()) return
        binding.countryName.setText(countryName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val countryName = args.countryName
        setCountryNameUI(countryName)

        Log.d(TAG, "onViewCreated: countryName: ${countryName}")
        lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    updateUI(it)
                }
        }
        viewModel.loadCountryData(countryName)
    }
}