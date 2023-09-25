package com.inlay.hotelroomservice.presentation.fragments.search


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.presentation.activities.SplashActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class FragmentSearchTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun fragmentSearchTest() {
        val floatingActionButton = onView(
            allOf(
                withId(R.id.fab_search), withContentDescription("Search button"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.drawer_layout),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val button = onView(
            allOf(
                withId(R.id.efab_search), withText("Search Hotels"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val editText = onView(
            allOf(
                withId(R.id.search_bar),
                withText("Search Location"),
                withContentDescription("Search for location"),
                withParent(
                    allOf(
                        withId(R.id.collapsing_toolbar_layout),
                        withParent(withId(R.id.appbar_layout))
                    )
                ),
                isDisplayed()
            )
        )
        editText.check(matches(withText("Search Location")))

        val checkableImageButton = onView(
            allOf(
                withId(com.google.android.material.R.id.text_input_end_icon),
                withContentDescription("Show dropdown menu"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.textfield.EndCompoundLayout")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        checkableImageButton.perform(click())

        val checkableImageButton2 = onView(
            allOf(
                withId(com.google.android.material.R.id.text_input_end_icon),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.textfield.EndCompoundLayout")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        checkableImageButton2.perform(click())

        val materialButton = onView(
            allOf(
                withId(com.google.android.material.R.id.cancel_button),
                withContentDescription("Cancel"),
                childAtPosition(
                    allOf(
                        withId(com.google.android.material.R.id.fullscreen_header),
                        childAtPosition(
                            withId(com.google.android.material.R.id.mtrl_picker_fullscreen),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
