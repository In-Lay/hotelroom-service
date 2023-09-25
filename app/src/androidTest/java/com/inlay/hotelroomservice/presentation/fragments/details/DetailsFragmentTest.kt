package com.inlay.hotelroomservice.presentation.fragments.details


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class DetailsFragmentTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun detailsFragmentTest() {
        val recyclerView = onView(
            allOf(
                withId(R.id.hotels_recycler_view),
                childAtPosition(
                    withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                    0
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val materialTextView = onView(
            allOf(
                withId(R.id.tv_restaurants_see_all), withText("View All"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scroll_view),
                        0
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(click())

        val shapeableImageView = onView(
            allOf(
                withId(R.id.img_close_icon),
                childAtPosition(
                    allOf(
                        withId(R.id.places_nearby_layout),
                        childAtPosition(
                            withId(com.google.android.material.R.id.custom),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        shapeableImageView.perform(click())

        val materialTextView2 = onView(
            allOf(
                withId(R.id.tv_attractions_see_all), withText("View All"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scroll_view),
                        0
                    ),
                    12
                ),
                isDisplayed()
            )
        )
        materialTextView2.perform(click())

        val shapeableImageView2 = onView(
            allOf(
                withId(R.id.img_close_icon),
                childAtPosition(
                    allOf(
                        withId(R.id.places_nearby_layout),
                        childAtPosition(
                            withId(com.google.android.material.R.id.custom),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        shapeableImageView2.perform(click())

        val shapeableImageView3 = onView(
            allOf(
                withId(R.id.img_hotel_image),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scroll_view),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        shapeableImageView3.perform(click())

        val shapeableImageView4 = onView(
            allOf(
                withId(R.id.img_close_icon),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        shapeableImageView4.perform(click())

        val imageView = onView(
            allOf(
                withId(R.id.img_hotel_image),
                withParent(withParent(withId(R.id.scroll_view))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val view = onView(
            allOf(
                withId(R.id.chip_parking), withText("Paid public parking nearby"),
                withParent(
                    allOf(
                        withId(R.id.chips_amenities),
                        withParent(withId(R.id.details_chip_group))
                    )
                ),
                isDisplayed()
            )
        )
        view.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.tv_restaurants_see_all), withText("View All"),
                withParent(withParent(withId(R.id.scroll_view))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("View All")))

        val imageView2 = onView(
            allOf(
                withId(R.id.img_places_nearby_image),
                withParent(withParent(withId(R.id.card_restaurant_nearby))),
                isDisplayed()
            )
        )
        imageView2.check(matches(isDisplayed()))
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
