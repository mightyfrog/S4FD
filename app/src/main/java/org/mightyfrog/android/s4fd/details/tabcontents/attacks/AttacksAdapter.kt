package org.mightyfrog.android.s4fd.details.tabcontents.attacks

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.mightyfrog.android.s4fd.R
import org.mightyfrog.android.s4fd.data.Move

/**
 * @author Shigehiro Soejima
 */
class AttacksAdapter(private var list: List<Move>, private val listener: AttacksFragment.OnItemClickListener) : RecyclerView.Adapter<AttacksAdapter.AttributeViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = list[position].id.toLong()

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: AttributeViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributeViewHolder {
        return AttributeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.vh_attack, parent, false)).apply {
            itemView.setOnClickListener {
                var name = list[adapterPosition].name
                var counter = 0
                for (c in name.toCharArray()) {
                    if (c.isLetter() || c == ' ') {
                        counter++
                        continue
                    }
                    counter--
                    break
                }
                name = name.subSequence(0, counter).toString()
                listener.onItemClick(name)
            }
        }
    }

    fun update(list: List<Move>) {
        this.list = list
        notifyDataSetChanged()
    }

    class AttributeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTv = itemView.findViewById<TextView>(R.id.name)
        private val typeTv = itemView.findViewById<TextView>(R.id.type)
        private val valueTv = itemView.findViewById<TextView>(R.id.value)

        fun bind(datum: Move) {
            datum.apply {
                nameTv.text = name
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
                        typeTv.text = itemView.context.getString(R.string.type_aerial)
                        valueTv.text = itemView.context.getString(R.string.attack_frame_data_0, hitbox, faf, damage, angle, bkb, lag, ac, kbg)
                    }
                    1 -> { // grounds
                        typeTv.text = itemView.context.getString(R.string.type_ground)
                        if (damage.isEmpty()) { // grabs,dodges
                            valueTv.text = itemView.context.getString(R.string.attack_frame_data_1b, hitbox, faf)
                        } else {
                            valueTv.text = itemView.context.getString(R.string.attack_frame_data_1, hitbox, faf, damage, angle, bkb, kbg)
                        }
                    }
                    2 -> { // specials
                        typeTv.text = itemView.context.getString(R.string.type_special)
                        valueTv.text = itemView.context.getString(R.string.attack_frame_data_1, hitbox, faf, damage, angle, bkb, kbg)
                    }
                    3 -> { // throws
                        typeTv.text = itemView.context.getString(R.string.type_throw)
                        valueTv.text = itemView.context.getString(R.string.attack_frame_data_3, damage, angle, bkb, kbg)
                    }
                    else -> valueTv.text = toString()
                }
            }
        }

        private fun fromHtml(html: String?): Spanned = Html.fromHtml(html)
    }
}