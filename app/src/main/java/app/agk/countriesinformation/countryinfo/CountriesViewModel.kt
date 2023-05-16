package app.agk.countriesinformation.countryinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.agk.countriesinformation.data.CountryInfoRepository
import app.agk.countriesinformation.data.source.local.CountryInfo

class CountriesViewModel internal constructor(
    private val repository: CountryInfoRepository
) : ViewModel() {
    fun countryInfo(countryName: String) : LiveData<CountryInfo?> =
        repository.fetchCountryInfo(countryName)
}