package app.agk.countriesinformation.countrydetail.data

import app.agk.countriesinformation.models.communication.ResourceResultState
import app.agk.countriesinformation.models.communication.asCountry
import app.agk.countriesinformation.models.communication.asEntityWithName
import app.agk.countriesinformation.countrydetail.data.local.LocalDataSource
import app.agk.countriesinformation.countrydetail.data.network.NetworkDataSource
import app.agk.countriesinformation.models.ICountryInfoRepository
import app.agk.countriesinformation.utils.CustomThrowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CountryInfoRepository(
    private val localDataSource : LocalDataSource,
    private val remoteDataSource : NetworkDataSource
) : ICountryInfoRepository {

    override suspend fun fetchCountryInfo(countryName : String) : Flow<ResourceResultState> {
        return localDataSource.fetchCountryInfo(countryName).map {
            try {
//                Log.d(TAG, "fetchCountryInfo: $countryName ... ... ... $it")
                if (it == null) {
                    tryDownloadAndStoreCountryInfoOrThrow(countryName)
                    ResourceResultState.Loading()
                } else
                    ResourceResultState.Success(it.asCountry())
            } catch (throwable: CustomThrowable) {
                ResourceResultState.Failure(throwable)
            }
        }
    }

    override suspend fun tryDownloadAndStoreCountryInfoOrThrow(countryName: String) {
        withContext(Dispatchers.IO) {
//            val response = async {
//
//            }
            val response = remoteDataSource.downloadCountryInfo(countryName)
            localDataSource.addOrUpdateCountryInfo(
                response
                //     .await()
                    .asEntityWithName(countryName)
            )
        }
    }
}