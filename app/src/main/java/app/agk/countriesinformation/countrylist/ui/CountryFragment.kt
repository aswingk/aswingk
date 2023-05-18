package app.agk.countriesinformation.countrylist.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.agk.countriesinformation.countrylist.CountryViewModel
import app.agk.countriesinformation.databinding.CountryInfoBinding
import app.agk.countriesinformation.utils.CountryListInjector

class CountryFragment : Fragment() {
    private lateinit var binding : CountryInfoBinding
    val viewModel: CountryViewModel by viewModels {
        CountryListInjector.provideCountryListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CountryInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutMgr =
            if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
                LinearLayoutManager(context)
            } else {
                GridLayoutManager(context, 2)
            }

        val countryAdapter = CountryAdapter(object : CountryItemClickListener {
            override fun navigateToDetailView(countryName: String) {
                val action = CountryFragmentDirections.actionCountryInfoFragmentToCountryDetailInfoFragment(countryName)
                findNavController().navigate(action)
            }
        })

        binding.countriesRecyclerView.apply {
            layoutManager = layoutMgr
            adapter = countryAdapter
        }

        viewModel.countryListLiveData.observe(viewLifecycleOwner, countryAdapter::submitList)
    }
}