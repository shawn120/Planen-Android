package com.example.planmanager.ui.Today

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planmanager.R
import com.example.planmanager.data.AppDatabase
import com.example.planmanager.data.TaskItem
import com.example.planmanager.data.TaskItemLocalRepository
import com.example.planmanager.ui.AddPlan.AddPlanDialog
import com.example.planmanager.ui.TaskViewModel
import com.example.planmanager.util.TaskType
import com.google.android.material.snackbar.Snackbar

class TodayFragment : Fragment(R.layout.fragment_today), TodayAdapter.OnTodoCheckboxChangedListener{
    private val adapter = TodayAdapter(::onTaskCardClick,::onTodoCheckboxChanged )
    val viewModel: TaskViewModel by viewModels()
    private lateinit var taskListRV: RecyclerView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskListRV = view.findViewById(R.id.rv_task_list)
        taskListRV.layoutManager = LinearLayoutManager(requireContext())
        taskListRV.setHasFixedSize(true)
        taskListRV.adapter = adapter

        viewModel.taskItemLocalsToday.observe(viewLifecycleOwner) {taskItemLocalsList ->
            Log.d("Today Fragment", "today fragment new item: {$taskItemLocalsList}")
            adapter.updateTasks(taskItemLocalsList)
            taskListRV.scrollToPosition(0)
        }

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition // int
                val deletedItem = adapter.deleteTask(position)
                val rootView = view.findViewById<View>(R.id.rv_task_list)
                val snackbar = rootView?.let {
                    Snackbar.make(
                        it,
                        "Delete \"${deletedItem.title}\" ?",
                        Snackbar.LENGTH_SHORT
                    )
                }
                snackbar?.setAction("Confirm") {
                    viewModel.deleteTask(deletedItem)
                }
                snackbar?.addCallback(object : Snackbar.Callback() {
                    override fun onShown(sb: Snackbar?) {
                        adapter.addTask(deletedItem, position)
                    }
                })
                snackbar?.show()
            }
        }

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(taskListRV)

    }

    private fun onTaskCardClick(task: TaskItem) {
        Log.d("TODAYFRAGMENT", "${task.title} is being clicked")
// input id into this dialog
        val dialog = AddPlanDialog()
        dialog.show(requireFragmentManager(), "add_plan_dialog")
    }

    override fun onTodoCheckboxChanged(taskId: String, isChecked: Boolean) {
        Log.d("lookhere","today checkbox change-- $taskId")
        viewModel.updateTodoCompletion(taskId, isChecked)

    }
}