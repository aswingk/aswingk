package app.agk.countriesinformation.countrydetail.data

import app.agk.countriesinformation.countrydetail.data.local.CountryDao
import app.agk.countriesinformation.countrydetail.data.local.CountryInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FakeDao : CountryDao {
    val list = ArrayList<CountryInfo>()
    override suspend fun upsert(countryInfo: CountryInfo) {
        // mocking to let it just add and no update
        list.add(countryInfo)
    }

    override fun getCountryInfo(name: String): Flow<CountryInfo?> {
        return flowOf(list).map {
            it.firstOrNull {
                it.name == name
            }
        }
    }
}