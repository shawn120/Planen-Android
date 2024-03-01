package com.example.planmanager.ui.Today

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planmanager.R
import com.example.planmanager.data.Deadline
import com.example.planmanager.databinding.FragmentTodayBinding

class TodayFragment : Fragment(R.layout.fragment_today) {
    private val adapter = TodayAdapter()
    private val viewModel: TodayViewModel by viewModels()
    private lateinit var deadlineListRV: RecyclerView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addBoxET: EditText = view.findViewById(R.id.et_title_entry)
        val addBtn: Button = view.findViewById(R.id.btn_add)

        deadlineListRV = view.findViewById(R.id.rv_task_list)
        deadlineListRV.layoutManager = LinearLayoutManager(requireContext())
        deadlineListRV.setHasFixedSize(true)

        deadlineListRV.adapter = adapter


        addBtn.setOnClickListener {
            val newDeadline = addBoxET.text.toString()
            Log.d("LookAtHere", "button being hit, input is {$newDeadline}")
            viewModel.loadData(newDeadline)
            addBoxET.setText("")
            deadlineListRV.scrollToPosition(0)
        }

        viewModel.deadlineItems.observe(viewLifecycleOwner){ deadlineItems ->
            Log.d("LookAtHere", "new deadlineItem: {$deadlineItems}")
            if (deadlineItems != null) {
                adapter.updateDeadlines(deadlineItems)
            }
        }

    }
}