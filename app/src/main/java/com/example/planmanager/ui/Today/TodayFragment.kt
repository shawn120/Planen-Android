package com.example.planmanager.ui.Today

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planmanager.R
import com.example.planmanager.data.TaskItem
import com.example.planmanager.ui.AddPlan.AddPlanDialog
import com.example.planmanager.ui.TaskViewModel
import com.example.planmanager.util.TaskType

class TodayFragment : Fragment(R.layout.fragment_today) {
    private val adapter = TodayAdapter(::onTaskCardClick)
    val viewModel: TaskViewModel by viewModels()
    private lateinit var taskListRV: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskListRV = view.findViewById(R.id.rv_task_list)
        taskListRV.layoutManager = LinearLayoutManager(requireContext())
        taskListRV.setHasFixedSize(true)
        taskListRV.adapter = adapter

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.activity_main_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {

                        R.id.action_add_plan -> {
                            val dialog = AddPlanDialog(this@TodayFragment)
                            dialog.show(requireFragmentManager(), "add_plan_dialog")
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.STARTED
        )
        viewModel.taskItems.observe(viewLifecycleOwner) { taskItems ->
            Log.d("LookAtHere", "todayfragment new item: {$taskItems}")
            if (taskItems != null) {
                adapter.updateTasks(taskItems)
            }
            taskListRV.scrollToPosition(0)
        }

    }

    private fun onTaskCardClick(task: TaskItem) {
        if (task.taskType ==TaskType.DEADLINE) {
            Log.d("TODOFRAGMENT", "a ${task.deadline?.title} is being clicked")
        } else {
            Log.d("TODOFRAGMENT", "a ${task.todo?.text} is being clicked")
        }
//        val dialog = AddPlanDialog(this@TodayFragment)
//        dialog.show(requireFragmentManager(), "add_plan_dialog")
    }
}