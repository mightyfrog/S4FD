package org.mightyfrog.android.s4fd;

import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.preference.PreferenceManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mightyfrog.android.s4fd.main.MainActivity;
import org.mightyfrog.android.s4fd.util.TestConst;
import org.mightyfrog.android.s4fd.util.TestUtils;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.mightyfrog.android.s4fd.util.TestUtils.withRecyclerView;
import static org.mightyfrog.android.s4fd.util.TestUtils.withToolbarV7Title;

/**
 * @author Shigehiro Soejima
 */
@RunWith(Enclosed.class)
public class MainSortMenuTest implements TestConst {

    @RunWith(AndroidJUnit4.class)
    public static class LinearViewMode {
        @Rule
        public ActivityTestRule<MainActivity> mActivityTestRule =
                new ActivityTestRule<MainActivity>(MainActivity.class) {
                    @Override
                    protected void beforeActivityLaunched() {
                        PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext()).edit().clear().apply();

                        super.beforeActivityLaunched();
                    }
                };

        @Before
        public void checkGridViewToggleIsShown() { // means it's in the linear view mode.
            onView(withId(R.id.grid_view))
                    .check(matches(isDisplayed()));
        }

        @Test
        public void clickOnSortIconAndSelectNameSortsByNameInAscendingOrder() {
            clickOnSortByNameMenu();

            int len = CHARACTERS.length;
            for (int i = 0; i < len; i++) {
                validateRecyclerViewItemHasCorrectName(i);
            }
        }

        @Test
        public void clickOnSortIconAndSelectNameAndClickOnReverseIconSortsByNameInDescendingOrder() {
            clickOnSortByNameMenu();

            clickOnViewByResId(R.id.reverse);

            int len = CHARACTERS.length;
            for (int i = 0; i < len; i++) {
                scrollRecyclerViewToPosition(i);

                validateRecyclerViewItemAtPositionHasText(i, CHARACTERS[len - 1 - i]);
            }
        }

        @Test
        public void clickOnSortIconAndSelectNameMovesBayonettaToTheTop() {
            validateSortResult(0, BAYONETTA);
        }

        @Test
        public void clickOnSortIconAndSelectWeightMovesBowserToTheTop() {
            validateSortResult(1, BOWSER);
        }

        @Test
        public void clickOnSortIconAndSelectWalkSpeedMovesLucinaToTheTop() {
            validateSortResult(3, LUCINA);
        }

        @Test
        public void clickOnSortIconAndSelectRunSpeedMovesSonicToTheTop() {
            validateSortResult(2, SONIC);
        }

        @Test
        public void clickOnSortIconAndSelectMaxJumpsMovesACharacterWithSixJumpsToTheTop() {
            validateSortResult(4, "6");
        }

        @Test
        public void clickOnSortIconAndSelectWallClingMovesCharacterWithYesToTheTop() {
            validateSortResult(5, "Yes");
        }

        @Test
        public void clickOnSortIconAndSelectWallJumpMovesCharacterWithYesToTheTop() {
            validateSortResult(6, "Yes");
        }

        @Test
        public void clickOnSortIconAndSelectAirSpeedMovesYoshiToTheTop() {
            validateSortResult(7, YOSHI);
        }

        @Test
        public void clickOnSortIconAndSelectCrawlMovesCharacterWithYesToTheTop() {
            validateSortResult(8, "Yes");
        }

        @Test
        public void clickOnSortIconAndSelectTetherMovesCharacterWithYesToTheTop() {
            validateSortResult(9, "Yes");
        }

        @Test
        public void clickOnSortIconAndSelectJumpsquatMovesMiiGunnerToTheTop() {
            validateSortResult(10, MII_GUNNER);
        }

        @Test
        public void clickOnSortIconAndSelectAirAccelerationMovesCharacterWithZeroPointOneToTheTop() {
            validateSortResult(11, "0.1");
        }

        @Test
        public void clickOnSortIconAndSelectSoftLandingLagMovesMiiGunnerToTheTop() {
            validateSortResult(12, MII_GUNNER);
        }

        @Test
        public void clickOnSortIconAndSelectHardLandingLagMovesMiiGunnerToTheTop() {
            validateSortResult(13, MII_GUNNER);
        }

