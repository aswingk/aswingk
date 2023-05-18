package app.agk.countriesinformation.ui

import androidx.annotation.UiThread
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import app.agk.countriesinformation.countryinfo.CountryAdapter
import app.agk.countriesinformation.countryinfo.CountryItemClickListener
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@UiThread
internal class CountryAdapterTest {

    lateinit var adapter: CountryAdapter
    val testData = listOf(
        "USA",
        "India",
        "Canada"
    )

    @Before
    fun setUp() {
        adapter = CountryAdapter(object : CountryItemClickListener {
            override fun navigateToDetailView(countryName: String) {
                // Ignore for now
            }
        })
        adapter.submitList(testData)
    }

    @Test
    fun getItemCountTest() = runBlocking {
        assertEquals(3, adapter.itemCount)
    }

    @Test
    fun onBindViewHolderTest() {
        val parent = RecyclerView(ApplicationProvider.getApplicationContext()).also {
            it.layoutManager = LinearLayoutManager(it.context)
        }
        val viewHolder = adapter.onCreateViewHolder(parent, 0)
        adapter.onBindViewHolder(viewHolder, 1)
        assertEquals("India", viewHolder.countryName)
    }
}