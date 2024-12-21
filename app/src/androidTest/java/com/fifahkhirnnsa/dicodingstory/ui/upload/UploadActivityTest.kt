package com.fifahkhirnnsa.dicodingstory.ui.upload

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.fifahkhirnnsa.dicodingstory.R
import com.fifahkhirnnsa.dicodingstory.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class UploadActivityTest {

    @Before
    fun setUp() {
        ActivityScenario.launch(UploadActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getEspressoIdlingResource())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getEspressoIdlingResource())
    }

    @Test
    fun uploadStory_Success() {
        onView(withId(R.id.previewImageView)).check(matches(isDisplayed()))
        onView(withId(R.id.button_gallery)).check(matches(isDisplayed()))
        onView(withId(R.id.button_camera)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_add_description)).check(matches(isDisplayed()))
        onView(withId(R.id.button_add)).check(matches(isDisplayed()))

        onView(withId(R.id.button_gallery)).perform(click())

        onView(withId(R.id.ed_add_description))
            .perform(typeText("Test Story Description"), closeSoftKeyboard())

        onView(withId(R.id.switchLocation)).perform(click())

        onView(withId(R.id.button_add)).check(matches(isEnabled()))
    }

    @Test
    fun uploadStory_WithLocation_Success() {
        onView(withId(R.id.ed_add_description))
            .perform(typeText("Test Story With Location"), closeSoftKeyboard())

        onView(withId(R.id.switchLocation)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.switchLocation)).check(matches(isChecked()))
    }
}