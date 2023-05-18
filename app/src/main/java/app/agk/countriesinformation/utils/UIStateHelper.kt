package app.agk.countriesinformation.utils

import app.agk.countriesinformation.R
import app.agk.countriesinformation.countrydetail.DisplayCountryDetailsUIState
import app.agk.countriesinformation.data.Country

fun Country.toDisplayCountryDetailsUIState() : DisplayCountryDetailsUIState {
    return let {
        DisplayCountryDetailsUIState(
            isLoading = false,
            capital = it.capital,
            population = "${it.population}",
            area = "${it.area}",
            region = it.region,
            subregion = it.subregion,
            userMessage = null
        )
    }
}

fun ErrorState.toDisplayCOuntryDetailsUIState() : DisplayCountryDetailsUIState{
    val errorMsg = when (this) {
        ErrorState.HttpError -> R.string.server_error
        ErrorState.NoNetworkError -> R.string.network_error
        else -> R.string.unconfined_error
    }
    return DisplayCountryDetailsUIState(userMessage = errorMsg)
}