package com.goot.gootdistri.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.argos.com.argosapp.Interfaces.AdapterCallbackRobot
import app.argos.com.argosapp.Model.Robot
import app.argos.com.argosapp.R
import kotlinx.android.synthetic.main.item_robot.view.*
import java.util.*

class RobotAdapter(private val mContext: Context, private val mAdapterCallbackRobot: AdapterCallbackRobot) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private var listRobots: MutableList<Robot> = ArrayList()
    private lateinit var mListener: ItemClickCallback
    internal lateinit var inflater: LayoutInflater

    interface ItemClickCallback { fun onItemClick(position: Int) }
    fun setOnItemClickListener(itemClickCallback: ItemClickCallback) {
        mListener = itemClickCallback
    }

    fun setData(data: MutableList<Robot>) {
        listRobots = data
        notifyDataSetChanged()
    }

    fun addItem(item: Robot){
        listRobots.add(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rowView: View
        rowView = LayoutInflater.from(mContext).inflate(R.layout.item_robot, parent, false)
        return ViewHolder(rowView)

    }

    override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {
        h.itemView.setOnClickListener { mListener.onItemClick(h.adapterPosition) }

        val robot = listRobots[position]
        h.itemView.name.text = robot.name
        h.itemView.model.text = robot.model
        h.itemView.item.setOnClickListener{mAdapterCallbackRobot.onClickItem(robot)}

    }

    override fun getItemCount(): Int {
        return listRobots.size
    }

}
