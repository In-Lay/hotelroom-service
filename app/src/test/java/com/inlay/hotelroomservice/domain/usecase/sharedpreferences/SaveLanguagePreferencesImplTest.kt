package com.inlay.hotelroomservice.domain.usecase.sharedpreferences

import android.content.SharedPreferences
import com.inlay.hotelroomservice.CoroutineTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test


internal class SaveLanguagePreferencesImplTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val sharedPreferences = mockk<SharedPreferences>(relaxed = true)
    private val langKey = "LANGUAGE_KEY"

    private lateinit var saveLanguagePreferencesImpl: SaveLanguagePreferencesImpl

    @Before
    fun setUp() {
        saveLanguagePreferencesImpl = SaveLanguagePreferencesImpl(sharedPreferences)
    }

    @Test
    fun saveLanguage() {
        val expectedValue = "es"
        val putStringMock = mockk<(String, String) -> Unit>(relaxed = true)

        every {
            sharedPreferences.edit().putString(any(), any()).apply()
        } returns putStringMock.invoke(
            langKey,
            expectedValue
        )

        saveLanguagePreferencesImpl.saveLanguage(expectedValue)

        verify { putStringMock.invoke(langKey, expectedValue) }
    }
}