package app.agk.countriesinformation.countrylist.data

import app.agk.countriesinformation.countrylist.data.local.ICountryResources

class FakeResources : ICountryResources {
    val list = listOf("USA", "Canada", "India")
    override suspend fun fetchCountryList(resourceId: Int): List<String> = list
}