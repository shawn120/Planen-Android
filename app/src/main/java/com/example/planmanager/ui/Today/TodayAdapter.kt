package com.example.planmanager.ui.Today

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.planmanager.R
import com.example.planmanager.data.Deadline
import com.example.planmanager.data.TaskItem
import com.example.planmanager.data.ToDoItem
import com.example.planmanager.util.TaskType

class TodayAdapter(
    private val onTaskCardClick: (TaskItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var tasks: MutableList<TaskItem> = mutableListOf()

    companion object {
        private const val VIEW_TYPE_DEADLINE = 0
        private const val VIEW_TYPE_TODO = 1
    }

    fun updateTasks(newTasks: MutableList<TaskItem>) {
        notifyItemRangeRemoved(0, tasks.size)
        tasks = newTasks
        Log.d("Lookathere","update :${newTasks}")
        notifyItemRangeInserted(0, tasks.size)
    }

    fun deleteDeadline(position: Int): TaskItem {
        val deadline = tasks.removeAt(position)
        notifyItemRemoved(position)
        return deadline
    }

    override fun getItemCount() = tasks.size

    override fun getItemViewType(position: Int): Int {
        val taskItem = tasks[position]
        val type = taskItem.taskType
        Log.d("LookatHere", "type : {$type}")
        return when (taskItem.taskType) {
            TaskType.DEADLINE -> VIEW_TYPE_DEADLINE
            TaskType.TODO -> VIEW_TYPE_TODO

            else -> throw IllegalArgumentException("Invalid task type")
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_DEADLINE -> {
                val view = layoutInflater.inflate(R.layout.task_card, parent, false)
                DeadlineViewHolder(view, onTaskCardClick)
            }
            VIEW_TYPE_TODO -> {
                val view = layoutInflater.inflate(R.layout.task_card, parent, false)
                TodoViewHolder(view, onTaskCardClick)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val taskItem = tasks[position]
        when (holder) {
            is DeadlineViewHolder -> {
                Log.d("LookatHere" , "DeadlineviewHolder{$taskItem}")
                if (taskItem.taskType == TaskType.DEADLINE) {
                    holder.bindDeadline(taskItem)
                }
            }
            is TodoViewHolder -> {
                if (taskItem.taskType == TaskType.TODO) {
                    Log.d("LookatHere" , "TODOviewHolder{$taskItem}")
                    holder.bindTodo(taskItem)
                }
            }
        }
    }

    inner class DeadlineViewHolder(
        view: View,
        onClick: (TaskItem) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val deadlineTV: TextView = view.findViewById(R.id.tv_ddl_name)
        private val deadlineDateTV: TextView = view.findViewById(R.id.tv_ddl_deadline_date)
        private val deadlineProgressPB: ProgressBar = view.findViewById(R.id.pb_ddl_progress_bar)
        private val deadlinePercentageTV: TextView = view.findViewById(R.id.tv_ddl_percentage)
        private val startDateTV: TextView = view.findViewById(R.id.tv_ddl_start_date)
        private var currentDeadline: Deadline? = null
        private var deadlineSubCard: ConstraintLayout = view.findViewById(R.id.ddl_sub_card)
        private var todoSubCard: ConstraintLayout = view.findViewById(R.id.todo_sub_card)

        init{
            itemView.setOnClickListener {
                val currentTask = TaskItem(
                    taskType = TaskType.DEADLINE,
                    deadline = currentDeadline
                )
                currentTask.let(onClick)
            }
        }

        fun bindDeadline(taskItem: TaskItem) {
            currentDeadline = taskItem.deadline
            deadlineTV.text = taskItem.deadline?.title
            deadlineDateTV.text = taskItem.deadline?.deadlineDate
            deadlineProgressPB.progress = taskItem.deadline?.percentagePassed!!
            deadlinePercentageTV.text = taskItem.deadline?.percentagePassed.toString() + "%"
            startDateTV.text = taskItem.deadline?.startDate
            deadlineSubCard.visibility=View.VISIBLE
            todoSubCard.visibility=View.INVISIBLE
        }
    }
    inner class TodoViewHolder(
        view: View,
        onClick: (TaskItem) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private var todoTV: TextView = view.findViewById(R.id.tv_todo_text)
        private var currentTodo: ToDoItem? = null
        private var deadlineSubCard: ConstraintLayout = view.findViewById(R.id.ddl_sub_card)
        private var todoSubCard: ConstraintLayout = view.findViewById(R.id.todo_sub_card)

        init{
            itemView.setOnClickListener {
                val currentTask = TaskItem(
                    taskType = TaskType.TODO,
                    todo = currentTodo
                )
                currentTask.let(onClick)
            }
        }

        fun bindTodo(taskItem: TaskItem) {

            currentTodo = taskItem.todo
            currentTodo?.let {
                todoTV.text = it.text
            }
            deadlineSubCard.visibility=View.INVISIBLE
            todoSubCard.visibility=View.VISIBLE
        }
    }
}