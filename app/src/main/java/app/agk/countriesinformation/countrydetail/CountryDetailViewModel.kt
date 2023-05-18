package app.agk.countriesinformation.countrylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import app.agk.countriesinformation.R
import app.agk.countriesinformation.countrydetail.ui.DisplayCountryDetailsUIState
import app.agk.countriesinformation.countrydetail.ui.toDisplayCountryDetailsUIState
import app.agk.countriesinformation.models.ICountryInfoRepository
import app.agk.countriesinformation.models.communication.ResourceResultState
import app.agk.countriesinformation.utils.ErrorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CountryDetailViewModel internal constructor(
    private val repository: ICountryInfoRepository,
    private val countryName: String,
) : ViewModel() {

    private val _detailUIState = MutableStateFlow(DisplayCountryDetailsUIState(isLoading = true))
    val countryDetailUILiveData: LiveData<DisplayCountryDetailsUIState> =
        _detailUIState.asLiveData()

    init {
        viewModelScope.launch {
            repository.fetchCountryInfo(countryName)
                .collect {
//                Log.d(TAG, "fetchCountryData: $countryName ... ... ... $it")
                    val value = when (it) {
                        is ResourceResultState.Success -> it.value.toDisplayCountryDetailsUIState()
                        is ResourceResultState.Loading -> DisplayCountryDetailsUIState(isLoading = true)
                        is ResourceResultState.Failure -> {
                            val errorMsg = when (it.customThrowable.errorState) {
                                ErrorState.HttpError -> R.string.server_error
                                ErrorState.NoNetworkError -> R.string.network_error
                                else -> R.string.unconfined_error
                            }
                            DisplayCountryDetailsUIState(errorMsg = errorMsg)
                        }
                    }
                    _detailUIState.value = value
                }
        }
    }
}