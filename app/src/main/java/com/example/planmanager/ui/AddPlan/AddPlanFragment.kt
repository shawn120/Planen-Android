package com.example.planmanager.ui.AddPlan

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.planmanager.R
import com.google.android.material.tabs.TabLayout

class AddPlanFragment : Fragment(R.layout.fragment_add_plan) {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var taskBottomContent: ConstraintLayout
    private lateinit var deadlineBottomContent: ConstraintLayout
    private lateinit var taskScroll:ScrollView
    private lateinit var deadlineScroll:ScrollView
    private lateinit var cancelbtn: Button
    private lateinit var donebtn: Button


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)
        taskBottomContent = view.findViewById(R.id.taskBottomContent)
        deadlineBottomContent = view.findViewById(R.id.deadlineBottomContent)
        taskScroll = view.findViewById(R.id.task_scroll)
        deadlineScroll = view.findViewById(R.id.deadline_scroll)
        cancelbtn = view.findViewById(R.id.task_buttonCancel)
        donebtn = view.findViewById(R.id.task_buttonDone)

        cancelbtn.setOnClickListener {
            // 返回到原始的 Fragment
            requireActivity().supportFragmentManager.popBackStack()
        }

        donebtn.setOnClickListener {
            // 返回到原始的 Fragment
            requireActivity().supportFragmentManager.popBackStack()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewPager.currentItem = it.position
                    updateBottomContentVisibility(it.position)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun updateBottomContentVisibility(position: Int) {
        when (position) {
            0 -> {
                    taskScroll.visibility = View.VISIBLE
                    taskBottomContent.visibility = View.VISIBLE
                    deadlineScroll.visibility = View.GONE
                    deadlineBottomContent.visibility = View.GONE
            }
            1 -> {
                taskScroll.visibility = View.GONE
                taskBottomContent.visibility = View.GONE
                deadlineScroll.visibility = View.VISIBLE
                deadlineBottomContent.visibility = View.VISIBLE
            }
        }
    }
}
