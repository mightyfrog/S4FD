package org.mightyfrog.android.s4fd.main

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import org.mightyfrog.android.s4fd.App
import org.mightyfrog.android.s4fd.BuildConfig
import org.mightyfrog.android.s4fd.R
import org.mightyfrog.android.s4fd.data.AppDatabase
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.details.DetailsActivity
import org.mightyfrog.android.s4fd.settings.SettingsActivity
import org.mightyfrog.android.s4fd.util.Const
import org.mightyfrog.android.s4fd.util.SimpleItemDecoration
import java.io.File
import java.util.*
import javax.inject.Inject

/**
 * @author Shigehiro Soejima
 */
class MainActivity : AppCompatActivity(), MainContract.View {
    @Inject
    lateinit var mPresenter: MainPresenter

    @Inject
    lateinit var mPrefs: SharedPreferences

    private lateinit var mRecyclerView: RecyclerView

    private var mProgressDialog: ProgressDialog? = null

    private var mSurfaceRotation = Surface.ROTATION_0

    private val mListener = object : OnItemClickListener {
        override fun onItemClick(id: Int, position: Int) {
            mPresenter.openCharacter(id, position)
        }

        override fun onSorted() {
            mRecyclerView.scrollToPosition(0)
        }
    }

    private var mAdapter = CharacterAdapter(ArrayList<KHCharacter>(0), mListener)

