package org.mightyfrog.android.s4fd.compare

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import org.mightyfrog.android.s4fd.App
import org.mightyfrog.android.s4fd.R
import org.mightyfrog.android.s4fd.data.Move
import org.mightyfrog.widget.CenteringRecyclerView
import java.util.*
import javax.inject.Inject

/**
 * @author Shigehiro Soejima
 */
class CompareActivity : AppCompatActivity(), CompareContract.View {
    @Inject
    lateinit var mPrefs: SharedPreferences

    @Inject
    lateinit var mPresenter: ComparePresenter

    private lateinit var mRecyclerView: CenteringRecyclerView

    private val mAdapter = DataAdapter(ArrayList<Move>(0))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ownerId = intent.getIntExtra("ownerId", 0)
        setTheme(resources.getIdentifier("CharTheme." + ownerId, "style", packageName))

        setContentView(R.layout.activity_compare)

        DaggerCompareComponent.builder()
                .appComponent((application as App).getAppComponent())
                .compareModule(CompareModule(this))
                .build()
                .inject(this)

        val name = intent.getStringExtra("name")

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mRecyclerView = findViewById(R.id.recyclerView) as CenteringRecyclerView
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val charToCompareId = intent.getIntExtra("charToCompareId", 0)
        mPresenter.loadMoves(name, ownerId, charToCompareId)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.slide_out_down)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.compare, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.apply {
            if (mPrefs.getInt("compare_sort_type", DataAdapter.SORT_BY_CHAR) == DataAdapter.SORT_BY_CHAR) {
                findItem(R.id.sort_by_char)?.isChecked = true
                findItem(R.id.sort_by_move)?.isChecked = false
            } else {
                findItem(R.id.sort_by_char)?.isChecked = false
                findItem(R.id.sort_by_move)?.isChecked = true
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            R.id.sort_by_char -> {
                mPresenter.sort(DataAdapter.SORT_BY_CHAR)
            }
            R.id.sort_by_move -> {
                mPresenter.sort(DataAdapter.SORT_BY_MOVE)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showMoves(list: List<Move>, scrollPosition: Int) {
        mAdapter.update(list)
        mRecyclerView.post({
            mRecyclerView.center(scrollPosition)
        })
    }

    override fun showSortedMoves(type: Int) {
        mAdapter.sort(type)
        supportInvalidateOptionsMenu()
    }

    override fun showErrorMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun setPresenter(presenter: ComparePresenter) {
        mPresenter = presenter
    }
}