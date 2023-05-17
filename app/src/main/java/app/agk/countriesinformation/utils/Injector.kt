package app.agk.countriesinformation.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.agk.countriesinformation.countryinfo.CountryViewModel
import app.agk.countriesinformation.data.CountryRepository
import app.agk.countriesinformation.data.source.local.CountryDatabase
import app.agk.countriesinformation.data.source.local.LocalDataSource
import app.agk.countriesinformation.data.source.network.RemoteDataSource

interface ViewModelFactoryProvider {
    fun provideCountriesViewModelFactory(context : Context) : CountriesViewModelFactory
}

val Injector : ViewModelFactoryProvider
    get() = currentInjector

@Volatile private var currentInjector = DefaultViewModelProvider()

class DefaultViewModelProvider : ViewModelFactoryProvider {

    private fun getCountriesRepository(context: Context): CountryRepository {
        return CountryRepository.getInstance(
            localDao(context),
            remoteDataSource())
    }

    private fun remoteDataSource() = RemoteDataSource()

    private fun localDao(context: Context): LocalDataSource {
        return LocalDataSource(
            CountryDatabase.getInstance(context).getCountryDao()
        )
    }

    override fun provideCountriesViewModelFactory(context: Context): CountriesViewModelFactory {
        return CountriesViewModelFactory(
            getCountriesRepository(context)
        )
    }
}

class CountriesViewModelFactory(private val repository : CountryRepository) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CountryViewModel(repository) as T
}