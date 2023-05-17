package app.agk.countriesinformation.data

import app.agk.countriesinformation.data.source.local.LocalDataSource
import app.agk.countriesinformation.data.source.network.NetworkDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class CountryRepository private constructor(
    private val localDataSource : LocalDataSource,
    private val remoteDataSource : NetworkDataSource
) : IRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetchCountryInfo(countryName: String): Flow<Country?> {
        return localDataSource.fetchCountryInfo(countryName)
            .mapLatest {
                if (it == null)
                    tryDownloadAndStoreCountryInfo(countryName)

                it?.asCountry()
            }
    }

    suspend override fun tryDownloadAndStoreCountryInfo(countryName : String) {
        val response = remoteDataSource.downloadCountryInfo(countryName)
        localDataSource
            .addOrUpdateCountryInfo(
                response.asEntity()
            )
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