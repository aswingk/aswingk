package app.agk.countriesinformation.countrydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import app.agk.countriesinformation.countryinfo.CountryViewModel
import app.agk.countriesinformation.countryinfo.DisplayCountryDetailsUiState
import app.agk.countriesinformation.databinding.CountryDetailInfoBinding
import app.agk.countriesinformation.utils.Injector
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class CountryDetailFragment : Fragment() {
    private lateinit var binding : CountryDetailInfoBinding
    private val args : CountryDetailFragmentArgs by navArgs()
    val viewModel: CountryViewModel by viewModels {
        Injector.provideCountriesViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CountryDetailInfoBinding.inflate(inflater)
        return binding.root
    }

    private fun updateUI(displayCountryDetailsUiState : DisplayCountryDetailsUiState) {

        if (displayCountryDetailsUiState.isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            return
        }

        binding.progressBar.visibility = View.GONE
        binding.capitalName.text = displayCountryDetailsUiState.capital
        binding.population.text = displayCountryDetailsUiState.population
        binding.area.text = displayCountryDetailsUiState.area
        binding.region.text = displayCountryDetailsUiState.region
        binding.subRegion.text = displayCountryDetailsUiState.subregion

        displayCountryDetailsUiState.userMessage?.let {
            Snackbar
                .make(requireView(), it, Snackbar.LENGTH_LONG)
                .show()
        }
    }

    private fun setCountryNameUI(countryName: String) {
        binding.countryName.text = countryName
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val countryName = args.countryName
        setCountryNameUI(countryName)

        lifecycleScope.launch {
            viewModel.fetchCountryData(countryName).flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    updateUI(it)
                }
        }
    }
}