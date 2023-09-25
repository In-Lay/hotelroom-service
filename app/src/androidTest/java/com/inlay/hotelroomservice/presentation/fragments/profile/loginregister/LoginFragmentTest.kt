package com.inlay.hotelroomservice.presentation.fragments.profile.loginregister


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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun loginFragmentTest() {
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
        textInputEditText.perform(replaceText("usermail"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.et_email), withText("usermail"),
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
        textInputEditText3.perform(replaceText("userpassword123"), closeSoftKeyboard())

        val checkableImageButton = onView(
            allOf(
                withId(com.google.android.material.R.id.text_input_end_icon),
                withContentDescription("Show password"),
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

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.et_password), withText("userpassword123"),
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

        val editText = onView(
            allOf(
                withId(R.id.et_email), withText("usermail"),
                withParent(withParent(withId(R.id.et_layout_email))),
                isDisplayed()
            )
        )
        editText.check(matches(withText("usermail")))

        val editText2 = onView(
            allOf(
                withId(R.id.et_password), withText("userpassword123"),
                withParent(withParent(withId(R.id.et_layout_password))),
                isDisplayed()
            )
        )
        editText2.check(matches(withText("userpassword123")))

        val button = onView(
            allOf(
                withId(R.id.btn_login), withText("Sign in"),
                withParent(withParent(withId(R.id.fragment_container_view))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.tv_register_suggestion), withText("Don't have an account? Join now"),
                withParent(withParent(withId(R.id.fragment_container_view))),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))

        val imageView = onView(
            allOf(
                withId(R.id.img_close_icon),
                withParent(withParent(withId(R.id.fragment_container_view))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))
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
