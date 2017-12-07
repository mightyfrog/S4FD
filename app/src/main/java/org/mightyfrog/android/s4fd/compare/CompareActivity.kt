package org.mightyfrog.android.s4fd.compare

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_compare.*
import org.mightyfrog.android.s4fd.App
import org.mightyfrog.android.s4fd.R
import org.mightyfrog.android.s4fd.data.Move
import java.util.*
import javax.inject.Inject

/**
 * @author Shigehiro Soejima
 */
class CompareActivity : AppCompatActivity(), CompareContract.View {
    @Inject
    lateinit var comparePresenter: ComparePresenter

    @Inject
    lateinit var prefs: SharedPreferences

    private val adapter = DataAdapter(ArrayList(0))

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

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = name
            setDisplayHomeAsUpEnabled(true)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val charToCompareId = intent.getIntExtra("charToCompareId", 0)
        comparePresenter.loadMoves(name, ownerId, charToCompareId)
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
            if (prefs.getInt("compare_sort_type", DataAdapter.SORT_BY_CHAR) == DataAdapter.SORT_BY_CHAR) {
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
            android.R.id.home -> {
                finish()
            }
            R.id.sort_by_char -> {
                comparePresenter.sort(DataAdapter.SORT_BY_CHAR)
            }
            R.id.sort_by_move -> {
                comparePresenter.sort(DataAdapter.SORT_BY_MOVE)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showMoves(list: List<Move>, scrollPosition: Int) {
        adapter.update(list)
        recyclerView.post({
            recyclerView.center(scrollPosition)
        })
    }

    override fun showSortedMoves(type: Int) {
        adapter.sort(type)
        invalidateOptionsMenu()
    }

    override fun showErrorMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun setPresenter(presenter: ComparePresenter) {
        comparePresenter = presenter
    }
}