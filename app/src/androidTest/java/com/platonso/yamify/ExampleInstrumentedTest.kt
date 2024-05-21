package com.platonso.yamify

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.platonso.yamify.activity.MainActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Before
    fun setUp() {
        // Запуск MainActivity перед каждым тестом
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.platonso.yamify", appContext.packageName)
    }

    @Test
    fun testNavigationToIngredientsFragment() {
        // Нажатие на кнопку навигации Ingredients
        onView(withId(R.id.navigation_ingredients)).perform(click())

        // Проверка, что отображается фрагмент Ingredients (проверяем его содержимым)
        onView(withId(R.id.vegetables_tv)).check(matches(withText("Овощи")))
    }

    @Test
    fun testNavigationToRecipeFragment() {
        // Нажатие на кнопку навигации Recipe
        onView(withId(R.id.navigation_recipe)).perform(click())

        // Проверка, что отображается фрагмент Recipe (проверяем его содержимое)
        onView(withId(R.id.button_add_to_favourites)).check(matches(withText("Добавить в избранное")))
    }
}