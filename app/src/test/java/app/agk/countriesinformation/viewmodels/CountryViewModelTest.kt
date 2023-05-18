package app.agk.countriesinformation.viewmodels

import app.agk.countriesinformation.R
import app.agk.countriesinformation.countryinfo.CountryViewModel
import app.agk.countriesinformation.countryinfo.DisplayCountryDetailsUiState
import app.agk.countriesinformation.data.asCountry
import app.agk.countriesinformation.utils.MainCoroutineRule
import app.agk.countriesinformation.utils.data.FakeRepository
import app.agk.countriesinformation.utils.getCountryInfo
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
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
@OptIn(ExperimentalCoroutinesApi::class)
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
        Dispatchers.setMain(StandardTestDispatcher())

        var isLoading = true
        var region = ""
        val job = launch {
            countryViewModel.detailUIState
                .map {
                    region = it.region
                    isLoading = it.isLoading
                }
                .collect {}
        }

        Assert.assertTrue(isLoading)
        assertEquals("", region)
        countryViewModel.fetchCountryData("Dummy")

        // Execute pending coroutines actions
        advanceUntilIdle()

        assertEquals("", region)

        countryViewModel.fetchCountryData(country.name)

        // Execute pending coroutines actions
        advanceUntilIdle()

        assertEquals(region, country.region)

        job.cancel()
    }

    @Test
    fun getActiveCountryFromRepositoryAndLoadIntoView() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        var uiState : DisplayCountryDetailsUiState? = null
        val job = launch {
            countryViewModel.detailUIState.collect {
                uiState = it
            }
        }

        countryViewModel.fetchCountryData(country.name)

        // Execute pending coroutines actions
        advanceUntilIdle()

        // Then verify that the view was notified
        with(uiState) {
            assertEquals(this?.region, country.region)
            assertEquals(this?.subregion, country.subregion)
            assertEquals(this?.population, "${country.population}")
            assertEquals(this?.area, "${country.area}")
        }

        job.cancel()
    }

    @Test
    fun countryViewModel_repositoryError() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        var uiState : DisplayCountryDetailsUiState? = null
        val job = launch {
            countryViewModel.detailUIState.collect {
                uiState = it
            }
        }

        // Given a repository that throws errors
        countryRepository.setShouldThrowError(true)

        countryViewModel.fetchCountryData("Dummy")

        // Execute pending coroutines actions
        advanceUntilIdle()

        assert(uiState?.isLoading == false)
        assertEquals(uiState?.userMessage, R.string.unconfined_error)

        job.cancel()
    }

    @Test
    fun loadCountry_loading() = runTest {
        // Set Main dispatcher to not run coroutines eagerly, for just this one test
        Dispatchers.setMain(StandardTestDispatcher())

        var isLoading = true
        val job = launch {
            countryViewModel.detailUIState.collect {
                isLoading = it.isLoading
            }
        }

        // Then progress indicator is shown
        Assert.assertTrue(isLoading)

        countryViewModel.fetchCountryData(country.name)

        // Execute pending coroutines actions
        advanceUntilIdle()

        // Then progress indicator is hidden
        Assert.assertFalse(isLoading)

        job.cancel()
    }
}