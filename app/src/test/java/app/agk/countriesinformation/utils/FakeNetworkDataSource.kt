package app.agk.countriesinformation.utils

import app.agk.countriesinformation.data.source.network.NetworkDataSource
import app.agk.countriesinformation.data.source.network.responses.CountryInfoNetworkData

class FakeNetworkDataSource(var countriesList : List<CountryInfoNetworkData>) : NetworkDataSource {
    override suspend fun downloadCountryInfo(countryName: String): CountryInfoNetworkData {
        return countriesList.filter { country: CountryInfoNetworkData ->
            country.name.common == countryName
        }.first()
    }
}