package app.agk.countriesinformation.data

import app.agk.countriesinformation.data.source.local.LocalDataSource
import app.agk.countriesinformation.utils.MainCoroutineRule
import app.agk.countriesinformation.utils.data.FakeDao
import app.agk.countriesinformation.utils.data.FakeNetworkDataSource
import app.agk.countriesinformation.utils.getCountryInfoNetworkData
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class CountryRepositoryTest {

    // Test dependencies
    private lateinit var networkDataSource: FakeNetworkDataSource
    private lateinit var localDataSource: LocalDataSource

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    // Class under test
    private lateinit var countryRepository: CountryRepository

    private val country = getCountryInfoNetworkData()
    private val countryName = country.name.common

    @Before
    fun setUp() {
        networkDataSource = FakeNetworkDataSource(listOf(country))
        localDataSource = LocalDataSource(FakeDao())
        // Get a reference to the class under test
        countryRepository = CountryRepository.getInstance(
            localDataSource = localDataSource,
            remoteDataSource = networkDataSource,
        )
    }

    @Test
    fun getCountryDetailsFromNetwork() = runBlocking {

        var name: String? = null

        val job = async {
            countryRepository.fetchCountryInfo(countryName).collect {
                assertEquals(it?.name, name)
                name = countryName
            }
        }

        assertNotSame(countryName, name)

        // Execute pending coroutines actions
        job.await()

        assertSame(countryName, name)

        job.cancel()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCountryWhenNoNetwork() = runTest {
        var name = ""
        val job = async {
            countryRepository.fetchCountryInfo("countryName").collect {
                it?.let {
                    name = it.name
                }
            }
        }

        // Execute pending coroutines actions
        advanceUntilIdle()

        assertTrue(name == "")

        job.cancel()
    }
}