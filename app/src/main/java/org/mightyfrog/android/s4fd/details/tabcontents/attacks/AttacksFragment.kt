package org.mightyfrog.android.s4fd.details.tabcontents.attacks

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
import org.mightyfrog.android.s4fd.compare.CompareActivity
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.data.Move
import org.mightyfrog.android.s4fd.details.tabcontents.BaseFragment
import java.util.*

/**
 * @author Shigehiro Soejima
 */
class AttacksFragment : BaseFragment(), AttacksContract.View {
    private lateinit var attackPresenter: AttacksPresenter

    private val listener = object : OnItemClickListener {
        override fun onItemClick(name: String) {
            attackPresenter.compare(name)
        }
    }

    private val adapter = AttacksAdapter(ArrayList(0), listener)

    companion object {
        fun newInstance(b: Bundle): AttacksFragment = AttacksFragment().apply {
            arguments = b
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AttacksPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab_content, container, false)?.apply {
            val rv = findViewById<RecyclerView>(R.id.recyclerView)
            rv.adapter = adapter
            val glm = GridLayoutManager(context, 2)
            glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (surfaceRotation == Surface.ROTATION_0 || surfaceRotation == Surface.ROTATION_180) 2 else 1
                }
            }
            rv.layoutManager = glm
            rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.apply {
            attackPresenter.loadMoves(getInt("id"))
        }
    }

    override fun showComparison(name: String, charToCompare: KHCharacter?) {
        Intent(activity, CompareActivity::class.java).apply {
            putExtra("name", name)
            putExtra("ownerId", arguments?.getInt("id"))
            putExtra("charToCompareId", charToCompare?.id)
            startActivity(this)
//        activity.overridePendingTransition(R.anim.slide_in_up, 0)
        }
    }

    override fun showMoves(list: List<Move>) {
        adapter.update(list)
    }

    override fun showErrorMessage(msg: String) {
        activity?.apply {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }

    override fun showErrorMessage(resId: Int) {
        showErrorMessage(getString(resId))
    }

    override fun setPresenter(presenter: AttacksPresenter) {
        attackPresenter = presenter
    }

    fun setCharToCompare(char: KHCharacter?) { // hmmm...
        attackPresenter.setCharToCompare(char)
    }

    interface OnItemClickListener {
        fun onItemClick(name: String)
    }
}