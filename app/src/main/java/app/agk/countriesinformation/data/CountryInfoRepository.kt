package app.agk.countriesinformation.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import app.agk.countriesinformation.TAG
import app.agk.countriesinformation.data.source.local.LocalDataSource
import app.agk.countriesinformation.data.source.local.CountryInfo
import app.agk.countriesinformation.data.source.network.RemoteDataSource
import app.agk.countriesinformation.data.source.network.responses.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class CountryInfoRepository private constructor(
    private val localDataSource : LocalDataSource,
    private val remoteDataSource : RemoteDataSource
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchCountryInfo(countryName: String): LiveData<CountryInfo?> {
        return localDataSource.fetchCountryInfo(countryName)
            .mapLatest {
                Log.d(TAG, "fetchCountryInfo: $it, $countryName")
                if(it == null) tryDownloadAndStoreCountryInfo(countryName)

                it
            }
            .asLiveData()
    }

    suspend fun tryDownloadAndStoreCountryInfo(countryName : String) {
        withContext(Dispatchers.IO) {
            if(countryName.isBlank()) return@withContext

            Log.d(TAG, "tryDownloadAndStoreCountryInfo: got the flow with $countryName")

            val response = remoteDataSource.downloadCountryInfo(countryName)
            if (response is NetworkResult.Success) {
                localDataSource.addOrUpdateCountryInfo(response.value)
            }
        }
    }

    companion object {
        private var instance: CountryInfoRepository? = null
        fun getInstance(
            localDataSource: LocalDataSource,
            remoteDataSource: RemoteDataSource
        ): CountryInfoRepository {
            return instance ?: synchronized(this) {
                instance ?: CountryInfoRepository(localDataSource, remoteDataSource).also {
                    instance = it
                }
            }
        }
    }
}