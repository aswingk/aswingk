package app.agk.countriesinformation.data

import app.agk.countriesinformation.data.source.local.LocalDataSource
import app.agk.countriesinformation.utils.FakeDao
import app.agk.countriesinformation.utils.FakeNetworkDataSource
import app.agk.countriesinformation.utils.MainCoroutineRule
import app.agk.countriesinformation.utils.getCountryInfoNetworkData
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch

internal class CountryRepositoryTest {

    // Test dependencies
    private lateinit var networkDataSource: FakeNetworkDataSource
    private lateinit var localDataSource: LocalDataSource

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

    @ExperimentalCoroutinesApi
    @Test
    fun getCountryDetailsFromNetwork() = runBlocking {

        var name : String? = null
        var result: String? = ""

        val countDownLatch = CountDownLatch(2)

        val job = launch {
            countryRepository.fetchCountryInfo(countryName).collect {
                assertEquals(it?.name, name)
                name = countryName
                countDownLatch.countDown()
            }
        }

        // Execute pending coroutines actions
//         advanceUntilIdle()

        countDownLatch.await()

        job.cancel()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCountryWhenNoNetwork() = runTest {
        var name = ""
        val job = launch {
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