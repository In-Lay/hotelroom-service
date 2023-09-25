package com.inlay.hotelroomservice.presentation.fragments.profile


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
class ProfileFragmentTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun profileFragmentTest() {
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

        val shapeableImageView = onView(
            allOf(
                withId(R.id.header_image),
                childAtPosition(
                    childAtPosition(
                        withId(com.google.android.material.R.id.navigation_header_container),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        shapeableImageView.perform(click())

        val textInputEditText = onView(
            allOf(
                withId(R.id.et_email),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.et_layout_email),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("test321@mail.com"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.et_email), withText("test321@mail.com"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.et_layout_email),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(pressImeActionButton())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.et_password),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.et_layout_password),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("test321"), closeSoftKeyboard())

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.et_password), withText("test321"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.et_layout_password),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText4.perform(pressImeActionButton())

        val materialButton = onView(
            allOf(
                withId(R.id.btn_login), withText("Sign in"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.fragment_container_view),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val imageView = onView(
            allOf(
                withId(R.id.img_user_profile),
                withParent(withParent(withId(R.id.fragment_container_view))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.tv_user_name), withText("tester3211"),
                withParent(withParent(withId(R.id.fragment_container_view))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("tester3211")))

        val textView2 = onView(
            allOf(
                withId(R.id.tv_user_mail), withText("test321@mail.com"),
                withParent(withParent(withId(R.id.fragment_container_view))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("test321@mail.com")))

        val linearLayout = onView(
            allOf(
                withId(R.id.layout_logout),
                withParent(withParent(withId(R.id.fragment_container_view))),
                isDisplayed()
            )
        )
        linearLayout.check(matches(isDisplayed()))

        val imageView2 = onView(
            allOf(
                withId(R.id.img_edit_icon),
                withParent(
                    allOf(
                        withId(R.id.toolbar_general),
                        withParent(withId(R.id.appbar_layout))
                    )
                ),
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
