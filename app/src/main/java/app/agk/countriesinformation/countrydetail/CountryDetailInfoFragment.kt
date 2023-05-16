package app.agk.countriesinformation.countrydetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import app.agk.countriesinformation.TAG
import app.agk.countriesinformation.countryinfo.CountriesViewModel
import app.agk.countriesinformation.data.source.local.CountryInfo
import app.agk.countriesinformation.databinding.CountryDetailInfoBinding
import app.agk.countriesinformation.utils.Injector

class CountryDetailInfoFragment : Fragment() {
    private lateinit var binding : CountryDetailInfoBinding
    private val viewModel: CountriesViewModel by viewModels { Injector.provideCountriesViewModelFactory(requireContext()) }
    private val args : CountryDetailInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CountryDetailInfoBinding.inflate(inflater)
        return binding.root
    }

    private fun updateUI(countryInfo : CountryInfo){
        setCountryNameUI(countryInfo.name)
        binding.capitalName.setText(countryInfo.capital.run {
            this?.firstOrNull() ?: ""
        })
        binding.population.setText("${countryInfo.population}")
        binding.area.setText("${countryInfo.area}")
        binding.region.setText(countryInfo.region)
        binding.subRegion.setText(countryInfo.subregion)
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
        viewModel.countryInfo(countryName).observe(viewLifecycleOwner, {
            it?.let {
                updateUI(it)
            }
        })
    }
}