package app.agk.countriesinformation.countrydetail.data.network

import app.agk.countriesinformation.countrydetail.data.network.responses.CountryInfoNetworkData
import app.agk.countriesinformation.utils.CustomThrowable
import app.agk.countriesinformation.utils.ErrorState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://restcountries.com/"
class RemoteDataSource : NetworkDataSource {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient().newBuilder()
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

    private val client: CountryServiceApi by lazy {
        retrofit.create(CountryServiceApi::class.java)
    }

    override suspend fun downloadCountryInfo(countryName: String): CountryInfoNetworkData {
        return withContext(Dispatchers.IO) {
            try {
                client.fetchCountryInfo(countryName).first()
            } catch (throwable: Throwable) {
                throw when (throwable) {
                    is HttpException -> CustomThrowable(ErrorState.HttpError, throwable)
                    else -> CustomThrowable(ErrorState.NoNetworkError, throwable)
                }
            }
        }
    }
}