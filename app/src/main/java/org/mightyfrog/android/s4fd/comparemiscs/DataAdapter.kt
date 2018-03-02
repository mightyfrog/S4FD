package org.mightyfrog.android.s4fd.comparemiscs

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.raizlabs.android.dbflow.sql.language.Select
import com.raizlabs.android.dbflow.sql.queriable.StringQuery
import com.squareup.picasso.Picasso
import org.mightyfrog.android.s4fd.R
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.data.KHCharacter_Table
import org.mightyfrog.android.s4fd.data.MiscAttribute
import org.mightyfrog.android.s4fd.util.Const
import java.util.*

/**
 * @author Shigehiro Soejima
 */
class DataAdapter(var name: String, ownerId: Int, charToCompareId: Int) : RecyclerView.Adapter<DataAdapter.MiscViewHolder>() {
    private val list: ArrayList<Misc> = ArrayList(0)

    private val thumbnailUrlMap = SparseArray<String?>()

    init {
        setHasStableIds(true)

        if (charToCompareId != 0) {
            list.addAll(getMiscList(ownerId))
            if (ownerId != charToCompareId) {
                list.addAll(getMiscList(charToCompareId))
            }
        } else {
            for (id in 1..Const.CHARACTER_COUNT) { // 1 to 58
                list.addAll(getMiscList(id))
            }
        }
    }

    override fun getItemId(position: Int) = list[position].ownerId.toLong()

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MiscViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MiscViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.vh_comparison, parent, false))

    private fun getMiscList(id: Int): List<Misc> { // TODO: rewrite me :(
        val miscList: ArrayList<Misc> = ArrayList(1)
        val list: List<MiscAttribute> = StringQuery(MiscAttribute::class.java, "select a.id as \"id\", a.name as \"cName\", b.name as \"sName\", a.rank, a.ownerId, a.value from CharacterAttributeDatum a, SmashAttributeType b where a.smashAttributeTypeId = b.id and a.ownerId=$id and b.name='$name'").queryList()
        val misc1 = Misc()
        misc1.ownerId = id
        misc1.name = list[0].sName
        when (name) {
            "AIRDODGE" -> misc1.displayName = "Air Dodge"
            "ROLLS" -> misc1.displayName = "Forward/Back Roll"
            "LEDGEROLL" -> misc1.displayName = "Ledge Roll"
            "SPOTDODGE" -> misc1.displayName = "Spot Dodge"
        }
        misc1.intangibility = list[0].value
        misc1.faf = list[1].value
        miscList.add(misc1)

        return miscList
    }

    inner class MiscViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbnailIv = itemView.findViewById<ImageView>(R.id.thumbnail)
        private val nameTv = itemView.findViewById<TextView>(R.id.name)
        private val valueTv = itemView.findViewById<TextView>(R.id.value)

        fun bind(datum: Misc) {
            datum.apply {
                val thumbnailUrl = thumbnailUrlMap.get(ownerId) ?: Select().from(KHCharacter::class.java).where(KHCharacter_Table.id.eq(ownerId)).querySingle()?.thumbnailUrl
                thumbnailUrlMap.put(ownerId, thumbnailUrl)
                Picasso.with(thumbnailIv.context)
                        .load(thumbnailUrl)
                        .placeholder(R.drawable.placeholder)
                        .into(thumbnailIv)
                nameTv.text = displayName
                valueTv.text = itemView.context.getString(R.string.misc_data, intangibility, faf)
            }
        }
    }

    class Misc {
        var ownerId: Int = 0
        var name: String? = "N/A"
        var displayName: String? = "N/A"
        var intangibility: String? = "N/A"
        var faf: String? = "N/A"
    }
}