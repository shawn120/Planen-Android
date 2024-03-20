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
import com.example.planmanager.ui.Month.MonthFragment
import com.example.planmanager.ui.TaskViewModel
import com.example.planmanager.ui.Today.TodayFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

class AddPlanDialog : DialogFragment() {

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

    private lateinit var scheduleScroll: ScrollView
    private lateinit var schedulecancelbtn: Button
    private lateinit var scheduledonebtn: Button
    private lateinit var schedulename: EditText
    private lateinit var scheduleSelectDateEdit: EditText
    private lateinit var scheduleSelectTimeEdit: EditText
    private lateinit var scheduleLocation: EditText


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("New Todo")
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_plan, null)
        val viewModel: TaskViewModel by viewModels()
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

        // setting default values
        val initialDate = String.format("%d-%02d-%02d", year, month+1, day)
        deadlineSelectStartDateEdit.setText("Start from: " + initialDate)
        taskSelectDateEdit.setText("Date: " + initialDate)

//        <------------------------Calendar-------------------------------->

        scheduleScroll = dialogView.findViewById(R.id.schedule_scroll)
        schedulecancelbtn = dialogView.findViewById(R.id.schedule_buttonCancel)
        scheduledonebtn = dialogView.findViewById(R.id.schedule_buttonDone)
        schedulename = dialogView.findViewById(R.id.editTextScheduleName)
        scheduleSelectDateEdit = dialogView.findViewById(R.id.schedule_selectDate_edit)
        scheduleSelectTimeEdit = dialogView.findViewById(R.id.schedule_selectTime)
        scheduleLocation = dialogView.findViewById(R.id.editTextScheduleLocation)

//        <--------------------------task function----------------------------->
        taskcancelbtn.setOnClickListener {
            dismiss()
        }

        taskdonebtn.setOnClickListener {
            val taskName = taskname.text.toString().trim()
            val taskDueDate = taskSelectDateEdit.text.toString().trim().substringAfter(":").trim()
            if (taskName.isNotEmpty()) {
//                Log.d("LookAtHere", "Done button being hit, input is {$taskName}")
//                if(todayFragment != null){
                    viewModel.loadTodo(
                        newToDoTitle = taskName,
                        newToDoDate = taskDueDate
                    )
//                } else if(monthFragment != null){
//                    monthFragment?.viewModel?.loadTodo(todoItem)
//                }

                taskname.text.clear()
                dismiss()


            } else {
                Toast.makeText(requireContext(), "Please enter a task name", Toast.LENGTH_SHORT).show()
            }
        }

        taskSelectDateEdit.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext())

            datePickerDialog.setOnDateSetListener { dialogView, year, month, dayOfMonth ->
                val selectedDate = String.format("Date: %d-%02d-%02d", year, month+1, dayOfMonth)
                taskSelectDateEdit.setText(selectedDate)
            }
            datePickerDialog.show()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewPager.currentItem = it.position
                    updateBottomContentVisibility(it.position)

                    val title = when (it.position) {
                        0 -> "New Todo"
                        1 -> "New Deadline"
                        2 -> "New Calendar"
                        else -> "New Task"
                    }
                    dialog?.setTitle(title)
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
//                if(todayFragment != null){
                    viewModel.loadDeadline(deadlineName,deadlineDueDate,deadlineStartDate)
//                } else if (monthFragment != null){
//                    monthFragment?.viewModel?.loadDeadline(deadlineName,deadlineDueDate,deadlineStartDate)
//
//                }
                deadlinename.text.clear()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter a task name", Toast.LENGTH_SHORT).show()
            }
            deadlinename.text.clear()
            deadlineSelectStartDateEdit.text.clear()
            deadlineSelectDueDateEdit.text.clear()
        }

        deadlineSelectStartDateEdit.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext())

            datePickerDialog.setOnDateSetListener { dialogView, year, month, dayOfMonth ->
                val selectedDate = String.format("Start from: %d-%02d-%02d", year, month+1, dayOfMonth)
                deadlineSelectStartDateEdit.setText(selectedDate)
            }
            datePickerDialog.show()
        }

        deadlineSelectDueDateEdit.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext())

            datePickerDialog.setOnDateSetListener { dialogView, year, month, dayOfMonth ->
                val selectedDate = String.format("Due on: %d-%02d-%02d", year, month+1, dayOfMonth)
                deadlineSelectDueDateEdit.setText(selectedDate)
            }
            datePickerDialog.show()
        }

//    <-----------------------------calendarfunction--------------------------------->
        schedulecancelbtn.setOnClickListener {
            dismiss()
        }

        scheduledonebtn.setOnClickListener {
            val scheduleName = schedulename.text.toString().trim()
            val scheduleLocation = scheduleLocation.text.toString().trim()
            val scheduleDate = scheduleSelectDateEdit.text.toString().trim().substringAfter(":").trim()
            val scheduleTime = scheduleSelectTimeEdit.text.toString().trim().substringAfter(":").trim()

            if (scheduleName.isNotEmpty()) {
//                if(todayFragment != null){
                    viewModel.loadSchedule(
                        newScheduleTitle = scheduleName,
                        newScheduleLocation = scheduleLocation,
                        newScheduleDate = scheduleDate,
                        newScheduleTime = scheduleTime
                    )
//                } else if(monthFragment != null){
//                    monthFragment?.viewModel?.loadSchedule(scheduleItem)
//                }


                schedulename.text.clear()
                scheduleSelectDateEdit.text.clear()
                scheduleSelectTimeEdit.text.clear()

                dismiss()


            } else {
                Toast.makeText(requireContext(), "Please enter a task name", Toast.LENGTH_SHORT).show()
            }
        }
        scheduleSelectDateEdit.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext())

            datePickerDialog.setOnDateSetListener { dialogView, year, month, dayOfMonth ->
                val selectedDate = String.format("Date: %d-%02d-%02d", year, month+1, dayOfMonth)
                scheduleSelectDateEdit.setText(selectedDate)
            }
            datePickerDialog.show()
        }
        scheduleSelectTimeEdit.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hour)
                .setMinute(minute)
                .setTitleText("Select Time")
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val selectedTime = "Time : ${timePicker.hour}:${timePicker.minute}"
                scheduleSelectTimeEdit.setText(selectedTime)
            }

            timePicker.show(parentFragmentManager, "TimePicker")
        }


//     <-------------------common--------------------------->
        tabLayout.getTabAt(0)?.select()
        updateBottomContentVisibility(0)
        return builder.create()
    }
    private fun updateBottomContentVisibility(position: Int) {
        when (position) {
            0 -> {
                taskScroll.visibility = View.VISIBLE
                deadlineScroll.visibility = View.GONE
                scheduleScroll.visibility = View.GONE
            }

            1 -> {
                taskScroll.visibility = View.GONE
                deadlineScroll.visibility = View.VISIBLE
                scheduleScroll.visibility = View.GONE
            }

            2 -> {
                taskScroll.visibility = View.GONE
                deadlineScroll.visibility = View.GONE
                scheduleScroll.visibility = View.VISIBLE

            }
        }
    }
}
