package com.inlay.hotelroomservice.presentation.activities


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.inlay.hotelroomservice.R
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun splashActivityTest() {
        val imageButton = onView(
            allOf(
                withId(R.id.fab_search), withContentDescription("Search button"),
                withParent(withParent(withId(R.id.drawer_layout))),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val imageButton2 = onView(
            allOf(
                withContentDescription("Navigate up"),
                withParent(
                    allOf(
                        withId(R.id.toolbar_general),
                        withParent(withId(R.id.toolbar))
                    )
                ),
                isDisplayed()
            )
        )
        imageButton2.check(matches(isDisplayed()))

        val imageView = onView(
            allOf(
                withId(R.id.hotel_image),
                withParent(withParent(withId(R.id.item_layout))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val imageView2 = onView(
            allOf(
                withId(R.id.hotel_image),
                withParent(withParent(withId(R.id.item_layout))),
                isDisplayed()
            )
        )
        imageView2.check(matches(isDisplayed()))
    }
}
