package com.inlay.hotelroomservice.domain.usecase.sharedpreferences

import android.content.SharedPreferences
import com.inlay.hotelroomservice.CoroutineTestRule
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals


internal class GetLanguagePreferencesImplTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val sharedPreferences = mockk<SharedPreferences>(relaxed = true)

    private lateinit var getLangPrefs: GetLanguagePreferencesImpl

    @Before
    fun setUp() {
        getLangPrefs = GetLanguagePreferencesImpl(sharedPreferences)
    }

    @Test
    fun getLanguage() {
        val expectedValue = "es"

        every { sharedPreferences.getString(any(), any()) } returns expectedValue

        val result = getLangPrefs.getLanguage()

        assertEquals(expectedValue, result)
    }
}