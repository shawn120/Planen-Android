package com.example.planmanager.ui.Month

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planmanager.R
import com.example.planmanager.data.TaskItem
import com.example.planmanager.databinding.FragmentMonthBinding
import com.example.planmanager.ui.AddPlan.AddPlanDialog
import com.example.planmanager.ui.TaskViewModel
import com.example.planmanager.ui.Today.TodayAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MonthFragment : Fragment(R.layout.fragment_month) {

    private var _binding: FragmentMonthBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val adapter = TodayAdapter(::onTaskCardClick)
    val viewModel: TaskViewModel by viewModels()
    private lateinit var taskListRV: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMonthBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskListRV = view.findViewById(R.id.rv_task_list_on_day)
        taskListRV.layoutManager = LinearLayoutManager(requireContext())
        taskListRV.setHasFixedSize(true)
        taskListRV.adapter = adapter

        setupCalendar()

        val monthViewModel = ViewModelProvider(this).get(MonthViewModel::class.java)

        val textView: TextView = binding.textMonth

        monthViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupCalendar() {
        val calendarView = binding.calendarView

        // Get the current date
        val currentDate = Calendar.getInstance()

        // Set the current date as the selected date in the CalendarView
        calendarView.setDate(currentDate.timeInMillis, false, true)
        val textView: TextView = binding.textMonth
        // Handle date change event

        // initial today's task
        viewModel.taskItemLocalsToday.observe(viewLifecycleOwner){
            adapter.updateTasks(it)
            taskListRV.scrollToPosition(0)
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)


            textView.text = formattedDate
            val list = viewModel.getTaskOnDay(date = formattedDate)
            list.observe(viewLifecycleOwner) {taskItemLocalsList ->
                Log.d("LookAtHere", "today fragment new item: {$taskItemLocalsList}")
                adapter.updateTasks(taskItemLocalsList)
                taskListRV.scrollToPosition(0)
            }
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
                val rootView = view?.findViewById<View>(R.id.rv_task_list_on_day)
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
}