package app.agk.countriesinformation.countrylist.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.agk.countriesinformation.countrydetail.ui.COUNTRY_NAME_KEY
import app.agk.countriesinformation.countrylist.CountryViewModel
import app.agk.countriesinformation.countrylist.search.IFragmentMenuBridge
import app.agk.countriesinformation.countrylist.search.MyMenuProvider
import app.agk.countriesinformation.databinding.CountryInfoBinding
import app.agk.countriesinformation.utils.CountryListInjector


class CountryFragment : Fragment() {
    private lateinit var binding: CountryInfoBinding
    lateinit var searchQuery: String
    val viewModel: CountryViewModel by viewModels {
        CountryListInjector.provideCountryListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CountryInfoBinding.inflate(inflater)

        searchQuery = savedInstanceState?.let {
            savedInstanceState.getString(COUNTRY_NAME_KEY, "")
        } ?: ""

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutMgr =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                LinearLayoutManager(context)
            } else {
                GridLayoutManager(context, 2)
            }

        val countryAdapter = CountryAdapter(object : CountryItemClickListener {
            override fun navigateToDetailView(countryName: String) {
                val action =
                    CountryFragmentDirections.actionCountryInfoFragmentToCountryDetailInfoFragment(
                        countryName
                    )
                findNavController().navigate(action)
            }
        })

        requireActivity().addMenuProvider(MyMenuProvider(fragmentMenuBridge), viewLifecycleOwner)

        binding.countriesRecyclerView.apply {
            layoutManager = layoutMgr
            adapter = countryAdapter
        }

        viewModel.searchQueryLiveData.observe(viewLifecycleOwner) {
            if (!it.isBlank()) searchQuery = it
        }

        viewModel.countryListLiveData.observe(viewLifecycleOwner) {
            binding.errorContainer.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
            countryAdapter.submitList(it)
        }
    }

    val fragmentMenuBridge = object : IFragmentMenuBridge{
        override fun getSearchQuery(): String {
            return searchQuery
        }

        override fun hideKeyboard(binder : IBinder) {
            val inputMethodManager = requireContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binder, 0)
        }

        override fun updateSearch(query: String) {
            viewModel.updateSearchQuery(query.lowercase())
        }

        override fun toggleSortFilter() {
            viewModel.toggleSortFilter()
        }
    }
}