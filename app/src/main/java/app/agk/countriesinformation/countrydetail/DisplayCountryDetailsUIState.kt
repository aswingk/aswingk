package app.agk.countriesinformation.countrydetail

data class DisplayCountryDetailsUIState(
    val capital: String = "",
    val population: String = "",
    val area: String = "",
    val region: String = "",
    val subregion: String = "",
    val userMessage: Int? = null,
    val isLoading: Boolean = false,
)