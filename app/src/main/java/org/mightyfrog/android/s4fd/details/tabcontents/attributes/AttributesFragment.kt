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
    private lateinit var attributesPresenter: AttributesPresenter

    private val adapter = AttributesAdapter(ArrayList(0))

    companion object {
        fun newInstance(b: Bundle): AttributesFragment = AttributesFragment().apply {
            arguments = b
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AttributesPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab_content, container, false)?.apply {
            val rv = findViewById<RecyclerView>(R.id.recyclerView)
            rv.adapter = adapter
            val glm = GridLayoutManager(context, 6)
            glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (surfaceRotation == Surface.ROTATION_0 || surfaceRotation == Surface.ROTATION_180) 3 else 2
                }
            }
            rv.layoutManager = glm
            rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.apply {
            attributesPresenter.loadAttributes(getInt("id"))
        }
    }

    override fun showAttributes(list: List<MovementDatum>) {
        adapter.update(list)
    }

    override fun setCharToCompare(char: KHCharacter?) {
        adapter.compare(char)
    }

    override fun showErrorMessage(msg: String) {
        activity?.apply {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }

    override fun showErrorMessage(resId: Int) {
        showErrorMessage(getString(resId))
    }

    override fun setPresenter(presenter: AttributesPresenter) {
        attributesPresenter = presenter
    }
}