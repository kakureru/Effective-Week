package com.example.greatweek.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.greatweek.R
import com.example.greatweek.databinding.RoleCardLayoutBinding
import com.example.greatweek.domain.model.Role

class RoleAdapter(
    private val renameRole: (role: Role) -> Unit,
    private val deleteRole: (roleId: Int) -> Unit
) : ListAdapter<Role, RoleAdapter.RoleViewHolder>(DiffCallback) {

    inner class RoleViewHolder(
        private val context: Context,
        private val binding: RoleCardLayoutBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(role: Role) {
            binding.roleTextView.text = role.name
            val goalAdapter = GoalAdapter()
            binding.goalsRecyclerView.adapter = goalAdapter
            goalAdapter.submitList(role.goals)
            binding.moreButton.setOnClickListener { popupMenus(it, context, role) }
        }
    }

    private fun popupMenus(v: View, context: Context, role: Role) {
        val popupMenus = PopupMenu(context, v)
        popupMenus.inflate(R.menu.role_menu)
        popupMenus.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit -> {
                    renameRole(role)
                    true
                }
                R.id.delete -> {
                    deleteRole(role.id)
                    true
                }
                else -> true
            }

        }
        popupMenus.show()
        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(popupMenus)
        menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
            .invoke(menu, true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleAdapter.RoleViewHolder {
        return RoleViewHolder(
            parent.context,
            RoleCardLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RoleAdapter.RoleViewHolder, position: Int) {
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