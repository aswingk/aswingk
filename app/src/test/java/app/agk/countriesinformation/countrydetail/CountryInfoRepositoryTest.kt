package app.agk.countriesinformation.countrydetail

import app.agk.countriesinformation.countrydetail.data.CountryInfoRepository
import app.agk.countriesinformation.models.communication.ResourceResultState
import app.agk.countriesinformation.countrydetail.data.local.LocalDataSource
import app.agk.countriesinformation.utils.MainCoroutineRule
import app.agk.countriesinformation.countrydetail.data.FakeDao
import app.agk.countriesinformation.countrydetail.data.FakeNetworkDataSource
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

internal class CountryInfoRepositoryTest {

    // Test dependencies
    private lateinit var networkDataSource: FakeNetworkDataSource
    private lateinit var localDataSource: LocalDataSource

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    // Class under test
    private lateinit var countryInfoRepository: CountryInfoRepository

    private val country = getCountryInfoNetworkData()
    private val countryName = "USA"

    @Before
    fun setUp() {
        networkDataSource = FakeNetworkDataSource(listOf(country))
        localDataSource = LocalDataSource(FakeDao())
        // Get a reference to the class under test
        countryInfoRepository = CountryInfoRepository(
            localDataSource = localDataSource,
            remoteDataSource = networkDataSource,
        )
    }

    @Test
    fun getCountryDetailsFromNetwork() = runBlocking {

        var name: String? = null

        val job = async {
            countryInfoRepository.fetchCountryInfo(countryName).collect {
                if(it is ResourceResultState.Success){
                    assertEquals(it.value.name, name)
                }
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
            countryInfoRepository.fetchCountryInfo("countryName").collect {
                if (it is ResourceResultState.Success) {
                    name = it.value.name
                }
            }
        }

        // Execute pending coroutines actions
        advanceUntilIdle()

        assertTrue(name == "")

        job.cancel()
    }
}