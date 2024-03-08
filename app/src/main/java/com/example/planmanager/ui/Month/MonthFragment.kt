package com.example.planmanager.ui.Month

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.planmanager.R
import com.example.planmanager.databinding.FragmentMonthBinding
import java.text.SimpleDateFormat
import java.util.*

class MonthFragment : Fragment() {

    private var _binding: FragmentMonthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val monthViewModel =
            ViewModelProvider(this).get(MonthViewModel::class.java)

        _binding = FragmentMonthBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMonth
        monthViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        setupCalendar()

        return root
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

        // Handle date change event
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)

            val textView: TextView = binding.textMonth
            textView.text = formattedDate
        }
    }
}
