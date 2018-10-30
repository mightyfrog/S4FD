package org.mightyfrog.android.s4fd;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mightyfrog.android.s4fd.compare.CompareActivity;
import org.mightyfrog.android.s4fd.details.DetailsActivity;
import org.mightyfrog.android.s4fd.main.MainActivity;
import org.mightyfrog.android.s4fd.rules.InitDBFlow;
import org.mightyfrog.android.s4fd.settings.SettingsActivity;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.Objects;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(Enclosed.class)
public class UnitTest {

    @Rule
    public InitDBFlow dbFlow = new InitDBFlow();

    @Before
    public void setup() {
//            ShadowLog.stream = System.out;

        PreferenceManager.getDefaultSharedPreferences(application)
                .edit().clear().apply();
    }

    @RunWith(RobolectricTestRunner.class)
    @Config(constants = BuildConfig.class, sdk = 25)
    public static class Main {

        @Test
        public void validateMainActivityTitle() {
            MainActivity activity = Robolectric.setupActivity(MainActivity.class);
            Toolbar toolbar = activity.findViewById(R.id.toolbar);
            assertEquals("MainActivity title is S4FD.", toolbar.getTitle(), activity.getString(R.string.app_name));
        }

        @Test
        public void validateInitialMenuItemVisibilities() {
            MainActivity activity = Robolectric.setupActivity(MainActivity.class);
            Toolbar toolbar = activity.findViewById(R.id.toolbar);
            ShadowActivity shadow = shadowOf(activity);
            shadow.onCreateOptionsMenu(toolbar.getMenu());
            assertTrue("Reverse menu is visible.", shadow.getOptionsMenu().findItem(R.id.reverse).isVisible());
            assertTrue("Sort by menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_holder).isVisible());
            assertTrue("Sort by name menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_name).isVisible());
            assertTrue("Sort by weight menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_weight).isVisible());
            assertTrue("Sort by run speed menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_run_speed).isVisible());
            assertTrue("Sort by walk speed menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_walk_speed).isVisible());
            assertTrue("Sort by max jumps menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_max_jumps).isVisible());
            assertTrue("Sort by wall cling menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_wall_cling).isVisible());
            assertTrue("Sort by wall jump menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_wall_jump).isVisible());
            assertTrue("Sort by air speed menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_air_speed).isVisible());
            assertTrue("Sort by crawl menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_crawl).isVisible());
            assertTrue("Sort by tether menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_tether).isVisible());
            assertTrue("Sort by jumpsquat menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_jumpsquat).isVisible());
            assertTrue("Sort by air acceleration menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_air_acceleration).isVisible());
            assertTrue("Sort by soft landing lag menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_soft_landing_lag).isVisible());
            assertTrue("Sort by hard landing lag menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_hard_landing_lag).isVisible());
            assertTrue("Sort by gravity menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_gravity).isVisible());
            assertTrue("Sort by fall speed menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_fall_speed).isVisible());
            assertTrue("Sort by fast fall speed menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_fast_fall_speed).isVisible());
            assertTrue("Sort by SH air time menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_sh_air_time).isVisible());
            assertTrue("Sort by FH air time menu is visible.", shadow.getOptionsMenu().findItem(R.id.sort_by_fh_air_time).isVisible());
            assertTrue("Grid view menu is visible.", shadow.getOptionsMenu().findItem(R.id.grid_view).isVisible());
            assertTrue("Settings menu is visible.", shadow.getOptionsMenu().findItem(R.id.settings).isVisible());

            assertTrue("Linear view menu is not visible.", !shadow.getOptionsMenu().findItem(R.id.linear_view).isVisible());
        }

        @Test
        public void validateLinearGridViewMenuIconVisibilities() {
            MainActivity activity = Robolectric.setupActivity(MainActivity.class);
            ShadowActivity shadow = shadowOf(activity);
            assertTrue("Linear view menu is visible.", !shadow.getOptionsMenu().findItem(R.id.linear_view).isVisible());
            assertTrue("Grid view menu is visible.", shadow.getOptionsMenu().findItem(R.id.grid_view).isVisible());
        }

        @Test
        public void clickOnGridViewMenuTogglesViewMode() {
            MainActivity activity = Robolectric.setupActivity(MainActivity.class);
            ShadowActivity shadow = shadowOf(activity);
            shadow.clickMenuItem(R.id.grid_view);
            assertTrue("Linear view menu is visible.", shadow.getOptionsMenu().findItem(R.id.linear_view).isVisible());
            assertTrue("Grid view menu is visible.", !shadow.getOptionsMenu().findItem(R.id.grid_view).isVisible());
        }

        @Test
        public void validateShowActivityCircleVisibilities() {
            MainActivity activity = Robolectric.setupActivity(MainActivity.class);
            activity.showActivityCircle();
            assertEquals("Activity circle is visible.", activity.findViewById(R.id.activity_circle).getVisibility(), View.VISIBLE);
            activity.hideActivityCircle();
            assertEquals("Activity circle is not visible.", activity.findViewById(R.id.activity_circle).getVisibility(), View.GONE);
        }

        @Test
        public void clickOnSettingsMenuLaunchesSettingsActivity() {
            MainActivity activity = Robolectric.setupActivity(MainActivity.class);
            ShadowActivity shadow = shadowOf(activity);
            shadow.clickMenuItem(R.id.settings);
//            Intent expectedIntent = new Intent(activity, SettingsActivity.class);
//            assertThat(shadow.getNextStartedActivity()).isEqualTo(expectedIntent);
//            https://github.com/robolectric/robolectric/issues/2627
            assertEquals(Objects.requireNonNull(shadowOf(activity).getNextStartedActivity().getComponent()).getClassName(), SettingsActivity.class.getName());
        }
    }

    @RunWith(RobolectricTestRunner.class)
    @Config(constants = BuildConfig.class, sdk = 25)
    public static class Details {

        @Test
        public void validateDetailsActivityTitle() {
            Intent intent = new Intent();
            intent.putExtra("id", 1);
            DetailsActivity activity = Robolectric.buildActivity(DetailsActivity.class, intent)
                    .create()
                    .get();
            Toolbar toolbar = (Toolbar) shadowOf(activity).findViewById(R.id.toolbar);
            assertEquals("DetailsActivity title is Bayonetta: (actual=" + toolbar.getTitle() + ")", "Bayonetta", toolbar.getTitle());
        }

        @Test
        public void validateFABVisibility() {
            Intent intent = new Intent();
            intent.putExtra("id", 1);
            DetailsActivity activity = Robolectric.buildActivity(DetailsActivity.class, intent)
                    .create()
                    .get();
            FloatingActionButton fab = (FloatingActionButton) shadowOf(activity).findViewById(R.id.fab);
            assertEquals("FAB is visible.", fab.getVisibility(), View.VISIBLE);
        }

        @Test
        public void clickOnFABOpensCompareDialog() {
            Intent intent = new Intent();
            intent.putExtra("id", 1);
            DetailsActivity activity = Robolectric.buildActivity(DetailsActivity.class, intent)
                    .create()
                    .get();
            FloatingActionButton fab = (FloatingActionButton) shadowOf(activity).findViewById(R.id.fab);
            fab.performClick();
            AlertDialog alert = (AlertDialog) ShadowAlertDialog.getShownDialogs().get(0);
            assertTrue(alert.isShowing());
        }

        @Test
        public void validateOpenInBrowserMenuVisibility() {
            Intent intent = new Intent();
            intent.putExtra("id", 1);
            DetailsActivity activity = Robolectric.buildActivity(DetailsActivity.class, intent)
                    .create()
                    .visible()
                    .start()
                    .resume()
                    .get();
            Toolbar toolbar = (Toolbar) shadowOf(activity).findViewById(R.id.toolbar);
            TabLayout tabLayout = activity.findViewById(R.id.tabLayout);
            ShadowActivity shadow = shadowOf(activity);
            shadow.onCreateOptionsMenu(toolbar.getMenu());
            assertTrue("Open in browser menu is not visible when Attributes tab is selected.", !shadow.getOptionsMenu().findItem(R.id.open_in_browser).isVisible());
            Objects.requireNonNull(tabLayout.getTabAt(1)).select();
            assertTrue("Open in browser menu is not visible when Attacks tab is selected.", !shadow.getOptionsMenu().findItem(R.id.open_in_browser).isVisible());
            Objects.requireNonNull(tabLayout.getTabAt(2)).select();
            assertTrue("Open in browser menu is not visible when Miscs tab is selected.", !shadow.getOptionsMenu().findItem(R.id.open_in_browser).isVisible());
            Objects.requireNonNull(tabLayout.getTabAt(3)).select();
            assertTrue("Open in browser menu is visible when KH Web tab is selected.", shadow.getOptionsMenu().findItem(R.id.open_in_browser).isVisible());
        }
    }

    @RunWith(RobolectricTestRunner.class)
    @Config(constants = BuildConfig.class, sdk = 25)
    public static class Compare {

        @Test
        public void validateCompareActivityTitle() {
            Intent intent = new Intent();
            intent.putExtra("name", "Jab");
            intent.putExtra("ownerId", 1);
            CompareActivity activity = Robolectric.buildActivity(CompareActivity.class, intent)
                    .create()
                    .get();
            Toolbar toolbar = (Toolbar) shadowOf(activity).findViewById(R.id.toolbar);
            assertEquals("CompareActivity title is Jab: (actual=" + toolbar.getTitle() + ")", "Jab", toolbar.getTitle());
        }

        @Test
        public void validateSortMenuIsVisible() {
            Intent intent = new Intent();
            intent.putExtra("name", "Jab");
            intent.putExtra("ownerId", 1);
            CompareActivity activity = Robolectric.buildActivity(CompareActivity.class, intent)
                    .create()
                    .visible()
                    .get();
            ShadowActivity shadow = shadowOf(activity);
            assertTrue("Toolbar has Sort menu visible.", shadow.getOptionsMenu().findItem(R.id.sort).isVisible());
        }

        @Test
        public void validateBayonettaComparedWithBowser_Jab() {
            Intent intent = new Intent();
            intent.putExtra("name", "Jab");
            intent.putExtra("ownerId", 1);
            intent.putExtra("charToCompareId", 2);
            CompareActivity activity = Robolectric.buildActivity(CompareActivity.class, intent)
                    .create()
                    .visible()
                    .get();
            RecyclerView rv = (RecyclerView) shadowOf(activity).findViewById(R.id.recyclerView);
            rv.measure(0, 0);
            rv.layout(0, 0, 100, 10000);
            int count = Objects.requireNonNull(rv.getAdapter()).getItemCount();
            assertEquals("Bayonetta vs Bowser (Jab) contains 8 rows (actual=" + count + ")", 8, count);
        }
    }

    // need presenter tests
}