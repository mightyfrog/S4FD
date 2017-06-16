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
import java.util.*

/**
 * @author Shigehiro Soejima
 */
class CharacterAdapter(var mList: List<KHCharacter>, val mListener: MainActivity.OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        val MODE_LINEAR = 1
        val MODE_GRID = 2
    }

    private var mMode = MODE_LINEAR

    private val mValueMap = SparseArray<String>()

    init {
        setHasStableIds(true)
    }

    override fun getItemCount(): Int = mList.size

    override fun getItemId(position: Int): Long = mList[position].id.toLong()

    override fun getItemViewType(position: Int): Int = mMode

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (mMode) {
            MODE_LINEAR -> {
                (holder as LinearViewHolder).bind(mList[position])
            }
            MODE_GRID -> {
                (holder as GridViewHolder).bind(mList[position])
            }
        }
    }

    private var mLastAdapterPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        when (mMode) {
            MODE_LINEAR -> {
                val vh = LinearViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.vh_main_linear, parent, false))
                vh.itemView.setOnClickListener {
                    val pos = vh.adapterPosition
                    if (mLastAdapterPosition != pos) {
                        mLastAdapterPosition = pos
                        mListener.onItemClick(mList[pos].id, pos)
                    }
                }
                return vh
            }
            MODE_GRID -> {
                val vh = GridViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.vh_main_grid, parent, false))
                vh.itemView.setOnClickListener {
                    val pos = vh.adapterPosition
                    if (mLastAdapterPosition != pos) {
                        mLastAdapterPosition = pos
                        mListener.onItemClick(mList[pos].id, pos)
                    }
                }
                return vh
            }
        }

        throw IllegalStateException("unknown view mode")
    }

    fun setMode(mode: Int) {
        mMode = mode
        notifyDataSetChanged()
    }

    fun update(list: List<KHCharacter>?) {
        list ?: return

        mList = list
        notifyDataSetChanged()
    }

    fun reverse() {
        Collections.reverse(mList)
        notifyDataSetChanged()
    }

    fun sort(sortBy: Int?) {
        when (sortBy) {
            R.id.sort_by_name -> {
                sortByName()
            }
            R.id.sort_by_weight -> {
                sortInt("Weight")
            }
            R.id.sort_by_run_speed -> {
                sortFloat("Run Speed")
            }
            R.id.sort_by_walk_speed -> {
                sortFloat("Walk Speed")
            }
            R.id.sort_by_max_jumps -> {
                sortInt("Max Jumps")
            }
            R.id.sort_by_wall_cling -> {
                sortString("Wall Cling")
            }
            R.id.sort_by_wall_jump -> {
                sortString("Wall Jump")
            }
            R.id.sort_by_air_speed -> {
                sortFloat("Air Speed")
            }
            R.id.sort_by_crawl -> {
                sortString("Crawl")
            }
            R.id.sort_by_tether -> {
                sortString("Tether")
            }
            R.id.sort_by_jumpsquat -> {
                sortString("Jumpsquat")
            }
            R.id.sort_by_air_acceleration -> {
                sortFloat("Air Acceleration")
            }
            R.id.sort_by_soft_landing_lag -> {
                sortString("Soft Landing Lag")
            }
            R.id.sort_by_hard_landing_lag -> {
                sortString("Hard Landing Lag")
            }
            R.id.sort_by_gravity -> {
                sortFloat("Gravity")
            }
            R.id.sort_by_fall_speed -> {
                sortFloat("Fall Speed")
            }
            R.id.sort_by_fast_fall_speed -> {
                sortFloat("Fast Fall Speed")
            }
            R.id.sort_by_sh_air_time -> {
                sortString("SH Air Time")
            }
            R.id.sort_by_fh_air_time -> {
                sortString("FH Air Time")
            }
        }
        mListener.onSorted()
        notifyDataSetChanged()
    }

    fun clearLastAdapterPosition() {
        mLastAdapterPosition = -1
    }

    private fun sortInt(by: String) {
        val list = Select().from(MovementDatum::class.java).where(MovementDatum_Table.name.eq(by)).queryList()
        Collections.sort(list, { lhs, rhs -> lhs.value!!.compareTo(rhs.value!!) })
        val map = SparseIntArray(list.size)
        for (datum in list) {
            map.put(datum.ownerId, datum.value!!.toInt())
            mValueMap.put(datum.ownerId, datum.value)
        }
        Collections.sort(mList, { lhs, rhs -> map.get(rhs.id).compareTo(map.get(lhs.id)) })
    }

    private fun sortFloat(by: String) {
        val list = Select().from(MovementDatum::class.java).where(MovementDatum_Table.name.eq(by)).queryList()
        Collections.sort(list, { lhs, rhs -> lhs.value!!.compareTo(rhs.value!!) })
        val map = SparseArray<Float>(list.size)
        for (datum in list) {
            map.put(datum.ownerId, datum.value!!.toFloat())
            mValueMap.put(datum.ownerId, datum.value)
        }
        Collections.sort(mList, { lhs, rhs -> map.get(rhs.id).compareTo(map.get(lhs.id)) })
    }

    private fun sortString(by: String) {
        val list = Select().from(MovementDatum::class.java).where(MovementDatum_Table.name.eq(by)).queryList()
        Collections.sort(list, { lhs, rhs -> lhs.value!!.compareTo(rhs.value!!) })
        val map = SparseArray<String>(list.size)
        for (datum in list) {
            map.put(datum.ownerId, datum.value)
            mValueMap.put(datum.ownerId, datum.value?.replace("frame", " frame"))
        }
        sortByName()
        Collections.sort(mList, { lhs, rhs ->
            map.get(rhs.id).compareTo(map.get(lhs.id))
        })
    }

    private fun sortByName() {
        Collections.sort(mList, { lhs, rhs -> lhs.name!!.compareTo(rhs.name!!) })
    }

    inner class LinearViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private val thumbnailIv = itemView?.findViewById(R.id.thumbnail) as ImageView
        private val nameTv = itemView?.findViewById(R.id.name) as TextView
        private val valueTv = itemView?.findViewById(R.id.value) as TextView

        fun bind(character: KHCharacter) {
            Picasso.with(itemView.context)
                    .load(character.thumbnailUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(thumbnailIv)
            nameTv.text = character.displayName?.trim()
            val value = mValueMap.get(character.id)
            if (value == null) {
                valueTv.visibility = View.GONE
            } else {
                valueTv.visibility = View.VISIBLE
                valueTv.text = value
            }
        }
    }

    inner class GridViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private val thumbnailIv = itemView?.findViewById(R.id.thumbnail) as ImageView
        private val valueTv = itemView?.findViewById(R.id.value) as TextView

        fun bind(character: KHCharacter) {
            Picasso.with(itemView.context)
                    .load(character.thumbnailUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(thumbnailIv)
            val value = mValueMap.get(character.id)
            if (value == null) {
                valueTv.visibility = View.GONE
            } else {
                valueTv.visibility = View.VISIBLE
                valueTv.text = value
            }
        }
    }
}