package org.mightyfrog.android.s4fd.compare

import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.raizlabs.android.dbflow.sql.language.Select
import com.squareup.picasso.Picasso
import org.mightyfrog.android.s4fd.R
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.data.KHCharacter_Table
import org.mightyfrog.android.s4fd.data.Move

/**
 * @author Shigehiro Soejima
 */
class DataAdapter(val list: MutableList<Move>) : RecyclerView.Adapter<DataAdapter.MoveViewHolder>() {

    companion object {
        const val SORT_BY_CHAR: Int = 1
        const val SORT_BY_MOVE: Int = 2
    }

    private val mThumbnailUrlMap = SparseArray<String?>()

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = list[position].id.toLong()

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MoveViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveViewHolder {
        return MoveViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.vh_comparison, parent, false))
    }

    inner class MoveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbnailIv = itemView.findViewById<ImageView>(R.id.thumbnail)
        private val nameTv = itemView.findViewById<TextView>(R.id.name)
        private val valueTv = itemView.findViewById<TextView>(R.id.value)

        fun bind(datum: Move) {
            datum.apply {
                nameTv.text = name
                val thumbnailUrl = mThumbnailUrlMap.get(ownerId)
                        ?: Select().from(KHCharacter::class.java).where(KHCharacter_Table.id.eq(ownerId)).querySingle()?.thumbnailUrl
                mThumbnailUrlMap.put(ownerId, thumbnailUrl)
                Picasso.with(thumbnailIv.context)
                        .load(thumbnailUrl)
                        .placeholder(R.drawable.placeholder)
                        .into(thumbnailIv)
                val hitbox = fromHtml(hitboxActive)
                val faf = fromHtml(firstActionableFrame)
                val damage = fromHtml(baseDamage)
                val angle = fromHtml(angle)
                val bkb = fromHtml(baseKnockBackSetKnockback)
                val lag = fromHtml(landingLag)
                val ac = fromHtml(autoCancel)
                val kbg = fromHtml(knockbackGrowth)
                when (type) {
                    0 -> { // aerials
                        valueTv.text = itemView.context.getString(R.string.attack_frame_data_0, hitbox, faf, damage, angle, bkb, lag, ac, kbg)
                    }
                    1 -> { // grounds
                        if (damage.isEmpty()) { // grabs
                            valueTv.text = itemView.context.getString(R.string.attack_frame_data_1b, hitbox, faf)
                        } else {
                            valueTv.text = itemView.context.getString(R.string.attack_frame_data_1, hitbox, faf, damage, angle, bkb, kbg)
                        }
                    }
                    2 -> { // throws
                        valueTv.text = itemView.context.getString(R.string.attack_frame_data_1, hitbox, faf, damage, angle, bkb, kbg)
                    }
                    3 -> { // specials
                        valueTv.text = itemView.context.getString(R.string.attack_frame_data_3, damage, angle, bkb, kbg)
                    }
                    else -> valueTv.text = toString()
                }
            }
        }

        private fun fromHtml(html: String?) = Html.fromHtml(html)
    }

    fun update(newList: List<Move>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun sort(sortBy: Int) {
        when (sortBy) {
            SORT_BY_CHAR -> {
                list.sortBy { it.ownerId }
            }
            SORT_BY_MOVE -> {
                list.sortBy { it.name }
            }
        }
        notifyDataSetChanged()
    }
}