        @Test
        public void clickOnSortIconAndSelectGravityMovesFoxToTheTop() {
            validateSortResult(14, FOX);
        }

        @Test
        public void clickOnSortIconAndSelectFallSpeedMovesFoxToTheTop() {
            validateSortResult(15, FOX);
        }

        @Test
        public void clickOnSortIconAndSelectFastFallSpeedMovesFoxToTheTop() {
            validateSortResult(16, FOX);
        }

        @Test
        public void clickOnSortIconAndSelectSHAirTimeMovesRosalinaToTheTop() {
            validateSortResult(17, ROSALINA_AND_LUMA);
        }

        @Test
        public void clickOnSortIconAndSelectFHAirTimeMovesRosalinaToTheTop() {
            validateSortResult(18, ROSALINA_AND_LUMA);
        }

        private void validateSortResult(int position, String textToMatch) {
            clickOnMenuAtPosition(position);

            validateRecyclerViewItemAtPositionHasText(0, textToMatch);
        }

        private void clickOnMenuAtPosition(int position) {
            clickOnViewByResId(R.id.sort_by_holder);

            clickOnSortMenuPopupAtPosition(position);
        }

        private void clickOnSortMenuPopupAtPosition(int position) {
            onData(anything())
                    .atPosition(position)
                    .perform(click());
        }

        private void clickOnViewByResId(int resId) {
            onView(withId(resId))
                    .perform(click());
        }

        private void clickOnSortByNameMenu() {
            clickOnMenuAtPosition(0);
        }

        private void scrollRecyclerViewToPosition(int i) {
            onView(withId(R.id.recyclerView))
                    .perform(scrollToPosition(i));
        }

        private void validateRecyclerViewItemHasCorrectName(int i) {
            scrollRecyclerViewToPosition(i);

            validateRecyclerViewItemAtPositionHasText(i, CHARACTERS[i]);
        }

