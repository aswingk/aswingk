package app.agk.countriesinformation.countryinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.agk.countriesinformation.countrydetail.DisplayCountryDetailsUIState
import app.agk.countriesinformation.data.IRepository
import app.agk.countriesinformation.data.Resource
import app.agk.countriesinformation.utils.toDisplayCOuntryDetailsUIState
import app.agk.countriesinformation.utils.toDisplayCountryDetailsUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CountryViewModel internal constructor(
    private val repository: IRepository
) : ViewModel() {

    private val _detailUIState = MutableStateFlow(DisplayCountryDetailsUIState(isLoading = true))
    val detailUIState: StateFlow<DisplayCountryDetailsUIState> = _detailUIState

    suspend fun fetchCountryData(countryName: String) = viewModelScope.launch {
        repository.fetchCountryInfo(countryName)
            .collect {
//                Log.d(TAG, "fetchCountryData: $countryName ... ... ... $it")
                val value = when (it) {
                    is Resource.Success -> it.value.toDisplayCountryDetailsUIState()
                    is Resource.Loading -> DisplayCountryDetailsUIState(isLoading = true)
                    is Resource.Failure -> it.customThrowable.errorState.toDisplayCOuntryDetailsUIState()
                }
                _detailUIState.value = value
            }
    }
}