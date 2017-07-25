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
import android.support.v4.app.ActivityCompat
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
    lateinit var detailsPresenter: DetailsPresenter

    private lateinit var tabLayout: TabLayout

    private lateinit var viewPager: ViewPager

    private lateinit var backdrop: ImageView

    private lateinit var vsThumbnail: ImageView

    private lateinit var fab: FloatingActionButton

    private var character: KHCharacter? = null

    private var charToCompare: KHCharacter? = null

    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout

    private var isAppBarCollapsed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getIntExtra("id", 0)
        if (id <= 0 || id > Const.CHARACTER_COUNT) {
            finish()
            return
        }

        character = Select().from(KHCharacter::class.java).where(KHCharacter_Table.id.eq(id)).querySingle()

        setTheme(resources.getIdentifier("CharTheme." + id, "style", packageName))

        setContentView(R.layout.activity_details)

        DaggerDetailsComponent.builder()
                .appComponent((application as App).getAppComponent())
                .detailsModule(DetailsModule(this))
                .build()
                .inject(this)

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        supportActionBar?.apply {
            title = character?.displayName?.trim()
            setDisplayHomeAsUpEnabled(true)
        }

        collapsingToolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)

        vsThumbnail = findViewById<ImageView>(R.id.vsThumbnail)

        viewPager = findViewById<ViewPager>(R.id.viewPager)
        val titles = resources.getStringArray(R.array.detail_tabs)
        viewPager.adapter = TabContentAdapter(titles, supportFragmentManager, id)
        viewPager.offscreenPageLimit = 3

        tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // no-op
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // no-op
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                ActivityCompat.invalidateOptionsMenu(this@DetailsActivity)
            }
        })

        fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            detailsPresenter.compare(id)
        }

        findViewById<AppBarLayout>(R.id.appbar).addOnOffsetChangedListener(this)

        backdrop = findViewById<ImageView>(R.id.backdrop)
        Picasso.with(this)
                .load(character?.mainImageUrl)
                .into(backdrop)

        viewPager.post({ detailsPresenter.setCharToCompareIfAny(id) })
    }

    override fun onResume() {
        super.onResume()

        backdrop.visibility = View.VISIBLE
    }

    override fun onPause() {
        backdrop.visibility = View.GONE

        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.open_in_browser)?.isVisible = tabLayout.selectedTabPosition == 3

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

        if (percentage >= 0.7f && isAppBarCollapsed) {
            fab.hide()
            (vsThumbnail.parent as View).animate().setDuration(500L).alpha(0f).start()
            isAppBarCollapsed = !isAppBarCollapsed
        } else if (percentage < 0.7f && !isAppBarCollapsed) {
            fab.show()
            (vsThumbnail.parent as View).animate().setDuration(500L).alpha(1f).start()
            isAppBarCollapsed = !isAppBarCollapsed
        }

        charToCompare?.let {
            if (percentage == 1f) {
                collapsingToolbarLayout.title = getString(R.string.attr_compare, character?.displayName, it.displayName)
            } else {
                collapsingToolbarLayout.title = character?.displayName?.trim()
            }
        }
    }

    override fun updateSubtitle(subtitle: CharSequence?) {
        supportActionBar?.subtitle = subtitle
    }

    override fun hideVsThumbnail() {
        vsThumbnail.visibility = View.GONE
    }

    override fun showVsThumbnail(charToCompare: KHCharacter?) {
        charToCompare?.apply {
            Picasso.with(this@DetailsActivity)
                    .load(thumbnailUrl)
                    .into(vsThumbnail)
            (vsThumbnail.parent as CardView).setCardBackgroundColor(Color.parseColor(colorTheme))
        }
        vsThumbnail.visibility = View.VISIBLE
        vsThumbnail.alpha = 0f
        vsThumbnail.animate().setDuration(750L).alpha(1f).start()
    }

    override fun setCharToCompare(charToCompare: KHCharacter?) {
        this.charToCompare = charToCompare
    }

    override fun showCompareDialog(list: List<KHCharacter>, displayNames: List<String>, scrollPosition: Int) {
        val ownerId = intent.getIntExtra("id", 0)
        val dialog = AlertDialog.Builder(this)
                .setTitle(R.string.compare)
                .setSingleChoiceItems(displayNames.toTypedArray(), scrollPosition, { dialogInterface, which ->
                    detailsPresenter.setCharToCompare(ownerId, list[which])
                    dialogInterface.dismiss()
                })
                .setNeutralButton(R.string.clear, { dialogInterface, i ->
                    detailsPresenter.setCharToCompare(ownerId, null)
                })
                .setNegativeButton(R.string.cancel, null)
                .create()
        dialog.ownerActivity = this
        dialog.show()
    }

    override fun showActivityCircle() {
        findViewById<View>(R.id.activity_circle).visibility = View.VISIBLE
    }

    override fun hideActivityCircle() {
        findViewById<View>(R.id.activity_circle).visibility = View.GONE
    }

    override fun setPresenter(presenter: DetailsPresenter) {
        detailsPresenter = presenter
    }

    override fun showErrorMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun openInBrowser() {
        val i = Intent()
        i.action = Intent.ACTION_VIEW
        i.data = Uri.parse(character?.fullUrl)
        try {
            startActivity(i)
        } catch (e: ActivityNotFoundException) {
            // just in case
            Toast.makeText(this, "No browser found :(", Toast.LENGTH_LONG).show()
        }
    }
}
