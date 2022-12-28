package com.example.greatweek.app.presentation.adapter

import android.content.ClipDescription
import android.content.Context
import android.graphics.Color
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.greatweek.R
import com.example.greatweek.databinding.RoleCardLayoutBinding
import com.example.greatweek.domain.model.Role

class RoleAdapter(
    private val renameRole: (role: Role) -> Unit,
    private val deleteRole: (role: String) -> Unit,
    private val addGoal: (role: String) -> Unit,
    private val completeGoal: (goalId: Int) -> Unit,
    private val editGoal: (goalId: Int) -> Unit,
    private val dropGoal: (goalId: Int, role: String) -> Unit,
    private val expandBottomSheet: () -> Unit
) : ListAdapter<Role, RoleAdapter.RoleViewHolder>(DiffCallback) {

    inner class RoleViewHolder(
        private val context: Context,
        private val binding: RoleCardLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dragListener = View.OnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    expandBottomSheet()
                    view.setBackgroundColor(Color.GRAY)
                    true
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    view.setBackgroundColor(Color.TRANSPARENT)
                    true
                }
                DragEvent.ACTION_DROP -> {
                    val item = event.clipData.getItemAt(0)
                    val goalId = item.text.toString().toInt()
                    val roleId = getItem(adapterPosition).name
                    dropGoal(goalId, roleId)
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    view.setBackgroundColor(Color.TRANSPARENT)
                    val v = event.localState as View
                    v.visibility = View.VISIBLE
                    //view.invalidate()
                    true
                }
                else -> false
            }
        }

        fun bind(role: Role) {
            val goalList = role.goals.filter { it.weekday == 0 }

            val goalAdapter = GoalAdapter(completeGoal, editGoal)

            binding.apply {
                // View
                roleTextView.text = role.name
                goalDropTarget.visibility = if (goalList.isEmpty()) View.VISIBLE else View.GONE
                // Adapter
                goalsRecyclerView.adapter = goalAdapter
                // On drag listener
                goalsRecyclerView.setOnDragListener(dragListener)
                goalDropTarget.setOnDragListener(dragListener)
                // On click listener
                moreButton.setOnClickListener { popupMenus(it, context, role) }
                addGoalButton.setOnClickListener { addGoal(role.name) }
            }

            goalAdapter.submitList(goalList)
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
                    if (role.goals.isNotEmpty())
                        Toast.makeText(
                            context,
                            "Can't delete role with active goals",
                            Toast.LENGTH_SHORT
                        ).show()
                    else
                        deleteRole(role.name)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        return RoleViewHolder(
            parent.context,
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
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Role, newItem: Role): Boolean {
                return oldItem == newItem
            }

        }
    }
}