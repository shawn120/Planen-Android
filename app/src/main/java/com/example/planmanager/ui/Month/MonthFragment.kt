package com.example.planmanager.ui.Month

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.planmanager.R
import com.example.planmanager.databinding.FragmentMonthBinding
import com.example.planmanager.ui.AddPlan.AddPlanDialog

class MonthFragment : Fragment() {

    private var _binding: FragmentMonthBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.activity_main_menu, menu)
                }
                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.action_add_plan -> {
                            val dialog = AddPlanDialog(null,this@MonthFragment)
                            dialog.show(requireFragmentManager(), "add_plan_dialog")
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.STARTED
        )

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}