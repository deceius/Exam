package com.decena.exam

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.decena.exam.entities.BoxEntity

class InventoryAdapter(mContext: Context, var mData: ArrayList<List<BoxEntity>>? = arrayListOf()): RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {

    private var mInflater: LayoutInflater? = null
    init {
        mInflater = LayoutInflater.from(mContext)
    }
    fun setData(data: ArrayList<List<BoxEntity>>){
        mData = data
    }
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val boxName: TextView = itemView.findViewById<TextView>(R.id.boxName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater!!.inflate(R.layout.item_box, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mData?.let {
            holder.boxName.text = it[position].toString()
        }
    }

    override fun getItemCount(): Int {
        mData?.let{
            return it.size
        }
        return 0
    }

}