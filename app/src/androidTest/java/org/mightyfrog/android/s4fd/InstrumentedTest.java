package org.mightyfrog.android.s4fd;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.web.webdriver.Locator;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mightyfrog.android.s4fd.main.MainActivity;
import org.mightyfrog.android.s4fd.util.TestConst;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.web.assertion.WebViewAssertions.webMatches;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.getText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.mightyfrog.android.s4fd.util.TestUtils.withAlpha;
import static org.mightyfrog.android.s4fd.util.TestUtils.withCollapsibleToolbarTitle;
import static org.mightyfrog.android.s4fd.util.TestUtils.withToolbarV7Title;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest implements TestConst {

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
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("org.mightyfrog.android.s4fd", appContext.getPackageName());
    }

    /* open details activity on click */

    @Test
    public void clickOnCharacterOpensItsDetailsInDetailsActivity() {
        for (String CHARACTER : CHARACTERS) {
            onView(withId(R.id.recyclerView))
                    .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(CHARACTER)), click()));

            onView(withId(R.id.toolbar))
                    .check(matches(hasDescendant(withText(CHARACTER))));

            pressBack();
        }
    }

    @Test
    public void clickOnCompareFABOpensCompareDialog() {
        clickOnBayonetta();

        clickOnFab();

        validateViewWithTextIsDisplayed("Compare");
    }

    @Test
    public void clickOnItemInCompareDialogShowsVsThumbnail() {
        clickOnBayonetta();

        clickOnFab();

        clickOnCompareDialogFirstItem();

        validateViewByResIdIsDisplayed(R.id.vsThumbnail);
    }

    @Test
    public void clickOnItemInCompareDialogShowsVsThumbnailAndOpenCompareDialogAgainAndClickClearHidesVsThumbnail() {
        clickOnBayonetta();

        clickOnFab();

        clickOnCompareDialogFirstItem();

        validateViewByResIdIsDisplayed(R.id.vsThumbnail);

        clickOnFab();

        clickOnViewByResId(android.R.id.button3);

        validateViewByResIdIsNotDisplayed(R.id.vsThumbnail);
    }

    @Test
    public void scrollingUpAppBarHidesFAB() {
        clickOnBayonetta();

        // Action will not be performed because the target view does not match one or more of the following constraints:
        // at least 90 percent of the view's area is displayed to the user.
//        onView(withId(R.id.viewPager))
//                .perform(swipeRight());

        swipeUpAppBar();

        validateViewByResIdIsNotDisplayed(R.id.fab);
    }

    @Test
    public void scrollingUpAppBarHidesVsThumbnail() {
        clickOnBayonetta();

        clickOnFab();

        clickOnCompareDialogFirstItem();

        swipeUpAppBar();

        validateVsThumbnailAlphaValue(0f);
    }

    @Test
    public void scrollingUpAppBarHidesVsThumbnailThenScrollingDownAppBarShowsVsThumbnail() {
        clickOnBayonetta();

        clickOnFab();

        clickOnCompareDialogFirstItem();

        swipeUpAppBar();

        validateVsThumbnailAlphaValue(0f);

        swipeDownViewPager();

        validateVsThumbnailAlphaValue(1f);
    }

    @Test
    public void openBayonettaAndOpenAttacksAndClickOnJab1OpensCompareActivityWithJabAsItsTitle() {
        clickOnBayonetta();

        selectTabByName("Attacks");

        clickOnAttackAndValidateToolbarTitle("Jab 1", "Jab");
    }

    @Test
    public void openBayonettaAndOpenMiscsAndClickOnAirDodgeOpensCompareMiscsActivityWithAirDodgeAsItsTitle() {
        clickOnBayonetta();

        selectTabByName("Miscs");

        clickOnAttackAndValidateToolbarTitle("Air Dodge", "Air Dodge");
    }

    @Test
    public void openBayonettaAndCompareWithBowserAndCollapseAppBar_TitleShouldBeBayoVsBowser() {
        clickOnBayonetta();

        clickOnFab();

        clickOnCompareDialogFirstItem();

        swipeUpAppBar();

        onView(withId(R.id.collapsing_toolbar))
                .check(matches(withCollapsibleToolbarTitle(is("Bayonetta ⇄ Bowser"))));
    }

    /* web view */

    @Test
    public void openCharacterAndWebViewShowsItsData() {
        int len = CHARACTERS.length;
        for (int i = 0; i < len; i++) {
            onView(withId(R.id.recyclerView))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));

            selectTabByName("KH Web");

            onWebView(withId(R.id.webView)).forceJavascriptEnabled();

            onWebView(withId(R.id.webView))
                    .withElement(findElement(Locator.TAG_NAME, "html"))
                    .check(webMatches(getText(), containsString(CHARACTERS[i])));

            pressBack();
        }
    }

    @Test
    public void openInBrowserMenuIsVisibleInKHWebTabOnly() {
        clickOnBayonetta();

        validateViewByResIdDoesNotExist(R.id.open_in_browser);

        selectTabByName("Attacks");

        validateViewByResIdDoesNotExist(R.id.open_in_browser);

        selectTabByName("Miscs");

        validateViewByResIdDoesNotExist(R.id.open_in_browser);

        selectTabByName("KH Web");

        validateViewByResIdIsDisplayed(R.id.open_in_browser);
    }

    /* toggle linear/grid view */

    @Test
    public void clickOnGridViewIconShowsLinearViewIconAndClickOnLinearViewIconShowsGridViewIcon() {
        clickOnViewByResId(R.id.grid_view);

        validateViewByResIdIsDisplayed(R.id.linear_view);

        clickOnViewByResId(R.id.linear_view);

        validateViewByResIdIsDisplayed(R.id.grid_view);
    }

    private void clickOnViewByResId(int resId) {
        onView(withId(resId))
                .perform(click());
    }

    private void swipeUpAppBar() {
        onView(withId(R.id.appbar))
                .perform(swipeUp());
    }

    private void swipeDownViewPager() {
        onView(withId(R.id.viewPager)) // can't swipe down R.id.appbar here
                .perform(swipeDown());
    }

    private void selectTabByName(String tabName) {
        onView(withText(tabName))
                .perform(click()); // swipeLeft() doesn't work
    }

    private void clickOnBayonetta() {
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    private void clickOnFab() {
        clickOnViewByResId(R.id.fab);
    }

    private void clickOnAttackAndValidateToolbarTitle(String attackName, String title) {
        onView(allOf(withId(R.id.recyclerView), hasDescendant(withText(attackName))))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(attackName)), click()));

        onView(withId(R.id.toolbar))
                .check(matches(withToolbarV7Title(is(title))));
    }

    private void validateViewByResIdIsDisplayed(int resId) {
        onView(withId(resId))
                .check(matches(isDisplayed()));
    }

    private void validateViewByResIdIsNotDisplayed(int resId) {
        onView(withId(resId))
                .check(matches(not(isDisplayed())));
    }

    private void validateViewWithTextIsDisplayed(String text) {
        onView(withText(text))
                .check(matches(isDisplayed()));
    }

    private void clickOnCompareDialogFirstItem() {
        onData(anything())
                .atPosition(0)
                .perform(click());
    }

    private void validateViewByResIdDoesNotExist(int resId) {
        onView(withId(resId))
                .check(doesNotExist());
    }

    private void validateVsThumbnailAlphaValue(float alpha) {
        onView(isAssignableFrom(CardView.class))
                .check(matches(withAlpha(alpha)));
    }
}
