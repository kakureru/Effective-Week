package com.example.greatweek.app.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.greatweek.databinding.RoleLayoutBinding
import com.example.greatweek.domain.model.Role

class RoleBottomSheetDialogAdapter(
    private val onItemClick: (role: Role) -> Unit
) :
    ListAdapter<Role, RoleBottomSheetDialogAdapter.RoleViewHolder>(DiffCallback) {
    inner class RoleViewHolder(private val binding: RoleLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(role: Role) {
            binding.roleTextView.text = role.name
            binding.root.setOnClickListener {
                onItemClick(role)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        return RoleViewHolder(
            RoleLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Role>() {
            override fun areItemsTheSame(oldItem: Role, newItem: Role): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Role, newItem: Role): Boolean {
                return oldItem == newItem
            }

        }
    }
}