package app.agk.countriesinformation.data

import app.agk.countriesinformation.data.source.local.LocalDataSource
import app.agk.countriesinformation.data.source.network.NetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CountryRepository private constructor(
    private val localDataSource : LocalDataSource,
    private val remoteDataSource : NetworkDataSource
) : IRepository {

    override fun fetchCountryInfo(countryName: String): Flow<Country?> {
        return localDataSource.fetchCountryInfo(countryName)
            .map {
                if (it == null)
                    tryDownloadAndStoreCountryInfo(countryName)

                it?.asCountry()
            }
    }

    suspend override fun tryDownloadAndStoreCountryInfo(countryName: String) {
        withContext(Dispatchers.IO) {
            val response = remoteDataSource.downloadCountryInfo(countryName)
            localDataSource
                .addOrUpdateCountryInfo(
                    response.asEntity()
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