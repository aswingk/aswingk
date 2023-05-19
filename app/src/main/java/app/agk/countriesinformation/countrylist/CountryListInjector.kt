package app.agk.countriesinformation.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.agk.countriesinformation.countrylist.*
import app.agk.countriesinformation.countrylist.data.CountryListRepository
import app.agk.countriesinformation.countrylist.data.local.CountryResources
import app.agk.countriesinformation.countrylist.data.local.LocalResources

interface CountryListViewModelFactoryProvider {
    fun provideCountryListViewModelFactory(context : Context) : CountryListViewModelFactory
}

val CountryListInjector : CountryListViewModelFactoryProvider
    get() = DefaultCountryListViewModelProvider

object DefaultCountryListViewModelProvider : CountryListViewModelFactoryProvider {

    private fun localResources(context: Context) : LocalResources {
        return LocalResources(
            CountryResources(context)
        )
    }

    private fun getCountryListRepository(context: Context) : CountryListRepository {
        return CountryListRepository(
            localResources(context)
        )
    }

    override fun provideCountryListViewModelFactory(context: Context): CountryListViewModelFactory {
        return CountryListViewModelFactory(getCountryListRepository(context))
    }
}

class CountryListViewModelFactory(
    private val repository : CountryListRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CountryViewModel(repository) as T
}