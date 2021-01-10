package com.ags.annada.directory.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ags.annada.directory.employees.EmployeesFragment
import com.ags.annada.directory.officerooms.OfficeRoomsFragment

class ChildFragmentStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EmployeesFragment()
            1 -> OfficeRoomsFragment()
            else -> EmployeesFragment()
        }
    }
}