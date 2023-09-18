package com.inlay.hotelroomservice.presentation.activities

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.inlay.hotelroomservice.R
import org.junit.Rule
import org.junit.Test

internal class MainActivityTest {

    @get:Rule
    val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun getGoToHotels() {
        Espresso.onView(ViewMatchers.withId(R.id.fab_search)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun getGoToDetails() {
    }

    @Test
    fun showProgressBar() {
    }
}