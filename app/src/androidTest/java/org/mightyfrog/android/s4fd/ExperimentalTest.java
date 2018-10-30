package org.mightyfrog.android.s4fd;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.preference.PreferenceManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mightyfrog.android.s4fd.main.MainActivity;
import org.mightyfrog.android.s4fd.util.TestConst;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author soejima.shigehiro on 2017/02/28.
 */
@RunWith(AndroidJUnit4.class)
public class ExperimentalTest implements TestConst {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class) {
                @Override
                protected void beforeActivityLaunched() {
                    PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext()).edit().clear().apply();
                    super.beforeActivityLaunched();
                }
            };

    @Test
    public void test() {
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click())); // Bayo

        onView(withId(R.id.open_in_browser))
                .check(doesNotExist());

        onView(withText("Attacks"))
                .perform(click()); // swipeLeft() doesn't work

        onView(withId(R.id.open_in_browser))
                .check(doesNotExist());

        onView(withText("Miscs"))
                .perform(click()); // swipeLeft() doesn't work

        onView(withId(R.id.open_in_browser))
                .check(doesNotExist());

        onView(withText("KH Web"))
                .perform(click()); // swipeLeft() doesn't work

        onView(withId(R.id.open_in_browser))
                .check(matches(isDisplayed()));
    }
}
