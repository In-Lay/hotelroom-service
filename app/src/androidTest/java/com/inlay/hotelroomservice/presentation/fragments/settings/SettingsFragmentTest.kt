package com.inlay.hotelroomservice.presentation.fragments.settings


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
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SettingsFragmentTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun settingsFragmentTest() {
        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar_general),
                        childAtPosition(
                            withId(R.id.toolbar),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val navigationMenuItemView = onView(
            allOf(
                withId(R.id.item_settings),
                childAtPosition(
                    allOf(
                        withId(com.google.android.material.R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.navigation_view),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView.perform(click())

        val materialSwitch = onView(
            allOf(
                withId(R.id.switch_notifications),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.fragment_container_view),
                        0
                    ),
                    11
                ),
                isDisplayed()
            )
        )
        materialSwitch.perform(click())

        val linearLayout = onView(
            allOf(
                withId(R.id.lang_change_layout),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.fragment_container_view),
                        0
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        linearLayout.perform(click())

        val materialRadioButton = onView(
            allOf(
                withId(R.id.btn_ru),
                childAtPosition(
                    allOf(
                        withId(R.id.rado_group),
                        childAtPosition(
                            withId(R.id.lang_dialog_layout),
                            8
                        )
                    ),
                    2
                )
            )
        )
        materialRadioButton.perform(scrollTo(), click())

        val materialButton = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withId(com.google.android.material.R.id.buttonPanel),
                        0
                    ),
                    3
                )
            )
        )
        materialButton.perform(scrollTo(), click())

        val materialButton2 = onView(
            allOf(
                withId(android.R.id.button1), withText("Change"),
                childAtPosition(
                    childAtPosition(
                        withId(com.google.android.material.R.id.buttonPanel),
                        0
                    ),
                    3
                )
            )
        )
        materialButton2.perform(scrollTo(), click())

        val materialSwitch2 = onView(
            allOf(
                withId(R.id.switch_day_night),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.fragment_container_view),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        materialSwitch2.perform(click())

        val switch_ = onView(
            allOf(
                withId(R.id.switch_day_night), withText("���"),
                withParent(withParent(withId(R.id.fragment_container_view))),
                isDisplayed()
            )
        )
        switch_.check(matches(isDisplayed()))

        val linearLayout2 = onView(
            allOf(
                withId(R.id.lang_change_layout),
                withParent(withParent(withId(R.id.fragment_container_view))),
                isDisplayed()
            )
        )
        linearLayout2.check(matches(isDisplayed()))

        val imageView = onView(
            allOf(
                withId(R.id.img_lang_icon),
                withParent(withParent(withId(R.id.fragment_container_view))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.tv_lang_label), withText("����"),
                withParent(withParent(withId(R.id.fragment_container_view))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("����")))
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
