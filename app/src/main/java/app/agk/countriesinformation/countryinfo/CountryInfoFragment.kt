package app.agk.countriesinformation.countryinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.agk.countriesinformation.R
import app.agk.countriesinformation.databinding.CountryInfoBinding

class CountryInfoFragment : Fragment() {
    private lateinit var binding : CountryInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CountryInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val countries = resources
            .getStringArray(R.array.countries_list)

        binding.countriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = CountryInfoAdapter(object : CountryItemClickListener {
                override fun navigateToDetailView(countryName: String) {
                    val action = CountryInfoFragmentDirections
                        .actionCountryInfoFragmentToCountryDetailInfoFragment(countryName)
                    findNavController().navigate(action)
                }
            }).also {
                it.submitList(countries.toList())
            }
        }

/*        binding.fastScrollList.apply {
            adapter = ArrayAdapter(this.context,
                R.layout.country_item_info,
                countries.map {
                    it.get(0).toString()
                }.distinct().toTypedArray()
            )
        }*/
    }
}