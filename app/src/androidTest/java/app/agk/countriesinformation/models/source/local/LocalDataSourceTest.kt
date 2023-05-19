package app.agk.countriesinformation.models.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.agk.countriesinformation.countrydetail.data.local.CountryDatabase
import app.agk.countriesinformation.countrydetail.data.local.LocalDataSource
import app.agk.countriesinformation.utils.getCountryInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
internal class LocalDataSourceTest {
    lateinit var localDataSource: LocalDataSource
    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val database = Room
            .inMemoryDatabaseBuilder(context, CountryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        val dao = database.getCountryDao()
        localDataSource = LocalDataSource(dao)
    }

    @Test
    fun fetchCountryInfo() = runBlocking {
        val actual = getCountryInfo()
        with(localDataSource){
            invokeAsync {
                addOrUpdateCountryInfo(actual)
            }
        }
        Assert.assertTrue(actual == localDataSource.fetchCountryInfo(actual.name).first())
    }

    @Test
    fun fetchCountryInfoWhenNoData() = runBlocking {
        with(localDataSource){
            Assert.assertNull(
                invokeAsync {
                    fetchCountryInfo("name").firstOrNull()
                })
        }
    }
}