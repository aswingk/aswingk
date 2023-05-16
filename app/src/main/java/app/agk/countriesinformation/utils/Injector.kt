package app.agk.countriesinformation.utils

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.agk.countriesinformation.countryinfo.CountriesViewModel
import app.agk.countriesinformation.data.source.local.CountryDatabase
import app.agk.countriesinformation.data.source.network.RemoteDataSource
import app.agk.countriesinformation.data.CountryInfoRepository
import app.agk.countriesinformation.data.source.local.LocalDataSource

interface ViewModelFactoryProvider {
    fun provideCountriesViewModelFactory(context : Context) : CountriesViewModelFactory
}

val Injector : ViewModelFactoryProvider
    get() = currentInjector

@Volatile private var currentInjector = DefaultViewModelProvider()

class DefaultViewModelProvider : ViewModelFactoryProvider {

    private fun getCountriesRepository(context: Context): CountryInfoRepository {
        return CountryInfoRepository.getInstance(
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

class CountriesViewModelFactory(private val repository : CountryInfoRepository) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CountriesViewModel(repository) as T
}

private object Lock

@VisibleForTesting
private fun setInjectorForTesting(injector: DefaultViewModelProvider?) {
    synchronized(Lock) {
        currentInjector = injector ?: DefaultViewModelProvider()
    }
}

@VisibleForTesting
private fun resetInjector() =
    setInjectorForTesting(null)