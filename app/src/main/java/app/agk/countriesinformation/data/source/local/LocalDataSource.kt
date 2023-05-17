package app.agk.countriesinformation.data.source.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalDataSource(private val countryDao: CountryDao) {

    fun fetchCountryInfo(countryName: String): Flow<CountryInfo?> {
        return countryDao.getCountryInfo(countryName)
    }

    suspend fun addOrUpdateCountryInfo(countryInfo: CountryInfo) {
        withContext(Dispatchers.IO) {
            countryDao.upsert(countryInfo)
        }
    }
}