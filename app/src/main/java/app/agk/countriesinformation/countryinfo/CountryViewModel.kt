package app.agk.countriesinformation.countryinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.agk.countriesinformation.R
import app.agk.countriesinformation.data.IRepository
import kotlinx.coroutines.flow.*
import retrofit2.HttpException

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

    fun fetchCountryData(countryName: String): StateFlow<DisplayCountryDetailsUiState> {
        detailUIState = repository.fetchCountryInfo(countryName)
            .map {
                return@map if (it != null) {
                    DisplayCountryDetailsUiState(
                        isLoading = false,
                        capital = it.capital,
                        population = "${it.population}",
                        area = "${it.area}",
                        region = it.region ?: "",
                        subregion = it.subregion ?: "",
                    )
                } else DisplayCountryDetailsUiState(isLoading = true)
            }.catch {
                var errorMsg = R.string.network_error
                if(it is HttpException){
                    errorMsg = R.string.no_data
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