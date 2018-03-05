package org.mightyfrog.android.s4fd.main

import android.Manifest
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
import android.view.*
import android.widget.Toast
import com.tbruyelle.rxpermissions.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
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
    lateinit var mainPresenter: MainPresenter

    @Inject
    lateinit var prefs: SharedPreferences

    private var progressDialog: ProgressDialog? = null

    private var surfaceRotation = Surface.ROTATION_0

    private val onItemClickListener = object : OnItemClickListener {
        override fun onItemClick(id: Int, position: Int) {
            showDetails(id, position)
        }

        override fun onSorted() {
            recyclerView.scrollToPosition(0)
        }
    }

    private var characterAdapter = CharacterAdapter(ArrayList(0), onItemClickListener)

    private var itemDecor: RecyclerView.ItemDecoration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DaggerMainComponent.builder()
                .appComponent((application as App).getAppComponent())
                .mainModule(MainModule(this))
                .build()
                .inject(this)

        setSupportActionBar(toolbar)

        recyclerView.adapter = characterAdapter
        setViewMode(prefs.getInt("view_mode", CharacterAdapter.MODE_LINEAR))

        updateSurfaceRotation()

        mainPresenter.loadCharacters()
    }

    override fun onResume() {
        super.onResume()

        characterAdapter.clearLastAdapterPosition()
    }

    override fun onPause() {
        if (!BuildConfig.DEBUG) {
            dump()
        }

        super.onPause()
    }

    override fun onDestroy() {
        mainPresenter.destroy()

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
        menu?.apply {
            when (prefs.getInt("view_mode", CharacterAdapter.MODE_LINEAR)) {
                CharacterAdapter.MODE_LINEAR -> {
                    findItem(R.id.grid_view)?.isVisible = true
                    findItem(R.id.linear_view)?.isVisible = false
                }
                CharacterAdapter.MODE_GRID -> {
                    findItem(R.id.grid_view)?.isVisible = false
                    findItem(R.id.linear_view)?.isVisible = true
                }
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.apply {
            when (itemId) {
                R.id.settings -> {
                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                }
                R.id.reverse -> {
                    characterAdapter.reverse()
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
                    characterAdapter.sort(itemId)
                    updateSubtitle(itemId)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showCharacters(list: List<KHCharacter>?) {
        characterAdapter.update(list)
        hideActivityCircle()
    }

    override fun showDetails(id: Int, position: Int) {
        Intent(this, DetailsActivity::class.java).let {
            it.putExtra("id", id)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, getViewAtPosition(position), "thumbnail")
            startActivity(it, options.toBundle())
        }
    }

    override fun showErrorMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        activity_circle.visibility = View.GONE
    }

    override fun showErrorMessage(resId: Int) {
        showErrorMessage(getString(resId))
    }

    override fun showActivityCircle() {
        activity_circle.visibility = View.VISIBLE
    }

    override fun hideActivityCircle() {
        activity_circle.visibility = View.GONE
    }

    override fun showProgressDialog(resId: Int, vararg arg: String?) {
        progressDialog ?: run {
            progressDialog = ProgressDialog(this)
            progressDialog?.isIndeterminate = true
            progressDialog?.setCancelable(false)
            progressDialog?.show()
            hideActivityCircle()
        }
        progressDialog?.setMessage(getString(resId, if (arg.isEmpty()) null else arg[0]))
    }

    override fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    override fun showFallbackDialog() {
        AlertDialog.Builder(this)
                .setMessage(R.string.connection_timeout)
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    fallback()
                }
                .setNegativeButton(android.R.string.cancel) { dialog, which ->
                    finish()
                }
                .show()
    }

    private fun fallback() {
        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe {
                    if (it) {
                        mainPresenter.copyDatabase(assets.open("smash4data.db"), getDatabasePath(AppDatabase.NAME + ".db"))
                    } else {
                        finish()
                    }
                }
    }

    override fun showDatabaseCopiedDialog() {
        AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(R.string.database_copied)
                .setPositiveButton(R.string.close_app) { dialog, which ->
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
                .show()
    }

    override fun setPresenter(presenter: MainPresenter) {
        mainPresenter = presenter
    }

    private fun updateSurfaceRotation() {
        val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        surfaceRotation = display.rotation
        setViewMode(prefs.getInt("view_mode", CharacterAdapter.MODE_LINEAR))
    }

    private fun getViewAtPosition(position: Int): View {
        val view = recyclerView.layoutManager.findViewByPosition(position)
        return view.findViewById(R.id.thumbnail)
    }

    private fun setViewMode(mode: Int) { // TODO: refactor me
        recyclerView.removeItemDecoration(itemDecor)
        val layoutManager: RecyclerView.LayoutManager
        when (mode) {
            CharacterAdapter.MODE_LINEAR -> {
                itemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
                layoutManager = GridLayoutManager(this, 2)
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (surfaceRotation == android.view.Surface.ROTATION_0
                                || surfaceRotation == android.view.Surface.ROTATION_180) 2 else 1
                    }
                }
                prefs.edit().putInt("view_mode", CharacterAdapter.MODE_LINEAR).apply()
            }
            else -> {
                val colCount = if (surfaceRotation == android.view.Surface.ROTATION_0
                        || surfaceRotation == android.view.Surface.ROTATION_180) 8 else 5
                itemDecor = SimpleItemDecoration(1)
                layoutManager = GridLayoutManager(this, 40)
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int = colCount
                }
                prefs.edit().putInt("view_mode", CharacterAdapter.MODE_GRID).apply()
            }
        }
        layoutManager.initialPrefetchItemCount = Const.CHARACTER_COUNT
        recyclerView.layoutManager = layoutManager
        (recyclerView.adapter as CharacterAdapter).setMode(mode)
        recyclerView.addItemDecoration(itemDecor)
        invalidateOptionsMenu()
    }

    private var sortBy = -1

    private fun updateSubtitle(sortBy: Int) {
        if (sortBy == this.sortBy) {
            return
        }

        this.sortBy = sortBy

        supportActionBar?.apply {
            subtitle = when (sortBy) {
                R.id.sort_by_name -> null
                R.id.sort_by_weight -> "Weight"
                R.id.sort_by_run_speed -> "Run Speed"
                R.id.sort_by_walk_speed -> "Walk Speed"
                R.id.sort_by_max_jumps -> "Max Jumps"
                R.id.sort_by_wall_cling -> "Wall Cling"
                R.id.sort_by_wall_jump -> "Wall Jump"
                R.id.sort_by_air_speed -> "Air Speed"
                R.id.sort_by_crawl -> "Crawl"
                R.id.sort_by_tether -> "Tether"
                R.id.sort_by_jumpsquat -> "Jumpsquat"
                R.id.sort_by_air_acceleration -> "Air Acceleration"
                R.id.sort_by_soft_landing_lag -> "Soft Landing Lag"
                R.id.sort_by_hard_landing_lag -> "Hard Landing Lag"
                R.id.sort_by_gravity -> "Gravity"
                R.id.sort_by_fall_speed -> "Fall Speed"
                R.id.sort_by_fast_fall_speed -> "Fast Fall Speed"
                R.id.sort_by_sh_air_time -> "SH Air Time"
                R.id.sort_by_fh_air_time -> "FH Air Time"
                else -> null
            }
        }
    }

    interface OnItemClickListener { // TODO: rename me
        fun onItemClick(id: Int, position: Int)

        fun onSorted()
    }

    private fun dump() {
        getDatabasePath(AppDatabase.NAME + ".db")
                .copyTo(File(Environment.getExternalStorageDirectory(), "smash4data.db"))
    }
}