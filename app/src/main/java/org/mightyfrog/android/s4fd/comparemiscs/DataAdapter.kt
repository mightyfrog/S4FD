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
class DataAdapter(var mName: String, ownerId: Int, charToCompareId: Int) : RecyclerView.Adapter<DataAdapter.MiscViewHolder>() {
    private val mList: ArrayList<Misc> = ArrayList(0)

    private val mThumbnailUrlMap = SparseArray<String?>()

    init {
        setHasStableIds(true)

        if (charToCompareId != 0) {
            mList.addAll(getMiscList(ownerId))
            if (ownerId != charToCompareId) {
                mList.addAll(getMiscList(charToCompareId))
            }
        } else {
            for (id in 1..Const.CHARACTER_COUNT) {
                mList.addAll(getMiscList(id))
            }
        }
    }

    override fun getItemCount(): Int = mList.size

    override fun getItemId(position: Int): Long = mList[position].ownerId.toLong()

    override fun onBindViewHolder(holder: MiscViewHolder?, position: Int) {
        holder?.bind(mList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MiscViewHolder
            = MiscViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.vh_comparison, parent, false))

    private fun getMiscList(id: Int): List<Misc> { // TODO: rewrite me :(
        val miscList: ArrayList<Misc> = ArrayList(1)
        val list: List<MiscAttribute> = StringQuery(MiscAttribute::class.java, "select a.id as \"id\", a.name as \"cName\", b.name as \"sName\", a.rank, a.ownerId, a.value from CharacterAttributeDatum a, SmashAttributeType b where a.smashAttributeTypeId = b.id and a.ownerId=$id and b.name='$mName'").queryList()
        val misc1 = Misc()
        misc1.ownerId = id
        misc1.name = list[0].sName!!
        when (mName) {
            "AIRDODGE" -> misc1.displayName = "Air Dodge"
            "ROLLS" -> misc1.displayName = "Forward/Back Roll"
            "LEDGEROLL" -> misc1.displayName = "Ledge Roll"
            "SPOTDODGE" -> misc1.displayName = "Spot Dodge"
        }
        misc1.intangibility = list[0].value!!
        misc1.faf = list[1].value!!
        miscList.add(misc1)

        return miscList
    }

    inner class MiscViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private val thumbnailIv = this.itemView?.findViewById(R.id.thumbnail) as ImageView
        private val nameTv = itemView?.findViewById(R.id.name) as TextView
        private val valueTv = itemView?.findViewById(R.id.value) as TextView

        fun bind(datum: Misc) {
            with(datum) {
                var thumbnailUrl = mThumbnailUrlMap.get(ownerId)
                if (thumbnailUrl == null) {
                    thumbnailUrl = Select().from(KHCharacter::class.java).where(KHCharacter_Table.id.eq(datum.ownerId)).querySingle()?.thumbnailUrl
                    mThumbnailUrlMap.put(ownerId, thumbnailUrl)
                }
                Picasso.with(thumbnailIv.context)
                        .load(thumbnailUrl)
                        .placeholder(R.drawable.placeholder)
                        .into(thumbnailIv)
                nameTv.text = displayName
                valueTv.text = itemView.context.getString(R.string.misc_data, datum.intangibility, datum.faf)
            }
        }
    }

    class Misc {
        var ownerId: Int = 0
        lateinit var name: String
        lateinit var displayName: String
        lateinit var intangibility: String
        lateinit var faf: String
    }
}