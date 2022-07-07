package com.example.medicalcounter.ui.medicine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalcounter.R
import com.example.medicalcounter.model.db.dataClass.Medicine

class MedicineAdapter (private val clickListener:(Medicine, Int)->Unit)
    : ListAdapter<Medicine, MedicineAdapter.MedicinesViewHolder>(MedicinesDiffUnit()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicinesViewHolder {
        val viewToClient = LayoutInflater.from(parent.context).inflate(R.layout.fragment_medicine_item, parent, false)
        return MedicinesViewHolder(viewToClient, clickListener)
    }

    override fun onBindViewHolder(holder: MedicinesViewHolder, position: Int) {
        getItem(position)?.let { medicine ->
            holder.bind(medicine)
        }
    }
    class MedicinesViewHolder(view: View, private val clickListener: (Medicine, Int) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bind(medicine: Medicine) {
            val tv_organization = itemView.findViewById<TextView>(R.id.text_view_organization)
            val tv_edicine = itemView.findViewById<TextView>(R.id.text_view_medicine)
            val button = itemView.findViewById<ImageButton>(R.id.imageButton_medicine_edit)
            tv_organization.text=medicine.organizationName
            tv_edicine.text=medicine.title
            button.setOnClickListener { clickListener(medicine,1)}




        }

    }

    class MedicinesDiffUnit : DiffUtil.ItemCallback<Medicine>() {
        override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
            return oldItem == newItem
        }
    }
}