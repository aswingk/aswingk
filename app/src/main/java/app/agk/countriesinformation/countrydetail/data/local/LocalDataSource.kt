package app.agk.countriesinformation.countrydetail.data.local

import app.agk.countriesinformation.utils.CustomThrowable
import app.agk.countriesinformation.utils.ErrorState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalDataSource(private val countryDao: CountryDao) {

    suspend fun <Api> invokeAsync(invokeFn: suspend () -> Api): Api {
        return withContext(Dispatchers.IO) {
            try {
                invokeFn()
            } catch (throwable: Throwable) {
                throw CustomThrowable(ErrorState.LocalDataSourceError, throwable)
            }
        }
    }

    suspend fun fetchCountryInfo(countryName: String): Flow<CountryInfo?> = invokeAsync {
        countryDao.getCountryInfo(countryName)
    }

    suspend fun addOrUpdateCountryInfo(countryInfo: CountryInfo) = invokeAsync {
        countryDao.upsert(countryInfo)
    }
}