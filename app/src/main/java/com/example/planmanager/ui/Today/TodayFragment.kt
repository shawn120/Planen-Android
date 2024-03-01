package com.example.planmanager.ui.Today

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planmanager.DummyTodoList
import com.example.planmanager.R
import com.example.planmanager.ToDoItem
import com.example.planmanager.TodoListAdapter
import com.example.planmanager.databinding.FragmentTodayBinding
import com.example.planmanager.ui.AddPlan.AddPlanFragment

class TodayFragment : Fragment() {

    private var _binding: FragmentTodayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TodoListAdapter // 自定义的适配器

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val todayViewModel =
        //   ViewModelProvider(this).get(TodayViewModel::class.java)

        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = root.findViewById(R.id.rv_todo_list)
        adapter = TodoListAdapter() // 初始化适配器

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val todoList = DummyTodoList.getDummyTodoList()
        adapter.toDos.addAll(todoList)
        adapter.notifyDataSetChanged()

        val buttonAddPlan: Button = root.findViewById(R.id.today_add_button)
        buttonAddPlan.setOnClickListener {
            findNavController().navigate(R.id.navigate_to_add_plan)
        }
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}