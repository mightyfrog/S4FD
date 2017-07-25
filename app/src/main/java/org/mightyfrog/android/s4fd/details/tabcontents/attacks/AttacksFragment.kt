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

    private val mListener = object : OnItemClickListener {
        override fun onItemClick(name: String) {
            attackPresenter.compare(name)
        }
    }

    private val mAdapter = AttacksAdapter(ArrayList<Move>(0), mListener)

    companion object {
        fun newInstance(b: Bundle): AttacksFragment = AttacksFragment().apply {
            arguments = b
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AttacksPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_tab_content, container, false)
        view?.apply {
            val rv = findViewById<RecyclerView>(R.id.recyclerView)
            rv.adapter = mAdapter
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        attackPresenter.loadMoves(arguments.getInt("id"))
    }

    override fun showComparison(name: String, charToCompare: KHCharacter?) {
        val intent = Intent(activity, CompareActivity::class.java)
        intent.putExtra("name", name)
        intent.putExtra("ownerId", arguments.getInt("id"))
        intent.putExtra("charToCompareId", charToCompare?.id)
        startActivity(intent)
//        activity.overridePendingTransition(R.anim.slide_in_up, 0)
    }

    override fun showMoves(list: List<Move>) {
        mAdapter.update(list)
    }

    override fun showErrorMessage(msg: String) {
        activity?.apply {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }

    override fun setPresenter(presenter: AttacksPresenter) {
        attackPresenter = presenter
    }

    fun setCharToCompare(char: KHCharacter?) { // hmm...
        attackPresenter.setCharToCompare(char)
    }

    interface OnItemClickListener {
        fun onItemClick(name: String)
    }
}