package app.agk.countriesinformation.data.source.local

import app.agk.countriesinformation.utils.CustomThrowable
import app.agk.countriesinformation.utils.ErrorState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalDataSource(private val countryDao: CountryDao) {

    suspend fun <Api> invokeDatabase(method: suspend () -> Api): Api {
        return withContext(Dispatchers.IO) {
            try {
                method()
            } catch (throwable: Throwable) {
                throw CustomThrowable(ErrorState.DatabaseError, throwable)
            }
        }
    }

    suspend fun fetchCountryInfo(countryName: String): Flow<CountryInfo?> = invokeDatabase {
        countryDao.getCountryInfo(countryName)
    }

    suspend fun addOrUpdateCountryInfo(countryInfo: CountryInfo) = invokeDatabase {
        countryDao.upsert(countryInfo)
    }
}