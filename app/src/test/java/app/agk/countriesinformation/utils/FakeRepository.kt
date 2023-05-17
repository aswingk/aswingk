package app.agk.countriesinformation.utils

import androidx.annotation.VisibleForTesting
import app.agk.countriesinformation.data.Country
import app.agk.countriesinformation.data.IRepository
import kotlinx.coroutines.flow.*

class FakeRepository : IRepository {
    private var shouldThrowError = false

    private val _countryList = MutableStateFlow(ArrayList<Country?>())
    val countryList: StateFlow<List<Country?>> = _countryList.asStateFlow()

    private val countryInfoObservable: Flow<List<Country?>> = countryList.map {
        it.toList()
    }

    fun setShouldThrowError(value: Boolean) {
        shouldThrowError = value
    }

    override fun fetchCountryInfo(countryName: String): Flow<Country?> {
        return countryInfoObservable.map {
            it.firstOrNull {
                it?.name == countryName
            }
        }.map {
            if(it == null) tryDownloadAndStoreCountryInfo(countryName)
            it
        }
    }

    override suspend fun tryDownloadAndStoreCountryInfo(countryName: String) {
        if (shouldThrowError) {
            throw Exception("Test exception")
        }
    }

    @VisibleForTesting
    fun addCountry(vararg country : Country?) {
        _countryList.update { oldCountryList ->
            val newCountryList = ArrayList(oldCountryList)
            for (item in country) {
                newCountryList.add(item)
            }
            newCountryList
        }
    }
}