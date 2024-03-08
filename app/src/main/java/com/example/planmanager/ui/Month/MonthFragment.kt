package com.example.planmanager.ui.Month

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.planmanager.util.TaskType
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.planmanager.R
import com.example.planmanager.data.TaskItem
import com.example.planmanager.databinding.FragmentMonthBinding
import com.example.planmanager.ui.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

class MonthFragment : Fragment() {

    private var _binding: FragmentMonthBinding? = null
    private val binding get() = _binding!!
    private val taskViewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val monthViewModel = ViewModelProvider(this).get(MonthViewModel::class.java)
        _binding = FragmentMonthBinding.inflate(inflater, container, false)
        val root: View = binding.root

        monthViewModel.text.observe(viewLifecycleOwner) {
            binding.textMonth.text = it
        }

        binding.monthAddButton.setOnClickListener {
            findNavController().navigate(R.id.navigate_to_add_plan)
        }

        setupCalendar()

        return root
    }

    private fun setupCalendar() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val selectedDateString = dateFormat.format(selectedDate.time)

            displayTasksForDate(selectedDateString)
        }
    }

    private fun displayTasksForDate(date: String) {
        taskViewModel.taskItems.observe(viewLifecycleOwner) { tasks ->
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val tasksForDate = tasks?.filter { taskItem ->
                when (taskItem.taskType) {
                    TaskType.DEADLINE -> taskItem.deadline?.deadlineDate == date
                    TaskType.TODO -> {
                        // Assuming ToDoItem has a date field; adjust as needed
                        // For example, if ToDoItem also uses a String for dates similar to Deadline
                        // taskItem.todo?.date == date
                        false // Change this based on your ToDoItem structure
                    }
                    TaskType.CALENDAR -> {
                        // Handle CALENDAR type if it's related to showing tasks for selected dates
                        false
                    }
                }
            }

            if (!tasksForDate.isNullOrEmpty()) {
                // Replace with your preferred method of displaying tasks
                Toast.makeText(context, "Tasks for $date: ${tasksForDate.size}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "No tasks for selected date", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
