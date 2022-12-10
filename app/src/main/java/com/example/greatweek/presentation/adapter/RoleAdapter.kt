package com.example.greatweek.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.greatweek.databinding.RoleCardLayoutBinding
import com.example.greatweek.domain.model.Role

class RoleAdapter
    : ListAdapter<Role, RoleAdapter.RoleViewHolder>(DiffCallback) {

    class RoleViewHolder(private val binding: RoleCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(role: Role) {
            binding.roleTextView.text = role.name
            val goalAdapter = GoalAdapter()
            binding.goalsRecyclerView.adapter = goalAdapter
            goalAdapter.submitList(role.goals)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        return RoleViewHolder(
            RoleCardLayoutBinding.inflate(
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
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Role, newItem: Role): Boolean {
                return oldItem == newItem
            }

        }
    }
}