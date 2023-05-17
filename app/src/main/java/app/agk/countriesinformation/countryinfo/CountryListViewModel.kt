package app.agk.countriesinformation.countryinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CountryListViewModel : ViewModel() {

    private val _listUIState = MutableStateFlow<List<String>>(listOf())

    fun fetchList(loadListIfNeeded : () -> List<String>): StateFlow<List<String>> {
        if(_listUIState.value.isEmpty()){
            viewModelScope.launch {
                _listUIState.value = loadListIfNeeded()
            }
        }
        return _listUIState
    }
}