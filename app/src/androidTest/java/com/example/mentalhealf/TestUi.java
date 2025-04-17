package com.example.mentalhealf;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.containsString;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

public class TestUi {

    @Rule
    public ActivityScenarioRule<ActivityMain> activityRule =
            new ActivityScenarioRule<>(ActivityMain.class);

    @Test
    public void testLoginButtonWorks() {
        onView(withId(R.id.emailInput)).perform(typeText("floof"), closeSoftKeyboard());
        onView(withId(R.id.passwordInput)).perform(typeText("2pac4Life"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText(containsString("how"))).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyLoginShowsError() {
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("Please enter email")).check(matches(isDisplayed()));
    }

    @Test
    public void testMoodLogOpensJournal() {
        onView(withId(R.id.nav_log)).perform(click());
        onView(withId(R.id.journalView)).check(matches(isDisplayed()));
    }
    // logMoodButton
}
