package org.mightyfrog.android.s4fd.details.tabcontents.miscs

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.raizlabs.android.dbflow.sql.queriable.StringQuery
import org.mightyfrog.android.s4fd.R
import org.mightyfrog.android.s4fd.data.MiscAttribute
import java.util.*

/**
 * @author Shigehiro Soejima
 */
class MiscsAdapter(id: Int, private val onItemClickListener: MiscsFragment.OnItemClickListener) : RecyclerView.Adapter<MiscsAdapter.MiscViewHolder>() {
    private val list: List<Misc>

    init {
        setHasStableIds(true)
        list = getMiscList(id)
    }

    override fun onBindViewHolder(holder: MiscViewHolder?, position: Int) {
        holder?.apply {
            bind(list[position])
        }
    }

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MiscViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.vh_misc, parent, false)
        val vh = MiscViewHolder(view)
        vh.itemView.setOnClickListener({
            val misc = list[vh.adapterPosition]
            onItemClickListener.onItemClick(misc.name, misc.ownerId)
        })
        return vh
    }

    override fun getItemId(position: Int) = list[position].id

    private fun getMiscList(id: Int): List<Misc> { // TODO: rewrite me :(
        val miscList = ArrayList<Misc>(4)
        var list = StringQuery(MiscAttribute::class.java, "select a.id as \"id\", a.name as \"cName\", b.name as \"sName\", a.rank, a.ownerId, a.value from CharacterAttributeDatum a, SmashAttributeType b where a.smashAttributeTypeId = b.id and a.ownerId=$id and b.name='AIRDODGE'").queryList()
        val airDodge = Misc()
        airDodge.id = 0L
        airDodge.ownerId = id
        airDodge.name = list[0].sName
        airDodge.displayName = "Air Dodge"
        airDodge.intangibility = list[0].value
        airDodge.faf = list[1].value
        miscList.add(airDodge)
        list = StringQuery(MiscAttribute::class.java, "select a.id as \"id\", a.name as \"cName\", b.name as \"sName\", a.rank, a.ownerId, a.value from CharacterAttributeDatum a, SmashAttributeType b where a.smashAttributeTypeId = b.id and a.ownerId=$id and b.name='SPOTDODGE'").queryList()
        val spotDodge = Misc()
        spotDodge.id = 1L
        spotDodge.ownerId = id
        spotDodge.name = list[0].sName
        spotDodge.displayName = "Spot Dodge"
        spotDodge.intangibility = list[0].value
        spotDodge.faf = list[1].value
        miscList.add(spotDodge)
        list = StringQuery(MiscAttribute::class.java, "select a.id as \"id\", a.name as \"cName\", b.name as \"sName\", a.rank, a.ownerId, a.value from CharacterAttributeDatum a, SmashAttributeType b where a.smashAttributeTypeId = b.id and a.ownerId=$id and b.name='ROLLS'").queryList()
        val forwardRoll = Misc() // greninja is the only one with different forward/back roll data
        forwardRoll.id = 2L
        forwardRoll.ownerId = id
        forwardRoll.name = list[0].sName
        forwardRoll.displayName = "Forward Roll"
        forwardRoll.intangibility = list[0].value
        forwardRoll.faf = list[1].value
        miscList.add(forwardRoll)
        if (list.size == 4) {
            val backRoll = Misc()
            backRoll.id = 2L
            backRoll.ownerId = id
            backRoll.name = list[2].sName
            backRoll.displayName = "Back Roll"
            backRoll.intangibility = list[2].value
            backRoll.faf = list[3].value
            miscList.add(backRoll)
        } else {
            val backRoll = Misc()
            backRoll.id = 2L
            backRoll.ownerId = id
            backRoll.name = list[0].sName
            backRoll.displayName = "Back Roll"
            backRoll.intangibility = list[0].value
            backRoll.faf = list[1].value
            miscList.add(backRoll)
        }
        list = StringQuery(MiscAttribute::class.java, "select a.id as \"id\", a.name as \"cName\", b.name as \"sName\", a.rank, a.ownerId, a.value from CharacterAttributeDatum a, SmashAttributeType b where a.smashAttributeTypeId = b.id and a.ownerId=$id and b.name='LEDGEROLL'").queryList()
        val ledgeRoll = Misc()
        ledgeRoll.id = 4L
        ledgeRoll.ownerId = id
        ledgeRoll.name = list[0].sName
        ledgeRoll.displayName = "Ledge Roll"
        ledgeRoll.intangibility = list[0].value
        ledgeRoll.faf = list[1].value
        miscList.add(ledgeRoll)

        return miscList
    }

    class MiscViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTv = itemView.findViewById<TextView>(R.id.name)
        private val valueTv = itemView.findViewById<TextView>(R.id.value)

        fun bind(datum: Misc) {
            datum.apply {
                nameTv.text = displayName
                valueTv.text = itemView.context.getString(R.string.misc_data, intangibility, faf)
            }
        }
    }

    class Misc {
        var id: Long = 0L
        var ownerId: Int = 0
        var name: String? = "N/A"
        var displayName: String? = "N/A"
        var intangibility: String? = "N/A"
        var faf: String? = "N/A"
    }
}