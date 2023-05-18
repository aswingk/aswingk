package app.agk.countriesinformation.countryinfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.agk.countriesinformation.R
import app.agk.countriesinformation.TAG
import app.agk.countriesinformation.data.IRepository
import app.agk.countriesinformation.utils.CustomThrowable
import app.agk.countriesinformation.utils.ErrorState
import kotlinx.coroutines.flow.*

data class DisplayCountryDetailsUiState(
    val capital: String = "",
    val population: String = "",
    val area: String = "",
    val region: String = "",
    val subregion: String = "",
    val userMessage: Int? = null,
    val isLoading: Boolean = false,
)

class CountryViewModel internal constructor(
    private val repository: IRepository
) : ViewModel() {

    lateinit var detailUIState: StateFlow<DisplayCountryDetailsUiState>

    suspend fun fetchCountryData(countryName: String): StateFlow<DisplayCountryDetailsUiState> {
        detailUIState = repository.fetchCountryInfo(countryName)
            .map {
                return@map if (it != null) {
//                    Log.d(TAG, "fetchCountryData: $countryName,,,,,$it")
                    DisplayCountryDetailsUiState(
                        isLoading = false,
                        capital = it.capital,
                        population = "${it.population}",
                        area = "${it.area}",
                        region = it.region,
                        subregion = it.subregion,
                    )
                } else DisplayCountryDetailsUiState(isLoading = true)
            }.catch {
                var errorMsg = R.string.unconfined_error
                if(it is CustomThrowable){
                    errorMsg = when(it.errorState){
                        ErrorState.HttpError -> R.string.server_error
                        ErrorState.NoNetworkError -> R.string.network_error
                        else -> R.string.unconfined_error
                    }
                }
                emit(DisplayCountryDetailsUiState(userMessage = errorMsg))
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = DisplayCountryDetailsUiState(isLoading = true)
            )
        return detailUIState
    }
}