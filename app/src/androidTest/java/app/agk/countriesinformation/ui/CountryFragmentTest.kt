package app.agk.countriesinformation.ui

import android.content.Context
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import app.agk.countriesinformation.MainActivity
import app.agk.countriesinformation.R
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@UiThread
internal class CountryFragmentTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    lateinit var mainActivity: ActivityScenario<MainActivity>

    @Before
    fun setup(){
        mainActivity = activityScenarioRule.scenario
    }

    val timeoutInMillis = 2500L

    @Test
    fun mainScenarioTest(){

        val context = ApplicationProvider.getApplicationContext<Context>()
        val displayedList = context.resources
            .getStringArray(R.array.countries_list)
            .toList()

        assertTrue(!displayedList.isEmpty())

        // test with random element
        onView(withId(R.id.countriesRecyclerView))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(20))
            .check(matches(
                isDisplayed()
            ))
            .inRoot(isDialog())

        onView(withText(displayedList.get(20)))
            .check(matches(isDisplayed()))

        Thread.sleep(timeoutInMillis)

        // test with middle element
        val countryPosition = displayedList.size/2
        val countryName = displayedList.get(countryPosition)

        onView(withId(R.id.countriesRecyclerView))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(displayedList.size/2))
            .check(matches(
                isDisplayed()
            ))
            .inRoot(isDialog())

        onView(withText(countryName))
            .check(matches(isDisplayed()))

        Thread.sleep(timeoutInMillis)

        onView(withText(countryName))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText(countryName))
            .check(matches(isDisplayed()))

        onView(withId(R.id.population))
            .check(matches(isDisplayed()))

        Thread.sleep(timeoutInMillis)
    }
}