package app.agk.countriesinformation.countryinfo

import app.agk.countriesinformation.utils.MainCoroutineRule
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CountryListViewModelTest {

    lateinit var countryListViewModel : CountryListViewModel
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup(){
        countryListViewModel = CountryListViewModel()
    }

    @Test
    fun loadCountryList() = runBlockingTest{

        val actualList = listOf("USA", "Antarctica")
        var newList = listOf<String>()
        val job = launch {
            countryListViewModel.fetchList {
                actualList
            }.collect {
                newList = it
            }
        }

        TestCase.assertEquals(newList, actualList)

        job.cancel()
    }
}