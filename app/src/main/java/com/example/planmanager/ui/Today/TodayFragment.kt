package com.example.planmanager.ui.Today

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planmanager.R
import com.example.planmanager.ui.AddPlan.AddPlanFragment
import com.example.planmanager.ui.TaskViewModel
import java.util.Calendar

class TodayFragment : Fragment(R.layout.fragment_today) {
    private val adapter = TodayAdapter()
    val viewModel: TaskViewModel by viewModels()
    private lateinit var taskListRV: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addBoxET: EditText = view.findViewById(R.id.et_title_entry)
        val addBtn: Button = view.findViewById(R.id.btn_add)

        taskListRV = view.findViewById(R.id.rv_task_list)
        taskListRV.layoutManager = LinearLayoutManager(requireContext())
        taskListRV.setHasFixedSize(true)
        taskListRV.adapter = adapter

        val toolbar: ImageButton = view.findViewById(R.id.today_add_button)

        toolbar.setOnClickListener{
            val popupMenu = PopupMenu(requireContext(), toolbar)
            popupMenu.menuInflater.inflate(R.menu.add_plan, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_task -> {
                        val dialog = AddPlanFragment(this@TodayFragment)
                        dialog.show(requireFragmentManager(), "add_plan_dialog")
                        true
                    }
                    R.id.menu_deadline -> {
                        // Handle menu deadline item click
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        val etDateEntry = view.findViewById<EditText>(R.id.et_ddl_pick)

        addBtn.setOnClickListener {
            val newDeadline = addBoxET.text.toString()
            Log.d("LookAtHere", "button being hit, input is {$newDeadline}")
            viewModel.loadDeadline(newDeadline, etDateEntry.text.toString())
            addBoxET.setText("")
            etDateEntry.setText("")
            taskListRV.scrollToPosition(0)
        }

        viewModel.taskItems.observe(viewLifecycleOwner){
            taskItems ->
            Log.d("LookAtHere", "todayfragment new item: {$taskItems}")
            if (taskItems != null) {
                adapter.updateTasks(taskItems)
            }
            taskListRV.scrollToPosition(0)
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

    }
}