package app.agk.countriesinformation.data.source.network

import app.agk.countriesinformation.data.source.network.responses.CountryInfoNetworkData
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryInfoService {
    @GET("/v3.1/name/{countryName}")
    suspend fun fetchCountryInfo(@Path("countryName") countryName : String) : List<CountryInfoNetworkData>
}