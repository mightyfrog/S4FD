package org.mightyfrog.android.s4fd.details.tabcontents.attributes

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.raizlabs.android.dbflow.sql.language.Select
import org.mightyfrog.android.s4fd.R
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.data.MovementDatum
import org.mightyfrog.android.s4fd.data.MovementDatum_Table

/**
 * @author Shigehiro Soejima
 */
class AttributesAdapter(var mList: List<MovementDatum>) : RecyclerView.Adapter<AttributesAdapter.AttributeViewHolder>() {
    private var mCharToCompare: KHCharacter? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemCount(): Int = mList.size

    override fun getItemId(position: Int): Long = mList[position].id.toLong()

    override fun onBindViewHolder(holder: AttributeViewHolder?, position: Int) {
        holder?.bind(mList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AttributeViewHolder
            = AttributeViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.vh_attribute, parent, false))

    fun update(list: List<MovementDatum>) {
        mList = list
        notifyDataSetChanged()
    }

    fun compare(char: KHCharacter?) {
        mCharToCompare = char
        notifyDataSetChanged()
    }

    inner class AttributeViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private val nameTv = itemView?.findViewById(R.id.name) as TextView
        private val valueTv = itemView?.findViewById(R.id.value) as TextView

        fun bind(datum: MovementDatum) {
            nameTv.text = datum.name
            val value = datum.value?.replace("frame", " frame")
            if (mCharToCompare != null) {
                val datumToComp = Select().from(MovementDatum::class.java)
                        .where(MovementDatum_Table.ownerId.eq(mCharToCompare!!.id))
                        .and(MovementDatum_Table.name.eq(datum.name))
                        .querySingle()
                if (datum.ownerId != datumToComp?.ownerId) {
                    val compValue = datumToComp?.value?.replace("frame", " frame")
                    valueTv.text = itemView.context.getString(R.string.attr_compare, value, compValue)
                } else {
                    valueTv.text = value
                }
            } else {
                valueTv.text = value
            }
        }
    }
}