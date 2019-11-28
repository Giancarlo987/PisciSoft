package com.example.piscisoftmobile


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginIncorrectoTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun loginIncorrectoTest() {
        val appCompatEditText = onView(
            allOf(
                withId(R.id.et_codigo),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("20160315"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.et_password),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("xyzqwery"), closeSoftKeyboard())

        val appCompatButton = onView(
            allOf(
                withId(R.id.btn_iniciar_sesion), withText("Iniciar sesión"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        val button = onView(
            allOf(
                withId(R.id.btn_iniciar_sesion),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))
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
