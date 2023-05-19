package app.agk.countriesinformation.countrydetail.data.network

import app.agk.countriesinformation.countrydetail.data.network.responses.CountryInfoNetworkData

interface NetworkDataSource {
    suspend fun downloadCountryInfo(countryName : String): CountryInfoNetworkData
}