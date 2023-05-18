package app.agk.countriesinformation.utils.data

import androidx.annotation.VisibleForTesting
import app.agk.countriesinformation.data.Country
import app.agk.countriesinformation.data.IRepository
import app.agk.countriesinformation.data.Resource
import app.agk.countriesinformation.utils.CustomThrowable
import app.agk.countriesinformation.utils.ErrorState
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

    override suspend fun fetchCountryInfo(countryName: String): Flow<Resource> {
        return countryInfoObservable.map {
            it.firstOrNull {
                it?.name == countryName
            }
        }.map {
            try {
                if (it == null) {
                    tryDownloadAndStoreCountryInfo(countryName)
                    Resource.Loading()
                } else
                    Resource.Success(it)
            } catch (throwable: CustomThrowable) {
                Resource.Failure(throwable)
            }
        }
    }

    override suspend fun tryDownloadAndStoreCountryInfo(countryName: String) {
        if (shouldThrowError) {
            throw CustomThrowable(ErrorState.DatabaseError, Throwable("Test exception"))
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