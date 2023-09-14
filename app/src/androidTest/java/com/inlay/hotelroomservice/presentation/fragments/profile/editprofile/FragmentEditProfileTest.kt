package com.inlay.hotelroomservice.presentation.fragments.profile.editprofile

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.inlay.hotelroomservice.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
internal class FragmentEditProfileTest {
    //    val fragmentScenarioRule = activityScenarioRule
    private lateinit var scenario: FragmentScenario<FragmentEditProfile>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer()
    }

    @Test
    fun changeText_full_name() {
        val userName = "User Name"

        onView(withId(R.id.et_full_name)).perform(typeText(userName), closeSoftKeyboard())

        onView(withId(R.id.et_full_name)).check(matches(withText(userName)))
    }
}