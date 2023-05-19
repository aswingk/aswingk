package app.agk.countriesinformation.countrydetail.data

import app.agk.countriesinformation.models.ICountryInfoRepository
import app.agk.countriesinformation.models.communication.Country
import app.agk.countriesinformation.models.communication.ResourceResultState
import app.agk.countriesinformation.utils.CustomThrowable
import app.agk.countriesinformation.utils.ErrorState
import kotlinx.coroutines.flow.*

class FakeCountryInfoRepository : ICountryInfoRepository {
    private var shouldThrowError = false

    fun setShouldThrowError(value: Boolean) {
        shouldThrowError = value
    }

    private val _countryList = MutableStateFlow(ArrayList<Country?>())
    val countryList: StateFlow<List<Country?>> = _countryList.asStateFlow()

    private val countryInfoObservable: Flow<List<Country?>> = countryList.map {
        it.toList()
    }

    override suspend fun fetchCountryInfo(countryName: String): Flow<ResourceResultState> {
        return countryInfoObservable.map {
            it.firstOrNull {
                it?.name == countryName
            }
        }.map {
            try {
                if (it == null) {
                    tryDownloadAndStoreCountryInfoOrThrow(countryName)
                    ResourceResultState.Loading()
                } else
                    ResourceResultState.Success(it)
            } catch (throwable: CustomThrowable) {
                ResourceResultState.Failure(throwable)
            }
        }
    }

    override suspend fun tryDownloadAndStoreCountryInfoOrThrow(countryName: String) {
        if (shouldThrowError) {
            throw CustomThrowable(ErrorState.LocalDataSourceError, Throwable("Test exception"))
        }
    }
}