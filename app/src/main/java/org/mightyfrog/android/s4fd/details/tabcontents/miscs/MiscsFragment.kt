package org.mightyfrog.android.s4fd.details.tabcontents.miscs

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import org.mightyfrog.android.s4fd.R
import org.mightyfrog.android.s4fd.comparemiscs.CompareMiscsActivity
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.details.tabcontents.BaseFragment

/**
 * @author Shigehiro Soejima
 */
class MiscsFragment : BaseFragment(), MiscsContract.View {
    private lateinit var miscPresenter: MiscsPresenter

    private val onItemClickListener = object : OnItemClickListener {
        override fun onItemClick(name: String?, ownerId: Int) {
            miscPresenter.compare(name, ownerId)
        }
    }

    companion object {
        fun newInstance(b: Bundle): MiscsFragment = MiscsFragment().apply {
            arguments = b
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MiscsPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_tab_content, container, false)
        view?.apply {
            val rv = findViewById<RecyclerView>(R.id.recyclerView)
            rv.adapter = MiscsAdapter(arguments.getInt("id"), onItemClickListener)
            val glm = GridLayoutManager(context, 2)
            glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (surfaceRotation == Surface.ROTATION_0 || surfaceRotation == Surface.ROTATION_180) 2 else 1
                }
            }
            rv.layoutManager = glm
            rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        return view
    }

    override fun showErrorMessage(msg: String) {
        activity?.apply {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }

    override fun showAttributes(name: String, ownerId: Int, charToCompareId: Int?) {
        activity?.apply {
            val intent = Intent(this, CompareMiscsActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("ownerId", ownerId)
            intent.putExtra("charToCompareId", charToCompareId)
            startActivity(intent)
        }
    }

    override fun setCharToCompare(char: KHCharacter?) {
        miscPresenter.setCharToCompare(char)
    }

    override fun setPresenter(presenter: MiscsPresenter) {
        miscPresenter = presenter
    }

    interface OnItemClickListener {
        fun onItemClick(name: String?, ownerId: Int)
    }
}