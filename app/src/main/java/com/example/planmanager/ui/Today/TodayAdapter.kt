package com.example.planmanager.ui.Today

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.planmanager.R
import com.example.planmanager.data.TaskItem
import com.example.planmanager.ui.TaskViewModel
import com.example.planmanager.util.TaskType
import com.google.android.material.snackbar.Snackbar
import java.util.Date

class TodayAdapter(
    private val onTaskCardClick: (TaskItem) -> Unit,
    private val onTodoCheckboxChanged: (String, Boolean) -> Unit,
    val currentDate: Date? = null

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var tasks: MutableList<TaskItem> = mutableListOf()

    companion object {
        private const val VIEW_TYPE_DEADLINE = 0
        private const val VIEW_TYPE_TODO = 1
        private const val VIEW_TYPE_SCHEDULE = 2
    }


    fun clearTasks(){
        tasks.clear()
    }
    fun updateTasks(newTasks: MutableList<TaskItem>?) {
        notifyItemRangeRemoved(0, tasks.size)
        tasks = newTasks?: mutableListOf()
        Log.d("Lookathere","update :${newTasks}")
        notifyItemRangeInserted(0, tasks.size)
    }

    fun addTask(newTask: TaskItem, position: Int = 0) {
        tasks.add(position, newTask)
        notifyItemInserted(position)
    }

    fun updateTask(position: Int): TaskItem {
        val task = tasks[position]
        notifyItemChanged(position)
        return task
    }

    fun deleteTask(position: Int): TaskItem {
        val task = tasks.removeAt(position)
        notifyItemRemoved(position)
        return task
    }

    override fun getItemCount() = tasks.size

    override fun getItemViewType(position: Int): Int {
        val taskItem = tasks[position]
        val type = taskItem.taskType
        Log.d("LookatHere", "type : {$type}")
        return when (taskItem.taskType) {
            TaskType.DEADLINE -> VIEW_TYPE_DEADLINE
            TaskType.TODO -> VIEW_TYPE_TODO
            TaskType.SCHEDULE -> VIEW_TYPE_SCHEDULE

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

            VIEW_TYPE_SCHEDULE -> {
                val view = layoutInflater.inflate(R.layout.task_card, parent, false)
                ScheduleViewHolder(view, onTaskCardClick)
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
                    holder.bindDeadline(taskItem, currentDate)
                }

            }
            is TodoViewHolder -> {
                if (taskItem.taskType == TaskType.TODO) {
                    Log.d("LookatHere" , "TODOviewHolder{$taskItem}")
                    holder.bindTodo(taskItem)
                }
            }
            is ScheduleViewHolder -> {
                if (taskItem.taskType == TaskType.SCHEDULE) {
                    Log.d("LookatHere" , "ScheduleviewHolder{$taskItem}")
                    holder.bindSchedule(taskItem)
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
        private var currentDeadline: TaskItem? = null
        private var deadlineSubCard: ConstraintLayout = view.findViewById(R.id.ddl_sub_card)
        private var todoSubCard: ConstraintLayout = view.findViewById(R.id.todo_sub_card)
        private var scheduleSubCard: ConstraintLayout = view.findViewById(R.id.schedule_sub_card)
        init{
            itemView.setOnClickListener {
//                 todo: implement later, maybe need different "onTaskCardClick" for three of them
                currentDeadline?.let(onClick)

            }
        }

        fun bindDeadline(taskItem: TaskItem, currentDate: Date?) {
            currentDeadline = taskItem
            deadlineTV.text = taskItem.title
            deadlineDateTV.text = taskItem.dateDeadline
            deadlineProgressPB.progress = taskItem.percentagePassed(currentDate)
            deadlinePercentageTV.text = taskItem.percentagePassed(currentDate).toString() + "%"
            startDateTV.text = taskItem.startDateDeadline
            deadlineSubCard.visibility=View.VISIBLE
            todoSubCard.visibility=View.INVISIBLE
            scheduleSubCard.visibility=View.INVISIBLE
        }
    }
    inner class TodoViewHolder(
        view: View,
        onClick: (TaskItem) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private var todoTV: TextView = view.findViewById(R.id.tv_todo_text)
        private var todoDateTV: TextView = view.findViewById(R.id.tv_todo_date)
        private var currentTodo: TaskItem? = null
        private var deadlineSubCard: ConstraintLayout = view.findViewById(R.id.ddl_sub_card)
        private var todoSubCard: ConstraintLayout = view.findViewById(R.id.todo_sub_card)
        private var scheduleSubCard: ConstraintLayout = view.findViewById(R.id.schedule_sub_card)
        private var todoCheckbox: CheckBox = view.findViewById(R.id.todo_checkbox)
        init{
            todoCheckbox.setOnCheckedChangeListener { _, isChecked ->
                currentTodo?.completedToDo = isChecked

                if (currentTodo != null) {
                    val completed = isChecked
                    val taskId = currentTodo?.id
                    taskId?.let {
                        onTodoCheckboxChanged(it, isChecked)
                    }
                }
            }
            itemView.setOnClickListener {
//                 todo: implement later, maybe need different "onTaskCardClick" for three of them
                currentTodo?.let(onClick)

            }
        }

        fun bindTodo(taskItem: TaskItem) {

            currentTodo = taskItem
            todoTV.text = taskItem.title
            todoDateTV.text = taskItem.dateToDo
            deadlineSubCard.visibility=View.INVISIBLE
            todoSubCard.visibility=View.VISIBLE
            scheduleSubCard.visibility=View.INVISIBLE

            todoCheckbox.setOnCheckedChangeListener(null)

            todoCheckbox.isChecked = taskItem.completedToDo == true
            todoCheckbox.setOnCheckedChangeListener { _, isChecked ->
                currentTodo?.completedToDo = isChecked

                if (currentTodo != null) {
                    val taskId = currentTodo?.id
                    taskId?.let {
                        onTodoCheckboxChanged(it, isChecked)
                    }
                }
            }
//            todoCheckbox.isChecked = taskItem.completedToDo == true
//
//            todoCheckbox.setOnCheckedChangeListener(null)
//            todoCheckbox.setOnCheckedChangeListener { _, isChecked ->
//                currentTodo?.let {
//                    onTodoCheckboxChanged(it.id, isChecked)
//                }
//            }
        }
    }

    inner class ScheduleViewHolder(
        view: View,
        onClick: (TaskItem) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private var scheduleTV: TextView = view.findViewById(R.id.tv_schedule_title)
        private var scheduleLocationTV: TextView = view.findViewById(R.id.tv_schedule_location)
        private var scheduleDateTimeTV: TextView = view.findViewById(R.id.tv_schedule_date_time)
        private var currentSchedule: TaskItem? = null
        private var deadlineSubCard: ConstraintLayout = view.findViewById(R.id.ddl_sub_card)
        private var todoSubCard: ConstraintLayout = view.findViewById(R.id.todo_sub_card)
        private var scheduleSubCard: ConstraintLayout = view.findViewById(R.id.schedule_sub_card)

        init{
            itemView.setOnClickListener {
                // todo: implement later, maybe need different "onTaskCardClick" for three of them
                currentSchedule?.let(onClick)

            }
        }

        fun bindSchedule(taskItem: TaskItem) {

            currentSchedule = taskItem
            scheduleTV.text = taskItem.title
            scheduleLocationTV.text = taskItem.locationSchedule
            scheduleDateTimeTV.text = taskItem.dateSchedule +" "+ taskItem.timeSchedule
            deadlineSubCard.visibility=View.INVISIBLE
            todoSubCard.visibility=View.INVISIBLE
            scheduleSubCard.visibility=View.VISIBLE

        }
    }
}