    private var mItemDecor: RecyclerView.ItemDecoration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }

        DaggerMainComponent.builder()
                .appComponent((application as App).getAppComponent())
                .mainModule(MainModule(this))
                .build()
                .inject(this)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        mRecyclerView = findViewById(R.id.recyclerView) as RecyclerView
        mRecyclerView.adapter = mAdapter
        setViewMode(mPrefs.getInt("view_mode", CharacterAdapter.MODE_LINEAR))

        updateSurfaceRotation()

        mPresenter.loadCharacters()
    }

    override fun onResume() {
        super.onResume()

        mAdapter.clearLastAdapterPosition()
    }

    override fun onPause() {
        if (!BuildConfig.DEBUG) {
            dump()
        }

        super.onPause()
    }

    override fun onDestroy() {
        mPresenter.destroy()

        super.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        updateSurfaceRotation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (mPrefs.getInt("view_mode", CharacterAdapter.MODE_LINEAR)) {
            CharacterAdapter.MODE_LINEAR -> {
                menu?.findItem(R.id.grid_view)?.isVisible = true
                menu?.findItem(R.id.linear_view)?.isVisible = false
            }
            CharacterAdapter.MODE_GRID -> {
                menu?.findItem(R.id.grid_view)?.isVisible = false
                menu?.findItem(R.id.linear_view)?.isVisible = true
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            R.id.reverse -> {
                mAdapter.reverse()
            }
            R.id.linear_view -> {
                setViewMode(CharacterAdapter.MODE_LINEAR)
            }
            R.id.grid_view -> {
                setViewMode(CharacterAdapter.MODE_GRID)
            }
            R.id.sort_by_holder -> {
                // ignore
            }
            else -> {
                mAdapter.sort(item?.itemId)
                updateSubtitle(item?.itemId!!)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showCharacters(list: List<KHCharacter>?) {
        mAdapter.update(list)
        hideActivityCircle()
    }

    override fun showDetails(id: Int, position: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("id", id)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, getViewAtPosition(position), "thumbnail")
        startActivity(intent, options.toBundle())
    }

    override fun showErrorMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        findViewById(R.id.activity_circle).visibility = View.GONE
    }

    override fun showActivityCircle() {
        findViewById(R.id.activity_circle).visibility = View.VISIBLE
    }

    override fun hideActivityCircle() {
        findViewById(R.id.activity_circle).visibility = View.GONE
    }

    override fun showProgressDialog(msg: CharSequence) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog?.isIndeterminate = true
            mProgressDialog?.setCancelable(false)
            mProgressDialog?.show()
            hideActivityCircle()
        }
        mProgressDialog?.setMessage(msg)
    }

    override fun hideProgressDialog() {
        mProgressDialog?.dismiss()
    }

    override fun showFallbackDialog() {
        AlertDialog.Builder(this)
                .setMessage(R.string.connection_timeout)
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    mPresenter.fallback()
                }
                .setNegativeButton(android.R.string.cancel) { dialog, which ->
                    finish()
                }
                .show()
    }

    override fun showDatabaseCopiedDialog() {
        AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(R.string.database_copied)
                .setPositiveButton(R.string.close_app) {
                    dialog, which ->
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
                .show()
    }

    override fun setPresenter(presenter: MainPresenter) {
        mPresenter = presenter
    }

    private fun updateSurfaceRotation() {
        val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        mSurfaceRotation = display.rotation
        setViewMode(mPrefs.getInt("view_mode", CharacterAdapter.MODE_LINEAR))
    }

    private fun getViewAtPosition(position: Int): View {
        val view = mRecyclerView.layoutManager.findViewByPosition(position)
        return view.findViewById(R.id.thumbnail)
    }

    private fun setViewMode(mode: Int) { // TODO: refactor me
        mRecyclerView.removeItemDecoration(mItemDecor)
        val layoutManager: RecyclerView.LayoutManager
        when (mode) {
            CharacterAdapter.MODE_LINEAR -> {
                mItemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
                layoutManager = GridLayoutManager(this, 2)
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (mSurfaceRotation == android.view.Surface.ROTATION_0
                                || mSurfaceRotation == android.view.Surface.ROTATION_180) 2 else 1
                    }
                }
                mPrefs.edit().putInt("view_mode", CharacterAdapter.MODE_LINEAR).apply()
            }
            else -> {
                val colCount = if (mSurfaceRotation == android.view.Surface.ROTATION_0
                        || mSurfaceRotation == android.view.Surface.ROTATION_180) 8 else 5
                mItemDecor = SimpleItemDecoration(1)
                layoutManager = GridLayoutManager(this, 40)
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int = colCount
                }
                mPrefs.edit().putInt("view_mode", CharacterAdapter.MODE_GRID).apply()
            }
        }
        layoutManager.setInitialPrefetchItemCount(Const.CHARACTER_COUNT)
        mRecyclerView.layoutManager = layoutManager
        (mRecyclerView.adapter as CharacterAdapter).setMode(mode)
        mRecyclerView.addItemDecoration(mItemDecor)
        supportInvalidateOptionsMenu()
    }

    private var mSortBy = -1

    private fun updateSubtitle(sortBy: Int) {
        if (sortBy == mSortBy) {
            return
        }

        mSortBy = sortBy
        when (sortBy) {
            R.id.sort_by_name -> {
                supportActionBar?.subtitle = null
            }
            R.id.sort_by_weight -> {
                supportActionBar?.subtitle = "Weight"
            }
            R.id.sort_by_run_speed -> {
                supportActionBar?.subtitle = "Run Speed"
            }
            R.id.sort_by_walk_speed -> {
                supportActionBar?.subtitle = "Walk Speed"
            }
            R.id.sort_by_max_jumps -> {
                supportActionBar?.subtitle = "Max Jumps"
            }
            R.id.sort_by_wall_cling -> {
                supportActionBar?.subtitle = "Wall Cling"
            }
            R.id.sort_by_wall_jump -> {
                supportActionBar?.subtitle = "Wall Jump"
            }
            R.id.sort_by_air_speed -> {
                supportActionBar?.subtitle = "Air Speed"
            }
            R.id.sort_by_crawl -> {
                supportActionBar?.subtitle = "Crawl"
            }
            R.id.sort_by_tether -> {
                supportActionBar?.subtitle = "Tether"
            }
            R.id.sort_by_jumpsquat -> {
                supportActionBar?.subtitle = "Jumpsquat"
            }
            R.id.sort_by_air_acceleration -> {
                supportActionBar?.subtitle = "Air Acceleration"
            }
            R.id.sort_by_soft_landing_lag -> {
                supportActionBar?.subtitle = "Soft Landing Lag"
            }
            R.id.sort_by_hard_landing_lag -> {
                supportActionBar?.subtitle = "Hard Landing Lag"
            }
            R.id.sort_by_gravity -> {
                supportActionBar?.subtitle = "Gravity"
            }
            R.id.sort_by_fall_speed -> {
                supportActionBar?.subtitle = "Fall Speed"
            }
            R.id.sort_by_fast_fall_speed -> {
                supportActionBar?.subtitle = "Fast Fall Speed"
            }
            R.id.sort_by_sh_air_time -> {
                supportActionBar?.subtitle = "SH Air Time"
            }
            R.id.sort_by_fh_air_time -> {
                supportActionBar?.subtitle = "FH Air Time"
            }
        }
    }

    interface OnItemClickListener { // TODO: rename me
        fun onItemClick(id: Int, position: Int)

        fun onSorted()
    }

    fun dump() {
        getDatabasePath(AppDatabase.NAME + ".db")
                .copyTo(File(Environment.getExternalStorageDirectory(), "smash4data.db"))
    }
}