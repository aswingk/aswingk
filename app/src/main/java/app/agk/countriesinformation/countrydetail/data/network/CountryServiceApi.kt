package app.agk.countriesinformation.countrydetail.data.network

import app.agk.countriesinformation.countrydetail.data.network.responses.CountryInfoNetworkData
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryServiceApi {
    @GET("/v3.1/name/{countryName}")
    suspend fun fetchCountryInfo(@Path("countryName") countryName : String) : List<CountryInfoNetworkData>
}