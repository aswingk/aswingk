package app.agk.countriesinformation.utils.data

import app.agk.countriesinformation.data.source.network.NetworkDataSource
import app.agk.countriesinformation.data.source.network.responses.CountryInfoNetworkData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FakeNetworkDataSource(var countriesList : List<CountryInfoNetworkData>) : NetworkDataSource {
    override suspend fun downloadCountryInfo(countryName: String): CountryInfoNetworkData = withContext(Dispatchers.IO) {
        countriesList.filter { country: CountryInfoNetworkData ->
            country.name.common == countryName
        }.first()
    }
}