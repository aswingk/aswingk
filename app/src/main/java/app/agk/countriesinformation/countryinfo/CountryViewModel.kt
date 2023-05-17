package app.agk.countriesinformation.countryinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.agk.countriesinformation.R
import app.agk.countriesinformation.data.Country
import app.agk.countriesinformation.data.IRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)

    private val _country: MutableStateFlow<Result<Country>?> = MutableStateFlow(null)

    val uiState: StateFlow<DisplayCountryDetailsUiState> = combine(
        _userMessage, _isLoading, _country
    ) { userMessage, isLoading, country ->

        if (isLoading || country == null) return@combine DisplayCountryDetailsUiState(isLoading = true)

        return@combine when (country.isSuccess) {
            true -> {
                DisplayCountryDetailsUiState(
                    isLoading = false,
                    capital = country.getOrNull()?.capital?.firstOrNull() ?: "",
                    population = "${country.getOrNull()?.population}",
                    area = "${country.getOrNull()?.area}",
                    region = country.getOrNull()?.region ?: "",
                    subregion = country.getOrNull()?.subregion ?: "",
                )
            } false -> {
                DisplayCountryDetailsUiState(
                    isLoading = false,
                    userMessage = userMessage
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DisplayCountryDetailsUiState(isLoading = true)
    )

    fun loadCountryData(countryName: String) = viewModelScope.launch {
        repository.fetchCountryInfo(countryName)
            .map {
                if (it != null) {
                    _country.value = Result.success(it)
                    _isLoading.value = false
                }
            }.catch {
                _userMessage.value = R.string.no_data
                _country.value = Result.failure(it)
                _isLoading.value = false
            }.stateIn(viewModelScope)
    }
}
