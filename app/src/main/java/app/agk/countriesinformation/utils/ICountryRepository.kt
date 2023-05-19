package app.agk.countriesinformation.models

import app.agk.countriesinformation.models.communication.ResourceResultState
import kotlinx.coroutines.flow.Flow

/**
 * Interface to the data layer.
 */
interface ICountryInfoRepository {

    suspend fun fetchCountryInfo(countryName: String): Flow<ResourceResultState>

    suspend fun tryDownloadAndStoreCountryInfoOrThrow(countryName : String)

}

interface ICountryListRepository {

    suspend fun fetchCountryList() : List<String>

}