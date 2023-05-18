package app.agk.countriesinformation.countrydetail.ui

import app.agk.countriesinformation.models.communication.Country

data class DisplayCountryDetailsUIState(
    val capital: String = "",
    val population: String = "",
    val area: String = "",
    val region: String = "",
    val subregion: String = "",
    val isLoading: Boolean = false,
    val errorMsg: Int? = null
)

fun Country.toDisplayCountryDetailsUIState() : DisplayCountryDetailsUIState {
    return let {
        DisplayCountryDetailsUIState(
            isLoading = false,
            capital = it.capital,
            population = "${it.population}",
            area = "${it.area}",
            region = it.region,
            subregion = it.subregion
        )
    }
}