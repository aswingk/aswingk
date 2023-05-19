package app.agk.countriesinformation.countrylist.data

import app.agk.countriesinformation.countrylist.data.local.LocalResources
import app.agk.countriesinformation.models.ICountryListRepository

class CountryListRepository(
    private val localResources: LocalResources
) : ICountryListRepository {
    override suspend fun fetchCountryList(): List<String> {
        return localResources.fetchCountryList()
    }
}