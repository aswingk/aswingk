package app.agk.countriesinformation.data.source.network

import app.agk.countriesinformation.data.source.network.responses.CountryInfoNetworkData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://restcountries.com/"
class RemoteDataSource : NetworkDataSource {
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

    suspend override fun downloadCountryInfo(countryName : String): CountryInfoNetworkData {
        return withContext(Dispatchers.IO){
            client.fetchCountryInfo(countryName).first()
        }
    }
}