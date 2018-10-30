package org.mightyfrog.android.s4fd.comparemiscs

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_compare_miscs.*
import org.mightyfrog.android.s4fd.R

/**
 * @author Shigehiro Soejima
 */
class CompareMiscsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ownerId = intent.getIntExtra("ownerId", 0)
        setTheme(resources.getIdentifier("CharTheme.$ownerId", "style", packageName))

        setContentView(R.layout.activity_compare_miscs)

        setSupportActionBar(toolbar)
        initActionBar()

        recyclerView.layoutManager = GridLayoutManager(this, 1)
        recyclerView.adapter = DataAdapter(intent.getStringExtra("name"), ownerId, intent.getIntExtra("charToCompareId", 0))
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.slide_out_down)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            val name = intent.getStringExtra("name")
            when (name) {
                "SPOTDODGE" -> title = "Spot Dodge"
                "AIRDODGE" -> title = "Air Dodge"
                "ROLLS" -> title = "Forward/Back Roll"
                "LEDGEROLL" -> title = "Ledge Roll"
            }
        }
    }
}