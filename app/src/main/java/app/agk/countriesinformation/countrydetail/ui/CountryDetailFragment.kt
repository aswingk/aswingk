package app.agk.countriesinformation.countrydetail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import app.agk.countriesinformation.countrylist.CountryDetailViewModel
import app.agk.countriesinformation.databinding.CountryDetailInfoBinding
import app.agk.countriesinformation.utils.CountryDetailInjector

const val COUNTRY_NAME_KEY = "CountryName"
class CountryDetailFragment : Fragment() {
    private lateinit var binding: CountryDetailInfoBinding
    private val args: CountryDetailFragmentArgs by navArgs()
    val viewModel: CountryDetailViewModel by viewModels {
        CountryDetailInjector.provideCountriesViewModelFactory(requireContext(), args.countryName)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CountryDetailInfoBinding.inflate(inflater)
        return binding.root
    }

    private fun updateCountryDetailsInUI(displayCountryDetailsUiState: DisplayCountryDetailsUIState) {
        if (displayCountryDetailsUiState.isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.dataGroup.visibility = View.GONE
            return
        }

        binding.progressBar.visibility = View.GONE
        displayCountryDetailsUiState.errorMsg?.also {
            binding.errorMsg.text = resources.getString(it)
            binding.errorMsg.visibility = View.VISIBLE
            binding.dataGroup.visibility = View.GONE
            return
        }

        binding.errorMsg.visibility = View.GONE
        binding.dataGroup.visibility = View.VISIBLE
        binding.capitalName.text = displayCountryDetailsUiState.capital
        binding.population.text = displayCountryDetailsUiState.population
        binding.area.text = displayCountryDetailsUiState.area
        binding.region.text = displayCountryDetailsUiState.region
        binding.subRegion.text = displayCountryDetailsUiState.subregion
    }

    private fun updateUITitle(countryName: String) {
        binding.countryName.text = countryName
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var countryName = args.countryName
        savedInstanceState?.let {
            countryName = savedInstanceState.getString(COUNTRY_NAME_KEY, args.countryName)
        }

        updateUITitle(countryName)

        viewModel.countryDetailUILiveData.observe(viewLifecycleOwner) {
            updateCountryDetailsInUI(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(COUNTRY_NAME_KEY, binding.countryName.text.toString())
    }
}