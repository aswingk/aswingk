package app.agk.countriesinformation.countrylist

import app.agk.countriesinformation.countrylist.data.CountryListRepository
import app.agk.countriesinformation.countrylist.data.local.ICountryResources
import app.agk.countriesinformation.countrylist.data.local.LocalResources
import app.agk.countriesinformation.countrylist.data.FakeResources
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class CountryListRepositoryTest {
    private lateinit var countryResources : ICountryResources
    // Class under test
    private lateinit var countryListRepository: CountryListRepository

    @Before
    fun setUp() {
        countryResources = FakeResources()
        val localResources = LocalResources(countryResources)
        // Get a reference to the class under test
        countryListRepository = CountryListRepository(
            localResources = localResources
        )
    }

    @Test
    fun getCountryList() = runBlocking {
        if(countryResources is FakeResources){
            assertEquals((countryResources as FakeResources).list.size, countryListRepository.fetchCountryList().size)
            assertEquals((countryResources as FakeResources).list, countryListRepository.fetchCountryList())
        }
    }
}