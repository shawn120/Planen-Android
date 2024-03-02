package com.example.planmanager.ui.AddPlan

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.planmanager.R
import com.example.planmanager.data.TaskItem
import com.example.planmanager.data.ToDoItem
import com.example.planmanager.ui.TaskViewModel
import com.example.planmanager.ui.Today.TodayAdapter
import com.example.planmanager.ui.Today.TodayFragment
import com.example.planmanager.util.TaskType
import com.google.android.material.tabs.TabLayout

class AddPlanFragment(private val todayFragment: TodayFragment) : DialogFragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var taskScroll: ScrollView
    private lateinit var deadlineScroll: ScrollView
    private lateinit var cancelbtn: Button
    private lateinit var donebtn: Button
    private lateinit var taskname: EditText
    private lateinit var selectDateEdit: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Add PLAN")
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_add_plan, null)
        builder.setView(dialogView)

        tabLayout = dialogView.findViewById(R.id.tabLayout)
        viewPager = dialogView.findViewById(R.id.viewPager)
        taskScroll = dialogView.findViewById(R.id.task_scroll)
        deadlineScroll = dialogView.findViewById(R.id.deadline_scroll)
        cancelbtn = dialogView.findViewById(R.id.task_buttonCancel)
        donebtn = dialogView.findViewById(R.id.task_buttonDone)
        taskname = dialogView.findViewById(R.id.editTextTaskName)
        selectDateEdit = dialogView.findViewById(R.id.selectDate_edit)

        cancelbtn.setOnClickListener {
            dismiss()
        }

        donebtn.setOnClickListener {
            val taskName = taskname.text.toString().trim()
            if (taskName.isNotEmpty()) {
                val todoItem = ToDoItem(taskName)
                Log.d("LookAtHere", "Done button being hit, input is {$taskName}")
                todayFragment.viewModel.loadTodo(todoItem)
                taskname.text.clear()
                dismiss()


            } else {
                Toast.makeText(requireContext(), "Please enter a task name", Toast.LENGTH_SHORT).show()
            }
        }


        selectDateEdit.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext())

            datePickerDialog.setOnDateSetListener { dialogView, year, month, dayOfMonth ->
                val selectedDate = "Date : ${year}-${month + 1}-${dayOfMonth}"
                selectDateEdit.setText(selectedDate)
            }
            datePickerDialog.show()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewPager.currentItem = it.position
                    updateBottomContentVisibility(it.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        return builder.create()
    }

    private fun updateBottomContentVisibility(position: Int) {
        when (position) {
            0 -> {
                taskScroll.visibility = View.VISIBLE
                deadlineScroll.visibility = View.GONE
            }

            1 -> {
                taskScroll.visibility = View.GONE
                deadlineScroll.visibility = View.VISIBLE
            }
        }
    }
}
