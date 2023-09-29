package com.example.greatweek.ui.screens.roles

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.greatweek.R
import com.example.greatweek.App
import com.example.greatweek.ui.ViewModelFactory
import com.example.greatweek.ui.collectFlowSafely
import com.example.greatweek.ui.screens.goaldialog.GoalDialogFragment
import com.example.greatweek.ui.screens.roledialog.RoleDialogFragment
import com.example.greatweek.ui.screens.schedule.ScheduleFragment
import com.example.greatweek.ui.screens.schedule.goals.GoalCallback
import com.example.greatweek.databinding.FragmentRolesBinding
import com.example.greatweek.domain.model.Role
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class RolesFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: RolesViewModel by viewModels { viewModelFactory }
    private lateinit var binding: FragmentRolesBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val bottomSheetCallback = object : BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_COLLAPSED -> viewModel.accept(RolesEvent.Collapse)
                BottomSheetBehavior.STATE_EXPANDED -> viewModel.accept(RolesEvent.Expand)
                else -> Unit
            }
        }
    }

    private val goalCallback = object : GoalCallback {
        override fun onCompleteClick(goalId: Int) {
            viewModel.accept(RolesEvent.CompleteGoal(goalId))
        }
        override fun onClick(goalId: Int) {
            openEditGoalDialog(goalId)
        }
    }

    private val roleCallback = object : RoleCallback {
        override fun onMoreClick(v: View, context: Context, role: Role) {
            popupMenus(v, context, role)
        }
        override fun onAddGoalClick(role: String) {
            openAddGoalDialog(role)
        }
    }

    var flag = false
    private val dragListener = View.OnDragListener { _, event ->
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                viewModel.accept(RolesEvent.Collapse)
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                val clipDescription = event.clipDescription.label.toString()
                if (clipDescription == "null")
                    flag = true
                viewModel.accept(RolesEvent.Expand)
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                if (flag) viewModel.accept(RolesEvent.Expand)
                flag = false
                true
            }
            else -> false
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRolesBinding.inflate(inflater, container, false)
        registerForContextMenu(binding.rolesRecyclerView)

        bottomSheetBehavior = BottomSheetBehavior.from((parentFragment as ScheduleFragment).binding.bottomSheetLayout.root)
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

        binding.root.setOnDragListener(dragListener)

        return binding.root
    }

    private val highlightColor by lazy { ContextCompat.getColor(requireContext(), R.color.highlight) }
    private val baseColor by lazy { ContextCompat.getColor(requireContext(), R.color.grey_dark) }

    private val roleDragListenerCallback = object : RoleDragListenerCallback {
        override fun onDragEntered(view: View): Boolean {
            viewModel.accept(RolesEvent.Expand)
            animateViewColor(view = view, colorFrom = baseColor, colorTo = highlightColor)
            return true
        }

        override fun onDragExited(view: View): Boolean {
            animateViewColor(view = view, colorFrom = highlightColor, colorTo = baseColor)
            return true
        }

        override fun onDrop(view: View, event: DragEvent, role: String): Boolean {
            animateViewColor(view = view, colorFrom = highlightColor, colorTo = baseColor)
            val item = event.clipData.getItemAt(0)
            val goalId = item.text.toString().toInt()
            viewModel.accept(RolesEvent.GoalDrop(goalId, role))
            return true
        }

        override fun onDragEnded(view: View, event: DragEvent): Boolean {
            view.setBackgroundColor(Color.TRANSPARENT)
            if (!event.result)
                (event.localState as View).visibility = View.VISIBLE
            return true
        }

    }

    private val roleAdapter = RoleAdapter(
        goalCallback = goalCallback,
        roleCallback = roleCallback,
        roleDragListenerCallback = roleDragListenerCallback,
    )

    private fun animateViewColor(view: View, colorFrom: Int, colorTo: Int) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 250
        colorAnimation.addUpdateListener { animator ->
            view.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()
    }

    private fun popupMenus(v: View, context: Context, role: Role) {
        val popupMenus = PopupMenu(context, v)
        popupMenus.inflate(R.menu.role_menu)

        popupMenus.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit -> {
                    openRenameRoleDialog(role)
                    true
                }
                R.id.delete -> {
                    if (role.goals.isNotEmpty())
                        showRoleWarning(context)
                    else
                        viewModel.accept(RolesEvent.DeleteRole(role.name))
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

    private fun showRoleWarning(context: Context) {
        Toast.makeText(context, "Can't delete role with active goals", Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.roleTabFragment = this@RolesFragment
        binding.rolesRecyclerView.adapter = roleAdapter
        viewModel.rolesState.render()
    }

    private fun StateFlow<RolesState>.render() = collectFlowSafely {
        collect {
            roleAdapter.submitList(it.roles)
            bottomSheetBehavior.state = it.expanded
        }
    }

    /**
     * Show dialog
     */

    private fun openEditGoalDialog(goalId: Int) =
        GoalDialogFragment.showForGoal(
            manager = parentFragmentManager,
            goalId = goalId,
        )

    private fun openAddGoalDialog(role: String) =
        GoalDialogFragment.showForRole(
            manager = parentFragmentManager,
            role = role,
        )

    private fun openRenameRoleDialog(role: Role) =
        RoleDialogFragment.showForEdit(
            manager = parentFragmentManager,
            role = role.name
        )

    fun openAddRoleDialog() =
        RoleDialogFragment.showForNew(manager = parentFragmentManager)
}