package app.agk.countriesinformation.countrydetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.agk.countriesinformation.countrydetail.data.FakeCountryInfoRepository
import app.agk.countriesinformation.countrydetail.ui.DisplayCountryDetailsUIState
import app.agk.countriesinformation.countrylist.CountryDetailViewModel
import app.agk.countriesinformation.models.communication.asCountry
import app.agk.countriesinformation.utils.MainCoroutineRule
import app.agk.countriesinformation.utils.getCountryInfo
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch

@OptIn(ExperimentalCoroutinesApi::class)
internal class CountryViewModelTest{
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var countryDetailViewModel : CountryDetailViewModel

    // Use a fake repository to be injected into the viewmodel
    private val fakeCountryInfoRepository = FakeCountryInfoRepository()

    private val country = getCountryInfo().asCountry()

    /*
    @Test
    fun loadCountryUnavailable() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        countryDetailViewModel = CountryDetailViewModel(fakeCountryInfoRepository, country.name)

        var isLoading = true
        var region = ""

        val countDownLatch = CountDownLatch(2)
        val observer = Observer<DisplayCountryDetailsUIState>{
            region = it.region
            isLoading = it.isLoading
            countDownLatch.countDown()
        }

        countryDetailViewModel.countryDetailUILiveData.observeForever(observer)

        Assert.assertTrue(isLoading)

        // Execute pending coroutines actions

        // countDownLatch.await()
        Assert.assertTrue(!isLoading)

        assertEquals(region, country.region)

        countryDetailViewModel.countryDetailUILiveData.removeObserver(observer)

//         job.cancel()
    }
    */

/*
    @Test
    fun getActiveCountryFromRepositoryAndLoadIntoView() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        var uiState : DisplayCountryDetailsUIState? = null
        val job = launch {
            countryDetailViewModel.detailUIState.collect {
                uiState = it
            }
        }

        countryDetailViewModel.fetchCountryData(country.name)

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

        var uiState : DisplayCountryDetailsUIState? = null
        val job = launch {
            countryDetailViewModel.detailUIState.collect {
                uiState = it
            }
        }

        // Given a repository that throws errors
        countryRepository.setShouldThrowError(true)

        countryDetailViewModel.fetchCountryData("Dummy")

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
            countryDetailViewModel.detailUIState.collect {
                isLoading = it.isLoading
            }
        }

        // Then progress indicator is shown
        Assert.assertTrue(isLoading)

        countryDetailViewModel.fetchCountryData(country.name)

        // Execute pending coroutines actions
        advanceUntilIdle()

        // Then progress indicator is hidden
        Assert.assertFalse(isLoading)

        job.cancel()
    }*/
}