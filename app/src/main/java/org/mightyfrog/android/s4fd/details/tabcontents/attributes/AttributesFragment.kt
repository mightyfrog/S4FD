package org.mightyfrog.android.s4fd.details.tabcontents.attributes

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
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.data.MovementDatum
import org.mightyfrog.android.s4fd.details.tabcontents.BaseFragment
import java.util.*

/**
 * @author Shigehiro Soejima
 */
class AttributesFragment : BaseFragment(), AttributesContract.View {
    private lateinit var mPresenter: AttributesPresenter

    private val mAdapter = AttributesAdapter(ArrayList<MovementDatum>(0))

    companion object {
        fun newInstance(b: Bundle): AttributesFragment = AttributesFragment().apply {
            arguments = b
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AttributesPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_tab_content, container, false)
        val rv = view?.findViewById(R.id.recyclerView) as RecyclerView
        rv.adapter = mAdapter
        val glm = GridLayoutManager(context, 6)
        glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (mSurfaceRotation == Surface.ROTATION_0 || mSurfaceRotation == Surface.ROTATION_180) 3 else 2
            }
        }
        rv.layoutManager = glm
        rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mPresenter.loadAttributes(arguments.getInt("id"))
    }

    override fun showAttributes(list: List<MovementDatum>) {
        mAdapter.update(list)
    }

    override fun setCharToCompare(char: KHCharacter?) {
        mAdapter.compare(char)
    }

    override fun showErrorMessage(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

    override fun setPresenter(presenter: AttributesPresenter) {
        mPresenter = presenter
    }
}