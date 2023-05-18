package app.agk.countriesinformation.data

import app.agk.countriesinformation.data.source.local.LocalDataSource
import app.agk.countriesinformation.data.source.network.NetworkDataSource
import app.agk.countriesinformation.utils.CustomThrowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CountryRepository private constructor(
    private val localDataSource : LocalDataSource,
    private val remoteDataSource : NetworkDataSource
) : IRepository {

    override suspend fun fetchCountryInfo(countryName : String) : Flow<Resource> {
        return localDataSource.fetchCountryInfo(countryName).map {
            try {
//                Log.d(TAG, "fetchCountryInfo: $countryName ... ... ... $it")
                if (it == null) {
                    tryDownloadAndStoreCountryInfo(countryName)
                    Resource.Loading()
                } else
                    Resource.Success(it.asCountry())
            } catch (throwable: CustomThrowable) {
                Resource.Failure(throwable)
            }
        }
    }

    override suspend fun tryDownloadAndStoreCountryInfo(countryName: String) {
        withContext(Dispatchers.IO) {
            val response = async {
                remoteDataSource.downloadCountryInfo(countryName)
            }
            localDataSource.addOrUpdateCountryInfo(
                response
                    .await()
                    .asEntity()
            )
        }
    }

    companion object {
        private var instance: CountryRepository? = null
        fun getInstance(
            localDataSource: LocalDataSource,
            remoteDataSource: NetworkDataSource
        ): CountryRepository {
            return instance ?: synchronized(this) {
                instance ?: CountryRepository(localDataSource, remoteDataSource).also {
                    instance = it
                }
            }
        }
    }
}