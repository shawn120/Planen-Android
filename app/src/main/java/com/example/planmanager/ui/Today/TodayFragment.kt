package com.example.planmanager.ui.Today

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planmanager.R
import java.util.Calendar

class TodayFragment : Fragment(R.layout.fragment_today) {
    private val adapter = TodayAdapter()
    private val viewModel: DeadlineViewModel by viewModels()
    private lateinit var deadlineListRV: RecyclerView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addBoxET: EditText = view.findViewById(R.id.et_title_entry)
        val addBtn: Button = view.findViewById(R.id.btn_add)

        deadlineListRV = view.findViewById(R.id.rv_task_list)
        deadlineListRV.layoutManager = LinearLayoutManager(requireContext())
        deadlineListRV.setHasFixedSize(true)

        deadlineListRV.adapter = adapter

        val etDateEntry = view.findViewById<EditText>(R.id.et_ddl_pick)
        addBtn.setOnClickListener {
            val newDeadline = addBoxET.text.toString()
            Log.d("LookAtHere", "button being hit, input is {$newDeadline}")
            viewModel.loadData(newDeadline, etDateEntry.text.toString())
            addBoxET.setText("")
            etDateEntry.setText("")
            deadlineListRV.scrollToPosition(0)
        }

        viewModel.deadlineItems.observe(viewLifecycleOwner){ deadlineItems ->
            Log.d("LookAtHere", "new deadlineItem: {$deadlineItems}")
            if (deadlineItems != null) {
                adapter.updateDeadlines(deadlineItems)
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

    }
}