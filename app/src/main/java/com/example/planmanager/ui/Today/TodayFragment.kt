package com.example.planmanager.ui.Today

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.example.planmanager.R
import com.example.planmanager.TodoListAdapter
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planmanager.data.ToDoItem
import com.example.planmanager.ui.TaskViewModel
import java.util.Calendar

class TodayFragment : Fragment(R.layout.fragment_today) {
    private val adapter = TodayAdapter()
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var taskListRV: RecyclerView
    private lateinit var todorecyclerView: RecyclerView
    private lateinit var todoadapter: TodoListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addBoxET: EditText = view.findViewById(R.id.et_title_entry)
        val addBtn: Button = view.findViewById(R.id.btn_add)

        taskListRV = view.findViewById(R.id.rv_task_list)
        taskListRV.layoutManager = LinearLayoutManager(requireContext())
        taskListRV.setHasFixedSize(true)

        taskListRV.adapter = adapter


//         todoListViewModel.todoItems.observe(requireContext()) {
//
//             todoData -> todoadapter.updateTodoList(todoData)
//
//         }

//         todoadapter.notifyDataSetChanged()

         val buttonAddPlan: Button = view.findViewById(R.id.today_add_button)
         buttonAddPlan.setOnClickListener {
             findNavController().navigate(R.id.navigate_to_add_plan)
         }
//         return root
//     }
//     override fun onDestroyView() {
//         super.onDestroyView()
//         _binding = null
// =======
        val etDateEntry = view.findViewById<EditText>(R.id.et_ddl_pick)

        addBtn.setOnClickListener {
            val newDeadline = addBoxET.text.toString()
            Log.d("LookAtHere", "button being hit, input is {$newDeadline}")
            viewModel.loadDeadline(newDeadline, etDateEntry.text.toString())
            addBoxET.setText("")
            etDateEntry.setText("")
            taskListRV.scrollToPosition(0)
        }

        viewModel.taskItems.observe(viewLifecycleOwner){ taskItems ->
            Log.d("LookAtHere", "new item: {$taskItems}")
            if (taskItems != null) {
                adapter.updateTasks(taskItems)
            }
        }

        // Set up a DatePickerDialog when the EditText is clicked
        etDateEntry.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Handle the selected date
                    etDateEntry.setText("$selectedYear-${selectedMonth + 1}-$selectedDay")
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }

// >>>>>>> shawn
    }
}