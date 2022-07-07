package com.example.medicalcounter.ui.organization.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalcounter.R
import com.example.medicalcounter.model.db.dataClass.Organization

class OrganizationAdapter (private val clickListener:(Organization, Int)->Unit)
: ListAdapter<Organization, OrganizationAdapter.OrganizationsViewHolder>(OrganizationsDiffUnit()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationsViewHolder {
        val viewToClient = LayoutInflater.from(parent.context).inflate(R.layout.fragment_organization_item, parent, false)
        return OrganizationsViewHolder(viewToClient, clickListener)
    }

    override fun onBindViewHolder(holder: OrganizationsViewHolder, position: Int) {
        getItem(position)?.let { organization ->
            holder.bind(organization)
        }
    }
    class OrganizationsViewHolder(view: View, private val clickListener: (Organization, Int) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bind(organization: Organization) {
            val tv_organization = itemView.findViewById<TextView>(R.id.text_view_organization_organization)
            val tv_agent = itemView.findViewById<TextView>(R.id.text_view_organization_agent)
            val tv_phone = itemView.findViewById<TextView>(R.id.text_view_organization_phone)
            val button = itemView.findViewById<ImageButton>(R.id.imageButton_organization_edit)
            tv_organization.text=organization.title
            tv_agent.text=organization.agent
            tv_phone.text=organization.phone
            button.setOnClickListener { clickListener(organization,1)}
            tv_phone.setOnClickListener { clickListener(organization,2)}
        }

    }

    class OrganizationsDiffUnit : DiffUtil.ItemCallback<Organization>() {
        override fun areItemsTheSame(oldItem: Organization, newItem: Organization): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Organization, newItem: Organization): Boolean {
            return oldItem == newItem
        }
    }
}