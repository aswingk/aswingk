package app.agk.countriesinformation.data

import kotlinx.coroutines.flow.Flow

/**
 * Interface to the data layer.
 */
interface IRepository {

    suspend fun fetchCountryInfo(countryName: String): Flow<Country?>

    suspend fun tryDownloadAndStoreCountryInfo(countryName : String)

}