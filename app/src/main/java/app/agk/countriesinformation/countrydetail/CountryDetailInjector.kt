package app.agk.countriesinformation.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.agk.countriesinformation.countrydetail.data.CountryInfoRepository
import app.agk.countriesinformation.countrydetail.data.local.CountryDatabase
import app.agk.countriesinformation.countrydetail.data.local.LocalDataSource
import app.agk.countriesinformation.countrydetail.data.network.RemoteDataSource
import app.agk.countriesinformation.countrylist.*

interface CountryDetailViewModelFactoryProvider {
    fun provideCountriesViewModelFactory(context : Context, countryName: String) : CountriesViewModelFactory
}

val CountryDetailInjector : CountryDetailViewModelFactoryProvider
    get() = DefaultCountryDetailViewModelProvider

object DefaultCountryDetailViewModelProvider : CountryDetailViewModelFactoryProvider {

    private fun getCountriesRepository(context: Context): CountryInfoRepository {
        return CountryInfoRepository(
            localDao(context),
            remoteDataSource())
    }

    private fun remoteDataSource() = RemoteDataSource()

    private fun localDao(context: Context): LocalDataSource {
        return LocalDataSource(
            CountryDatabase.getInstance(context).getCountryDao()
        )
    }

    override fun provideCountriesViewModelFactory(context: Context, countryName: String): CountriesViewModelFactory {
        return CountriesViewModelFactory(
            getCountriesRepository(context), countryName
        )
    }
}

class CountriesViewModelFactory(private val repository : CountryInfoRepository,
                                private val countryName: String)
    : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CountryDetailViewModel(repository, countryName) as T
}