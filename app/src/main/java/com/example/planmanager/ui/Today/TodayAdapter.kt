package com.example.planmanager.ui.Today

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planmanager.R
import com.example.planmanager.data.Deadline
import com.example.planmanager.data.TaskItem
import com.example.planmanager.util.TaskType

class TodayAdapter : RecyclerView.Adapter<TodayAdapter.TodayViewHolder>() {

    private var tasks: MutableList<TaskItem> = mutableListOf()

    fun updateTasks(newTasks: MutableList<TaskItem>) {
        notifyItemRangeRemoved(0, tasks.size)
        tasks = newTasks
        notifyItemRangeInserted(0, tasks.size)
    }

    fun deleteDeadline(position: Int): TaskItem {
        val deadline = tasks.removeAt(position)
        notifyItemRemoved(position)
        return deadline
    }

    override fun getItemCount() = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.deadline_card, parent, false)
        return TodayViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodayViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    class TodayViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val deadlineTV: TextView = view.findViewById(R.id.tv_ddl_name)
        private val deadlineDateTV: TextView = view.findViewById(R.id.tv_ddl_deadline_date)
        private val deadlineProgressPB: ProgressBar = view.findViewById(R.id.pb_ddl_progress_bar)
        private val deadlinePercentageTV: TextView = view.findViewById(R.id.tv_ddl_percentage)
        private val startDateTV: TextView = view.findViewById(R.id.tv_ddl_start_date)
        private var currentDeadline: Deadline? = null

        fun bind(taskItem: TaskItem) {
            if (taskItem.taskType == TaskType.DEADLINE) {
                currentDeadline = taskItem.deadline
                deadlineTV.text = taskItem.deadline?.title
                deadlineDateTV.text = taskItem.deadline?.deadlineDate
                deadlineProgressPB.progress = taskItem.deadline?.percentagePassed!!
                deadlinePercentageTV.text = taskItem.deadline?.percentagePassed.toString()+"%"
                startDateTV.text = taskItem.deadline?.startDate
            }

        }
    }
}