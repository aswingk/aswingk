package app.agk.countriesinformation.countrylist.data.local

import app.agk.countriesinformation.R
import app.agk.countriesinformation.utils.CustomThrowable
import app.agk.countriesinformation.utils.ErrorState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalResources(private val resources: ICountryResources) {
    suspend fun fetchCountryList() : List<String> {
        return withContext(Dispatchers.IO) {
            try {
                resources.fetchCountryList(R.array.countries_list)
            } catch (throwable: Throwable) {
                throw CustomThrowable(ErrorState.LocalDataSourceError, throwable)
            }
        }
    }
}