package com.example.medicalcounter.ui.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.motion.widget.Key.VISIBILITY
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalcounter.R
import com.example.medicalcounter.model.db.dataClass.Counter
import java.util.*

class HomeAdapter(private val clickListener:(Counter,Int)->Unit)
    : ListAdapter<Counter, HomeAdapter.CountersViewHolder>(CountersDiffUnit()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountersViewHolder {
        val viewToClient = LayoutInflater.from(parent.context).inflate(R.layout.fragment_home_item, parent, false)
        return CountersViewHolder(viewToClient, clickListener)
    }

    override fun onBindViewHolder(holder: CountersViewHolder, position: Int) {
        getItem(position)?.let { counter ->
            holder.bind(counter)
        }
    }
    class CountersViewHolder(view: View, private val clickListener: (Counter, Int) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bind(counters: Counter) {
            val tv_organization = itemView.findViewById<TextView>(R.id.text_view_organization_home)
            val tv_edicine = itemView.findViewById<TextView>(R.id.text_view_medicine_home)
            val tv_count = itemView.findViewById<TextView>(R.id.text_view_medicine_count_home)
            val buttonAdd = itemView.findViewById<ImageButton>(R.id.imageView_add_home)
            val buttonMinus = itemView.findViewById<ImageButton>(R.id.imageView_minus_home)
            tv_organization.text = counters.organizationName
            tv_edicine.text = counters.medicineTitle
            tv_count.text = counters.coun.toString()
            if(counters.coun==0){
                buttonMinus.visibility=INVISIBLE
            }else{
                buttonMinus.visibility=VISIBLE
            }
            buttonAdd.setOnClickListener { clickListener(counters, 1) }
            buttonMinus.setOnClickListener { clickListener(counters, 2) }


        }
    }
    class CountersDiffUnit : DiffUtil.ItemCallback<Counter>() {
        override fun areItemsTheSame(oldItem: Counter, newItem: Counter): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Counter, newItem: Counter): Boolean {
            return oldItem == newItem
        }
    }
}