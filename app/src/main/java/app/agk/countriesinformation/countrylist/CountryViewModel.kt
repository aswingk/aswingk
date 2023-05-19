package app.agk.countriesinformation.countrylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.agk.countriesinformation.models.ICountryListRepository
import kotlinx.coroutines.launch

class CountryViewModel(private val repository: ICountryListRepository) : ViewModel() {
    fun updateSearchQuery(query: String) {
        searchQuery = query
        updateSearch()
    }

    private fun updateSearch() {
        val result = if (searchQuery.isBlank()) originalList else {
            originalList.filter {
                it.trim().lowercase().startsWith(searchQuery)
            }
        }

        val sortedResult = if(isSortOrderAsc) result.sorted() else result.sortedDescending()

        searchQueryLiveData.value = searchQuery

        _countryListUIState.postValue(sortedResult)
    }

    fun toggleSortFilter(){
        isSortOrderAsc = !isSortOrderAsc
        updateSearch()
    }

    val searchQueryLiveData = MutableLiveData<String>()
    private var isSortOrderAsc = true
    private var searchQuery = ""

    private lateinit var originalList : List<String>
    private val _countryListUIState = MutableLiveData<List<String>>(listOf())
    val countryListLiveData: LiveData<List<String>> = _countryListUIState

    init {
        viewModelScope.launch {
            originalList = repository.fetchCountryList()
            updateSearch()
        }
    }
}