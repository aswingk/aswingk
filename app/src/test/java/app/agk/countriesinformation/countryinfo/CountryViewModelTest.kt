package app.agk.countriesinformation.countryinfo

import app.agk.countriesinformation.R
import app.agk.countriesinformation.data.asCountry
import app.agk.countriesinformation.utils.FakeRepository
import app.agk.countriesinformation.utils.MainCoroutineRule
import app.agk.countriesinformation.utils.getCountryInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//@RunWith(PowerMockRunner::class)
//@PrepareForTest(Log::class)
internal class CountryViewModelTest{
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    lateinit var countryViewModel : CountryViewModel

    // Use a fake repository to be injected into the viewmodel

    private lateinit var countryRepository: FakeRepository
    private val country = getCountryInfo().asCountry()

    @Before
    fun setupViewModel() {
        // PowerMockito.mockStatic(Log::class.java)
        // Mockito.`when`(Log.d(anyString(), anyString())).then {  }
        countryRepository = FakeRepository()
        countryRepository.addCountry(country)

        countryViewModel = CountryViewModel(countryRepository)
    }

    @Test
    fun loadCountryUnavailable() = runTest {
        var isLoading = true
        var region = ""
        val job = launch {
            countryViewModel.uiState.collect {
                region = it.region
                isLoading = it.isLoading
            }
        }

        Assert.assertTrue(isLoading)
        Assert.assertTrue(region == "")
        countryViewModel.loadCountryData("Dummy")

        // Execute pending coroutines actions
        advanceUntilIdle()

        Assert.assertTrue(region == "")

        countryViewModel.loadCountryData(country.name)
        // Execute pending coroutines actions
        advanceUntilIdle()

        Assert.assertTrue(region == country.region)

        job.cancel()
    }

    @Test
    fun getActiveCountryFromRepositoryAndLoadIntoView() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        var uiState : DisplayCountryDetailsUiState? = null
        val job = launch {
            countryViewModel.uiState.collect {
                uiState = it
            }
        }

        countryViewModel.loadCountryData(country.name)

        // Execute pending coroutines actions
        advanceUntilIdle()

        // Then verify that the view was notified
        uiState?.apply {
            Assert.assertTrue(region == country.region)
            Assert.assertTrue(subregion == country.subregion)
            Assert.assertTrue(population == "${country.population}")
            Assert.assertTrue(area == "${country.area}")
        }

        job.cancel()
    }

    @Test
    fun countryViewModel_repositoryError() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        var uiState : DisplayCountryDetailsUiState? = null
        val job = launch {
            countryViewModel.uiState.collect {
                uiState = it
            }
        }

        // Given a repository that throws errors
        countryRepository.setShouldThrowError(true)

        countryViewModel.loadCountryData("Dummy")

        // Execute pending coroutines actions
        advanceUntilIdle()

        assert(uiState?.isLoading == false)
        assert(uiState?.userMessage == R.string.no_data)

        job.cancel()
    }

    @Test
    fun loadCountry_loading() = runTest {
        // Set Main dispatcher to not run coroutines eagerly, for just this one test
        Dispatchers.setMain(StandardTestDispatcher())

        var isLoading = true
        val job = launch {
            countryViewModel.uiState.collect {
                isLoading = it.isLoading
            }
        }

        // Then progress indicator is shown
        Assert.assertTrue(isLoading)

        countryViewModel.loadCountryData(country.name)

        // Execute pending coroutines actions
        advanceUntilIdle()

        // Then progress indicator is hidden
        Assert.assertFalse(isLoading)

        job.cancel()
    }
}