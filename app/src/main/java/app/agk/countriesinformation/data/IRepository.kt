package app.agk.countriesinformation.data

import kotlinx.coroutines.flow.Flow

/**
 * Interface to the data layer.
 */
interface IRepository {

    suspend fun fetchCountryInfo(countryName: String): Flow<Resource>

    suspend fun tryDownloadAndStoreCountryInfo(countryName : String)

}