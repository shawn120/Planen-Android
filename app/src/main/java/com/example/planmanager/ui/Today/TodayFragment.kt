package com.example.planmanager.ui.Today

import android.content.Intent
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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planmanager.R
import com.example.planmanager.data.TaskItem
import com.example.planmanager.ui.AddPlan.AddPlanDialog
import com.example.planmanager.ui.TaskViewModel
import com.example.planmanager.util.TaskType
import com.google.android.material.snackbar.Snackbar
import java.util.UUID

class TodayFragment : Fragment(R.layout.fragment_today){
    private val adapter = TodayAdapter(::onTaskCardClick,::onTodoCheckboxChanged )
    val viewModel: TaskViewModel by viewModels()
    private lateinit var taskListRV: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskListRV = view.findViewById(R.id.rv_task_list)
        taskListRV.layoutManager = LinearLayoutManager(requireContext())
        taskListRV.setHasFixedSize(true)
        taskListRV.adapter = adapter

        viewModel.taskItemLocalsTodayWithRange.observe(viewLifecycleOwner) {taskItemLocalsList ->
            Log.d("Today Fragment", "today fragment new item: {$taskItemLocalsList}")
            adapter.updateTasks(taskItemLocalsList)
            taskListRV.scrollToPosition(0)
        }

        viewModel.loadHoliday(
            "2024",
            "US"
        )

        viewModel.apiResult.observe(viewLifecycleOwner) { holidays ->
            Log.d("HOLIDAY", "insideHOLIDAY: $holidays")
            if (holidays != null) {
                viewModel.updateHoliday(holidays)
            }
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
                        adapter.addTaskBackToListOnly(deletedItem, position)
                    }
                })
                snackbar?.show()
            }
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.activity_main_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {

                        R.id.action_share -> {
                            shareTask()
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.STARTED
        )

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(taskListRV)

    }

    private fun onTaskCardClick(task: TaskItem) {
        Log.d("TODAYFRAGMENT", "${task.title} is being clicked")
// input id into this dialog
        val dialog = AddPlanDialog()
        dialog.show(requireFragmentManager(), "add_plan_dialog")
    }

    private fun onTodoCheckboxChanged(taskId: String, isChecked: Boolean) {
        Log.d("CHECKBOXCHANGE","today checkbox change-- $taskId")
        viewModel.updateTodoCompletion(taskId, isChecked)
    }

    fun shareTask() {
        val taskItems = viewModel.taskItemLocalsToday.value
        if (taskItems != null && taskItems.isNotEmpty()) {
            val shareText = StringBuilder()
            shareText.append("My Tasks for Today:\n")

            for (task in taskItems) {
                shareText.append("• ${task.title}\n")
                when (task.taskType) {
                    TaskType.DEADLINE -> {
                        val deadlineTask = task
                        shareText.append("  Start Date: ${deadlineTask.startDateDeadline}\n")
                        shareText.append("  End Date: ${deadlineTask.dateDeadline}\n\n")
                    }
                    TaskType.SCHEDULE -> {
                        val scheduleTask = task
                        shareText.append("  Schedule: ${scheduleTask.dateSchedule} ${scheduleTask.timeSchedule}\n")
                        shareText.append("  Location: ${scheduleTask.locationSchedule}\n\n")
                    }
                    TaskType.TODO -> {
                        val todoTask = task
                        shareText.append("  Status: ${if (todoTask.completedToDo == true) "Completed" else "Incomplete"}\n\n")
                    }

                    else -> {}
                }
            }

            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText.toString())
                type = "text/plain"
            }

            startActivity(Intent.createChooser(intent, "Share via"))
        } else {
            view?.let { rootView ->
                Snackbar.make(rootView, "No tasks to share", Snackbar.LENGTH_SHORT).show()
            }
        }
//        val url = "https://api.openweathermap.org/data/2.5/weather?q=${tvLocation.text}"
//        val shareText = getString(
//            R.string.share_text,
//            tvLocation.text.toString(),
//            url
//        )
//        val intent: Intent = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_TEXT, shareText)
//            type = "text/plain"
//        }
//        startActivity(Intent.createChooser(intent, null))
    }
}