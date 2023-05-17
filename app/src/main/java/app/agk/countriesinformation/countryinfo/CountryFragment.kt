package app.agk.countriesinformation.countryinfo

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.agk.countriesinformation.R
import app.agk.countriesinformation.databinding.CountryInfoBinding

class CountryFragment : Fragment() {
    private lateinit var binding : CountryInfoBinding
    val viewModel: CountryListViewModel by viewModels()

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
                val action = CountryFragmentDirections
                    .actionCountryInfoFragmentToCountryDetailInfoFragment(countryName)
                findNavController().navigate(action)
            }
        })

        binding.countriesRecyclerView.apply {
            layoutManager = layoutMgr
            adapter = countryAdapter
        }

        fun loadListIfNeeded() : List<String>{
            return resources
                .getStringArray(R.array.countries_list)
                .toList()
        }

        val countriesLiveData = viewModel.fetchList(::loadListIfNeeded).asLiveData()

        countriesLiveData.observe(viewLifecycleOwner, countryAdapter::submitList)
    }
}