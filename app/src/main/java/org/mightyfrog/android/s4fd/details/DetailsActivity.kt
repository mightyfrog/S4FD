package org.mightyfrog.android.s4fd.details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.raizlabs.android.dbflow.sql.language.Select
import com.squareup.picasso.Picasso
import org.mightyfrog.android.s4fd.App
import org.mightyfrog.android.s4fd.R
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.data.KHCharacter_Table
import org.mightyfrog.android.s4fd.util.Const
import javax.inject.Inject

/**
 * @author Shigehiro Soejima
 */
class DetailsActivity : AppCompatActivity(), DetailsContract.View, AppBarLayout.OnOffsetChangedListener {
    @Inject
    lateinit var mPresenter: DetailsPresenter

    private lateinit var mTabLayout: TabLayout

    private lateinit var mViewPager: ViewPager

    private lateinit var mBackdrop: ImageView

    private lateinit var mVsThumbnail: ImageView

    private lateinit var mFab: FloatingActionButton

    private var mCharacter: KHCharacter? = null

    private var mCharToCompare: KHCharacter? = null

    private lateinit var mCollapsingToolbarLayout: CollapsingToolbarLayout

    private var mIsAppBarCollapsed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getIntExtra("id", 0)
        if (id <= 0 || id > Const.CHARACTER_COUNT) {
            finish()
            return
        }

        mCharacter = Select().from(KHCharacter::class.java).where(KHCharacter_Table.id.eq(id)).querySingle()

        setTheme(resources.getIdentifier("CharTheme." + id, "style", packageName))

        setContentView(R.layout.activity_details)

        DaggerDetailsComponent.builder()
                .appComponent((application as App).getAppComponent())
                .detailsModule(DetailsModule(this))
                .build()
                .inject(this)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = mCharacter?.displayName?.trim()

        mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar) as CollapsingToolbarLayout

        mVsThumbnail = findViewById(R.id.vsThumbnail) as ImageView

        mViewPager = findViewById(R.id.viewPager) as ViewPager
        val titles = resources.getStringArray(R.array.detail_tabs)
        mViewPager.adapter = TabContentAdapter(titles, supportFragmentManager, id)
        mViewPager.offscreenPageLimit = 3

        mTabLayout = findViewById(R.id.tabLayout) as TabLayout
        mTabLayout.setupWithViewPager(mViewPager)
        mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // no-op
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // no-op
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                supportInvalidateOptionsMenu()
            }
        })

        mFab = findViewById(R.id.fab) as FloatingActionButton
        mFab.setOnClickListener { view ->
            mPresenter.compare(id)
        }

        (findViewById(R.id.appbar) as AppBarLayout).addOnOffsetChangedListener(this)

        mBackdrop = findViewById(R.id.backdrop) as ImageView
        Picasso.with(this)
                .load(mCharacter?.mainImageUrl)
                .into(mBackdrop)

        mViewPager.post({ mPresenter.setCharToCompareIfAny(id) })
    }

    override fun onResume() {
        super.onResume()

        mBackdrop.visibility = View.VISIBLE
    }

    override fun onPause() {
        mBackdrop.visibility = View.GONE

        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.open_in_browser)?.isVisible = mTabLayout.selectedTabPosition == 3

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
            }
            R.id.open_in_browser -> {
                openInBrowser()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        appBarLayout ?: return

        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(verticalOffset) / maxScroll.toFloat()

        if (percentage >= 0.7f && mIsAppBarCollapsed) {
            mFab.hide()
            (mVsThumbnail.parent as View).animate().setDuration(500L).alpha(0f).start()
            mIsAppBarCollapsed = !mIsAppBarCollapsed
        } else if (percentage < 0.7f && !mIsAppBarCollapsed) {
            mFab.show()
            (mVsThumbnail.parent as View).animate().setDuration(500L).alpha(1f).start()
            mIsAppBarCollapsed = !mIsAppBarCollapsed
        }

        mCharToCompare?.let {
            if (percentage == 1f) {
                mCollapsingToolbarLayout.title = getString(R.string.attr_compare, mCharacter?.displayName, it.displayName)
            } else {
                mCollapsingToolbarLayout.title = mCharacter?.displayName?.trim()
            }
        }
    }

    override fun updateSubtitle(subtitle: CharSequence?) {
        supportActionBar?.subtitle = subtitle
    }

    override fun hideVsThumbnail() {
        mVsThumbnail.visibility = View.GONE
    }

    override fun showVsThumbnail(charToCompare: KHCharacter?) {
        charToCompare?.apply {
            Picasso.with(this@DetailsActivity)
                    .load(thumbnailUrl)
                    .into(mVsThumbnail)
            (mVsThumbnail.parent as CardView).setCardBackgroundColor(Color.parseColor(colorTheme))
        }
        mVsThumbnail.visibility = View.VISIBLE
        mVsThumbnail.alpha = 0f
        mVsThumbnail.animate().setDuration(750L).alpha(1f).start()
    }

    override fun setCharToCompare(charToCompare: KHCharacter?) {
        mCharToCompare = charToCompare
    }

    override fun showCompareDialog(list: List<KHCharacter>, displayNames: List<String>, scrollPosition: Int) {
        val ownerId = intent.getIntExtra("id", 0)
        val dialog = AlertDialog.Builder(this)
                .setTitle(R.string.compare)
                .setSingleChoiceItems(displayNames.toTypedArray(), scrollPosition, { dialogInterface, which ->
                    mPresenter.setCharToCompare(ownerId, list[which])
                    dialogInterface.dismiss()
                })
                .setNeutralButton(R.string.clear, { dialogInterface, i ->
                    mPresenter.setCharToCompare(ownerId, null)
                })
                .setNegativeButton(R.string.cancel, null)
                .create()
        dialog.ownerActivity = this
        dialog.show()
    }

    override fun showActivityCircle() {
        findViewById(R.id.activity_circle).visibility = View.VISIBLE
    }

    override fun hideActivityCircle() {
        findViewById(R.id.activity_circle).visibility = View.GONE
    }

    override fun setPresenter(presenter: DetailsPresenter) {
        mPresenter = presenter
    }

    override fun showErrorMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun openInBrowser() {
        val i = Intent()
        i.action = Intent.ACTION_VIEW
        i.data = Uri.parse(mCharacter?.fullUrl)
        try {
            startActivity(i)
        } catch (e: ActivityNotFoundException) {
            // just in case
            Toast.makeText(this, "No browser found :(", Toast.LENGTH_LONG).show()
        }
    }
}
