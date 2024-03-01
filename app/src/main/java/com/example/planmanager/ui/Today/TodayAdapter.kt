package com.example.planmanager.ui.Today

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planmanager.R
import com.example.planmanager.data.Deadline

class TodayAdapter : RecyclerView.Adapter<TodayAdapter.TodayViewHolder>() {

    private var deadlines: MutableList<Deadline> = mutableListOf()

    fun updateDeadlines(newdeadlines: MutableList<Deadline>) {
        notifyItemRangeRemoved(0, deadlines.size)
        deadlines = newdeadlines
        notifyItemRangeInserted(0, deadlines.size)
    }

    fun deleteDeadline(position: Int): Deadline {
        val deadline = deadlines.removeAt(position)
        notifyItemRemoved(position)
        return deadline
    }

    override fun getItemCount() = deadlines.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.deadline_card, parent, false)
        return TodayViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodayViewHolder, position: Int) {
        holder.bind(deadlines[position])
    }

    class TodayViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val deadlineTV: TextView = view.findViewById(R.id.deadlineNameTextView)
        private val deadlineDateTV: TextView = view.findViewById(R.id.deadlineDateTextView)
        private val deadlineProgressPB: ProgressBar = view.findViewById(R.id.progressBar)
        private val deadlinePercentageTV: TextView = view.findViewById(R.id.percentageTextView)
        private var currentDeadline: Deadline? = null

        fun bind(deadline: Deadline) {
            currentDeadline = deadline
            deadlineTV.text = deadline.title
            deadlineDateTV.text = deadline.deadlineDate
            deadlineProgressPB.progress = deadline.percentagePassed
            deadlinePercentageTV.text = deadline.percentagePassed.toString()+"%"
        }
    }
}