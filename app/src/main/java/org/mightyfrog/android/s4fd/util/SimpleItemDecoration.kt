package org.mightyfrog.android.s4fd.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Shigehiro Soejima
 */
class SimpleItemDecoration(private val mSpacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = mSpacing
        outRect.right = mSpacing
        outRect.bottom = mSpacing
        outRect.top = mSpacing
    }
}