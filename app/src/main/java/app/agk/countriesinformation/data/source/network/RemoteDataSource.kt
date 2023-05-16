package app.agk.countriesinformation.data.source.network

import android.util.Log
import app.agk.countriesinformation.TAG
import app.agk.countriesinformation.data.source.local.CountryInfo
import app.agk.countriesinformation.data.source.network.responses.NetworkResult
import app.agk.countriesinformation.data.source.network.responses.asEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://restcountries.com/"
class RemoteDataSource {
    private val retrofit : Retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient()
                    .newBuilder()
                    .addInterceptor(
                        HttpLoggingInterceptor().also {
                            it.setLevel(HttpLoggingInterceptor.Level.BASIC)
                        }
                    )
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val client : CountryInfoService by lazy {
        retrofit.create(CountryInfoService::class.java)
    }

    suspend fun downloadCountryInfo(countryName : String): NetworkResult<CountryInfo> {
        return withContext(Dispatchers.IO){
            try {
                client.fetchCountryInfo(countryName).run {
                    with(get(0)){
                        Log.d(TAG, "downloadCountryInfo: name: $name, mapInfoUrl: $maps, capital: $capital")
                        val countryInfo = asEntity()
                        NetworkResult.Success(countryInfo)
                    }
                }
            } catch (throwable: Throwable) {
                Log.d(TAG, "downloadCountryInfo: throwable: ${throwable.message}")
                when (throwable) {
                    is HttpException -> NetworkResult.Failure(
                        true,
                        throwable.code(),
                        throwable.message()
                    )
                    else -> NetworkResult.Failure(false)
                }
            }
        }
    }
}