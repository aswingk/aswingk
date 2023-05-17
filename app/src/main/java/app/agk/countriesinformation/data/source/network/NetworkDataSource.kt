package app.agk.countriesinformation.data.source.network

import app.agk.countriesinformation.data.source.network.responses.CountryInfoNetworkData

interface NetworkDataSource {
    suspend fun downloadCountryInfo(countryName : String): CountryInfoNetworkData
}