package com.example.planmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView

class TodoListAdapter : RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>(){

    val toDos: MutableList<ToDoItem> = mutableListOf()

    override fun getItemCount() = toDos.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodoListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_list, parent, false)
        return TodoListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        val todo = toDos[position]
        holder.todoTV.text= todo.text
    }

    class TodoListViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val checkBox: CheckBox = view.findViewById(R.id.todo_checkbox)
        val todoTV: TextView = view.findViewById(R.id.tv_todo_text)
        var currentToDo: ToDoItem? = null

        init {
            checkBox.setOnCheckedChangeListener {
                    _, isChecked ->
                currentToDo?.completed = isChecked
            }

        }

        fun bind(toDo: ToDoItem){
            currentToDo = toDo
            checkBox.isChecked = toDo.completed
            todoTV.text = toDo.text
        }

    }

}