        private void validateRecyclerViewItemAtPositionHasText(int i, String name) {
            onView(withRecyclerView(R.id.recyclerView)
                    .atPosition(i))
                    .check(matches(hasDescendant(withText(name))));
        }
    }

    @RunWith(AndroidJUnit4.class)
    public static class GridViewMode {

        @Rule
        public ActivityTestRule<MainActivity> mActivityTestRule =
                new ActivityTestRule<MainActivity>(MainActivity.class) {
                    @Override
                    protected void beforeActivityLaunched() {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("view_mode", 2); // grid view mode
                        editor.clear().apply();

                        super.beforeActivityLaunched();
                    }
                };

        @Before
        public void checkLinearViewToggleIsShown() { // means it's in the grid view mode.
            onView(withId(R.id.linear_view))
                    .check(matches(isDisplayed()));
        }

        @Test
        public void clickOnSortIconAndSelectNameSortsByNameInAscendingOrder() {
            clickOnMenu(0);

            int len = CHARACTERS.length;
            for (int i = 0; i < len; i++) {
                validateToolbarTitle(i);
            }
        }

        private void validateToolbarTitle(int i) {
            scrollRecyclerViewToPosition(i);

            clickOnRecyclerViewItemAtPosition(i);

            validateTitle(CHARACTERS[i]);

            pressBack();
        }

        @Test
        public void clickOnSortIconAndSelectNameAndClickOnReverseIconSortsByNameInDescendingOrder() {
            clickOnMenu(0);

            onView(withId(R.id.reverse))
                    .perform(click());

            int len = CHARACTERS.length;
            for (int i = 0; i < len; i++) {
                scrollRecyclerViewToPosition(i);

                clickOnRecyclerViewItemAtPosition(i);

                validateTitle(CHARACTERS[len - 1 - i]);

                pressBack();
            }
        }

        @Test
        public void clickOnSortIconAndSelectNameMovesBayonettaToTheTop() { // TODO: @Theory is better?
            validateSortResultByTitle(0, BAYONETTA);
        }

        @Test
        public void clickOnSortIconAndSelectWeightMovesBowserToTheTop() {
            validateSortResultByTitle(1, BOWSER);
        }

        @Test
        public void clickOnSortIconAndSelectRunSpeedMovesSonicToTheTop() {
            validateSortByAirTimeResult(2, SONIC);
        }

        @Test
        public void clickOnSortIconAndSelectWalkSpeedMovesLucinaToTheTop() {
            validateSortResultByTitle(3, LUCINA);
        }

        @Test
        public void clickOnSortIconAndSelectMaxJumpsMovesACharacterWithSixJumpsToTheTop() {
            validateSortResult(4, "6");
        }

        @Test
        public void clickOnSortIconAndSelectWallClingMovesCharacterWithYesToTheTop() {
            validateSortResult(5, "Yes");
        }

        @Test
        public void clickOnSortIconAndSelectWallJumpMovesCharacterWithYesToTheTop() {
            validateSortResult(6, "Yes");
        }

        @Test
        public void clickOnSortIconAndSelectAirSpeedMovesYoshiToTheTop() {
            validateSortResultByTitle(7, YOSHI);
        }

        @Test
        public void clickOnSortIconAndSelectCrawlMovesCharacterWithYesToTheTop() {
            validateSortResult(8, "Yes");
        }

        @Test
        public void clickOnSortIconAndSelectTetherMovesCharacterWithYesToTheTop() {
            validateSortResult(9, "Yes");
        }

        @Test
        public void clickOnSortIconAndSelectJumpsquatMovesMiiGunnerToTheTop() {
            validateSortResultByTitle(10, MII_GUNNER);
        }

        @Test
        public void clickOnSortIconAndSelectAirAccelerationMovesCharacterWithZeroPointOneToTheTop() {
            validateSortResult(11, "0.1");
        }

        @Test
        public void clickOnSortIconAndSelectSoftLandingLagMovesMiiGunnerToTheTop() {
            validateSortResultByTitle(12, MII_GUNNER);
        }

        @Test
        public void clickOnSortIconAndSelectHardLandingLagMovesMiiGunnerToTheTop() {
            validateSortResultByTitle(13, MII_GUNNER);
        }

        @Test
        public void clickOnSortIconAndSelectGravityMovesFoxToTheTop() {
            validateSortByAirTimeResult(14, FOX);
        }

        @Test
        public void clickOnSortIconAndSelectFallSpeedMovesFoxToTheTop() {
            validateSortResultByTitle(15, FOX);
        }

        @Test
        public void clickOnSortIconAndSelectFastFallSpeedMovesFoxToTheTop() {
            validateSortByAirTimeResult(16, FOX);
        }

        @Test
        public void clickOnSortIconAndSelectSHAirTimeMovesRosalinaToTheTop() {
            validateSortByAirTimeResult(17, ROSALINA_AND_LUMA);
        }

        @Test
        public void clickOnSortIconAndSelectFHAirTimeMovesRosalinaToTheTop() {
            validateSortByAirTimeResult(18, ROSALINA_AND_LUMA);
        }

        private void validateSortResult(int position, String textToMatch) {
            clickOnMenu(position);

            onView(withRecyclerView(R.id.recyclerView)
                    .atPosition(0))
                    .check(matches(hasDescendant(withText(textToMatch))));
        }

        private void validateSortResultByTitle(int pos, String name) {
            clickOnMenu(pos);

            clickOnRecyclerViewItemAtPosition(0);

            validateTitle(name);
        }

        private void validateSortByAirTimeResult(int pos, String name) {
            clickOnMenu(pos);

            IdlingResource res = TestUtils.startTiming(1000L);
            clickOnRecyclerViewItemAtPosition(0);
            TestUtils.stopTiming(res);

            validateTitle(name);
        }

        private void clickOnMenu(int position) {
            onView(withId(R.id.sort_by_holder))
                    .perform(click());

            onData(anything())
                    .atPosition(position)
                    .perform(click());
        }

        private void scrollRecyclerViewToPosition(int position) {
            onView(withId(R.id.recyclerView))
                    .perform(scrollToPosition(position));
        }

        private void clickOnRecyclerViewItemAtPosition(int position) {
            onView(withId(R.id.recyclerView))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));
        }

        private void validateTitle(String character) {
            // open details to see if it has the correct name
            onView(withId(R.id.toolbar))
                    .check(matches(withToolbarV7Title(is(character))));
        }
    }
}
