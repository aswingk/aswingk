package app.agk.countriesinformation.viewmodels

import app.agk.countriesinformation.countryinfo.CountryListViewModel
import app.agk.countriesinformation.utils.MainCoroutineRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CountryListViewModelTest {

    lateinit var countryListViewModel : CountryListViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup(){
        countryListViewModel = CountryListViewModel()
    }

    @Test
    fun loadCountryList() = runBlocking {

        val actualList = listOf("USA", "Antarctica")
        var newList = listOf<String>()
        val job = async {
            countryListViewModel.fetchList {
                actualList
            }.take(1)
                .collect {
                    newList = it
                }
        }

        job.await()

        assertEquals(newList, actualList)

        job.cancel()
    }
}