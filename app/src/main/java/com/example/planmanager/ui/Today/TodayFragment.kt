package com.example.planmanager.ui.Today

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planmanager.R
import com.example.planmanager.TodoListAdapter
import com.example.planmanager.TodoListViewModel
import com.example.planmanager.databinding.FragmentTodayBinding

class TodayFragment : Fragment() {

    private var _binding: FragmentTodayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val todoListViewModel: TodoListViewModel by viewModels()
    private lateinit var todorecyclerView: RecyclerView
    private lateinit var todoadapter: TodoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        todorecyclerView = root.findViewById(R.id.rv_todo_list)
        todoadapter = TodoListAdapter(listOf())

        val layoutManager = LinearLayoutManager(requireContext())
        todorecyclerView.layoutManager = layoutManager
        todorecyclerView.setHasFixedSize(true)

        todorecyclerView.adapter = todoadapter
        todorecyclerView.scrollToPosition(0)

//        todoListViewModel.todoItems.observe(requireContext()) {
//
//            todoData -> todoadapter.updateTodoList(todoData)
//
//        }

        todoadapter.notifyDataSetChanged()

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