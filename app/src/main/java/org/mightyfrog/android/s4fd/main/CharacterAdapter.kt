package org.mightyfrog.android.s4fd.main

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.raizlabs.android.dbflow.sql.language.Select
import com.squareup.picasso.Picasso
import org.mightyfrog.android.s4fd.R
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.data.MovementDatum
import org.mightyfrog.android.s4fd.data.MovementDatum_Table

/**
 * @author Shigehiro Soejima
 */
class CharacterAdapter(private var list: List<KHCharacter>, private val listener: MainActivity.OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val MODE_LINEAR = 1
        const val MODE_GRID = 2
    }

    private var mode = MODE_LINEAR

    private val valueMap = SparseArray<String>()

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = list[position].id.toLong()

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int = mode

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (mode) {
            MODE_LINEAR -> (holder as LinearViewHolder).bind(list[position])
            MODE_GRID -> (holder as GridViewHolder).bind(list[position])
        }
    }

    private var lastAdapterPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (mode) {
            MODE_LINEAR -> {
                val vh = LinearViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.vh_main_linear, parent, false))
                vh.itemView.setOnClickListener {
                    val pos = vh.adapterPosition
                    if (lastAdapterPosition != pos) {
                        lastAdapterPosition = pos
                        listener.onItemClick(list[pos].id, pos)
                    }
                }
                return vh
            }
            MODE_GRID -> {
                val vh = GridViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.vh_main_grid, parent, false))
                vh.itemView.setOnClickListener {
                    val pos = vh.adapterPosition
                    if (lastAdapterPosition != pos) {
                        lastAdapterPosition = pos
                        listener.onItemClick(list[pos].id, pos)
                    }
                }
                return vh
            }
        }

        throw IllegalStateException("unknown view mode")
    }

    fun setMode(mode: Int) {
        this.mode = mode
        notifyDataSetChanged()
    }

    fun update(list: List<KHCharacter>?) {
        list ?: return

        this.list = list
        notifyDataSetChanged()
    }

    fun reverse() {
        list = list.reversed()
        notifyDataSetChanged()
    }

    fun sort(sortBy: Int?) {
        when (sortBy) {
            R.id.sort_by_name -> sortByName()
            R.id.sort_by_weight -> sortInt("Weight")
            R.id.sort_by_run_speed -> sortFloat("Run Speed")
            R.id.sort_by_walk_speed -> sortFloat("Walk Speed")
            R.id.sort_by_max_jumps -> sortInt("Max Jumps")
            R.id.sort_by_wall_cling -> sortString("Wall Cling")
            R.id.sort_by_wall_jump -> sortString("Wall Jump")
            R.id.sort_by_air_speed -> sortFloat("Air Speed")
            R.id.sort_by_crawl -> sortString("Crawl")
            R.id.sort_by_tether -> sortString("Tether")
            R.id.sort_by_jumpsquat -> sortString("Jumpsquat")
            R.id.sort_by_air_acceleration -> sortFloat("Air Acceleration")
            R.id.sort_by_soft_landing_lag -> sortString("Soft Landing Lag")
            R.id.sort_by_hard_landing_lag -> sortString("Hard Landing Lag")
            R.id.sort_by_gravity -> sortFloat("Gravity")
            R.id.sort_by_fall_speed -> sortFloat("Fall Speed")
            R.id.sort_by_fast_fall_speed -> sortFloat("Fast Fall Speed")
            R.id.sort_by_sh_air_time -> sortString("SH Air Time")
            R.id.sort_by_fh_air_time -> sortString("FH Air Time")
        }
        listener.onSorted()
        notifyDataSetChanged()
    }

    fun clearLastAdapterPosition() {
        lastAdapterPosition = -1
    }

    private fun sortInt(by: String) {
        var list = Select().from(MovementDatum::class.java).where(MovementDatum_Table.name.eq(by)).queryList()
        list = list.sortedBy { it.value }
        val map = SparseIntArray(list.size)
        for (datum in list) {
            map.put(datum.ownerId, datum.value!!.toInt())
            valueMap.put(datum.ownerId, datum.value)
        }
        this.list = this.list.sortedByDescending { map[it.id] }
    }

    private fun sortFloat(by: String) {
        var list = Select().from(MovementDatum::class.java).where(MovementDatum_Table.name.eq(by)).queryList()
        list = list.sortedBy { it.value }
        val map = SparseArray<Float>(list.size)
        for (datum in list) {
            map.put(datum.ownerId, datum.value!!.toFloat())
            valueMap.put(datum.ownerId, datum.value)
        }
        this.list = this.list.sortedByDescending { map[it.id] }
    }

    private fun sortString(by: String) {
        var list = Select().from(MovementDatum::class.java).where(MovementDatum_Table.name.eq(by)).queryList()
        list = list.sortedBy { it.value }
        val map = SparseArray<String>(list.size)
        for (datum in list) {
            map.put(datum.ownerId, datum.value)
            valueMap.put(datum.ownerId, datum.value?.replace("frame", " frame"))
        }
        sortByName()
        this.list = this.list.sortedBy { map[it.id] }
    }

    private fun sortByName() {
        list = list.sortedBy { it.name }
    }

    inner class LinearViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbnailIv = itemView.findViewById<ImageView>(R.id.thumbnail)
        private val nameTv = itemView.findViewById<TextView>(R.id.name)
        private val valueTv = itemView.findViewById<TextView>(R.id.value)

        fun bind(character: KHCharacter) {
            Picasso.with(thumbnailIv.context)
                    .load(character.thumbnailUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(thumbnailIv)
            nameTv.text = character.displayName?.trim()
            valueMap.get(character.id)?.let { char ->
                valueTv.visibility = View.VISIBLE
                valueTv.text = char
            } ?: run {
                valueTv.visibility = View.GONE
            }
        }
    }

    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbnailIv = itemView.findViewById<ImageView>(R.id.thumbnail)
        private val valueTv = itemView.findViewById<TextView>(R.id.value)

        fun bind(character: KHCharacter) {
            Picasso.with(thumbnailIv.context)
                    .load(character.thumbnailUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(thumbnailIv)
            valueMap.get(character.id)?.let { char ->
                valueTv.visibility = View.VISIBLE
                valueTv.text = char
            } ?: run {
                valueTv.visibility = View.GONE
            }
        }
    }
}