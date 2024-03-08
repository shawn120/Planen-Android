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
import androidx.viewpager2.widget.ViewPager2
import com.example.planmanager.R
import com.example.planmanager.data.ToDoItem
import com.example.planmanager.ui.Today.TodayFragment
import com.google.android.material.tabs.TabLayout
import java.util.Calendar

class AddPlanDialog(private val todayFragment: TodayFragment) : DialogFragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private lateinit var taskScroll: ScrollView
    private lateinit var taskcancelbtn: Button
    private lateinit var taskdonebtn: Button
    private lateinit var taskname: EditText
    private lateinit var taskSelectDateEdit: EditText

    private lateinit var deadlineScroll: ScrollView
    private lateinit var deadlinecancelbtn: Button
    private lateinit var deadlinedonebtn: Button
    private lateinit var deadlinename: EditText
    private lateinit var deadlineSelectStartDateEdit: EditText
    private lateinit var deadlineSelectDueDateEdit: EditText


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Add PLAN")
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_plan, null)
        builder.setView(dialogView)

        tabLayout = dialogView.findViewById(R.id.tabLayout)
        viewPager = dialogView.findViewById(R.id.viewPager)

//        <-----------------------task------------------------------------>
        taskScroll = dialogView.findViewById(R.id.task_scroll)
        taskcancelbtn = dialogView.findViewById(R.id.task_buttonCancel)
        taskdonebtn = dialogView.findViewById(R.id.task_buttonDone)
        taskname = dialogView.findViewById(R.id.editTextTaskName)
        taskSelectDateEdit = dialogView.findViewById(R.id.task_selectDate_edit)

//        <------------------------deadline-------------------------------->
        deadlineScroll = dialogView.findViewById(R.id.deadline_scroll)
        deadlinecancelbtn = dialogView.findViewById(R.id.deadline_buttonCancel)
        deadlinedonebtn = dialogView.findViewById(R.id.deadline_buttonDone)
        deadlinename = dialogView.findViewById(R.id.editTextDeadlineName)
        deadlineSelectStartDateEdit = dialogView.findViewById(R.id.deadline_selectStart_edit)
        deadlineSelectDueDateEdit = dialogView.findViewById(R.id.deadline_selectDue_edit)

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val initialDate = "Date : ${year}-${month + 1}-${day}"
        deadlineSelectStartDateEdit.setText(initialDate)


//        <--------------------------task function----------------------------->
        taskcancelbtn.setOnClickListener {
            dismiss()
        }

        taskdonebtn.setOnClickListener {
            val taskName = taskname.text.toString().trim()
            val taskDueDate = taskSelectDateEdit.text.toString().trim().substringAfter(":").trim()
            if (taskName.isNotEmpty()) {
                val todoItem = ToDoItem(taskName,taskDueDate)
                Log.d("LookAtHere", "Done button being hit, input is {$taskName}")
                todayFragment.viewModel.loadTodo(todoItem)
                taskname.text.clear()
                dismiss()


            } else {
                Toast.makeText(requireContext(), "Please enter a task name", Toast.LENGTH_SHORT).show()
            }
        }

        taskSelectDateEdit.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext())

            datePickerDialog.setOnDateSetListener { dialogView, year, month, dayOfMonth ->
                val selectedDate = "Date : ${year}-${month + 1}-${dayOfMonth}"
                taskSelectDateEdit.setText(selectedDate)
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

//    <-----------------------------deadline function--------------------------------->
       deadlinecancelbtn.setOnClickListener {
            dismiss()
       }

        deadlinedonebtn.setOnClickListener {
            val deadlineName = deadlinename.text.toString().trim()
            val deadlineStartDate = deadlineSelectStartDateEdit.text.toString().trim().substringAfter(":").trim()

            val deadlineDueDate = deadlineSelectDueDateEdit.text.toString().trim().substringAfter(":").trim()
            if (deadlineName.isNotEmpty()) {
                todayFragment.viewModel.loadDeadline(deadlineName,deadlineDueDate,deadlineStartDate)
                deadlinename.text.clear()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter a task name", Toast.LENGTH_SHORT).show()
            }
        }

        deadlineSelectStartDateEdit.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext())

            datePickerDialog.setOnDateSetListener { dialogView, year, month, dayOfMonth ->
                val selectedDate = "Date : ${year}-${month + 1}-${dayOfMonth}"
                deadlineSelectStartDateEdit.setText(selectedDate)
            }
            datePickerDialog.show()
        }

        deadlineSelectDueDateEdit.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext())

            datePickerDialog.setOnDateSetListener { dialogView, year, month, dayOfMonth ->
                val selectedDate = "Date : ${year}-${month + 1}-${dayOfMonth}"
                deadlineSelectDueDateEdit.setText(selectedDate)
            }
            datePickerDialog.show()
        }
        tabLayout.getTabAt(0)?.select()
        updateBottomContentVisibility(0)
        return builder.create()
    }
//    <---------------------------------common------------------------------------------>
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
