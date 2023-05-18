package app.agk.countriesinformation.countrylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import app.agk.countriesinformation.models.ICountryListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CountryViewModel(private val repository: ICountryListRepository) : ViewModel() {

    // Save Instance State

    private val _countryListUIState = MutableStateFlow<List<String>>(listOf())
    val countryListLiveData: LiveData<List<String>> = _countryListUIState.asLiveData()

    init {
        viewModelScope.launch {
            _countryListUIState.value = repository.fetchCountryList()
        }
    }
}