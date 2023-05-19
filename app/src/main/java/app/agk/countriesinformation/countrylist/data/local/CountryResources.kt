package app.agk.countriesinformation.countrylist.data.local

import android.content.Context

interface ICountryResources {
    suspend fun fetchCountryList(resourceId : Int) : List<String>
}

class CountryResources(val context: Context) : ICountryResources {
    suspend override fun fetchCountryList(resourceId: Int): List<String> {
        return context.resources.getStringArray(resourceId).toList()
    }
}