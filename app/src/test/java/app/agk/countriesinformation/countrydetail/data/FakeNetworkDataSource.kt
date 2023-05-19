package app.agk.countriesinformation.countrydetail.data

import app.agk.countriesinformation.countrydetail.data.network.NetworkDataSource
import app.agk.countriesinformation.countrydetail.data.network.responses.CountryInfoNetworkData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FakeNetworkDataSource(var countriesList : List<CountryInfoNetworkData>) : NetworkDataSource {
    override suspend fun downloadCountryInfo(countryName: String): CountryInfoNetworkData =
        withContext(Dispatchers.IO) {
            countriesList.first()
        }
}