package com.ags.annada.directory.employees

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ags.annada.directory.R
import com.ags.annada.directory.databinding.FragmentEmployeesBinding
import com.ags.annada.directory.datasource.room.entities.Employee
import com.ags.annada.directory.home.HomeFragmentDirections
import com.ags.annada.directory.utils.EventObserver
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmployeesFragment : Fragment() {
    private val viewModel by viewModels<EmployeesViewModel>()
    private lateinit var adapter: EmployeesAdapter

    private lateinit var binding: FragmentEmployeesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmployeesBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner

        setupSnackbar()
        setupNavigation()
        setupAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)

        val item = menu.findItem(R.id.action_search)
        val searchView: SearchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.setSearchString(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.setSearchString(newText)
                return false
            }
        })
    }

    private fun setupAdapter() {
        val viewModel = binding.viewmodel

        if (viewModel != null) {
            adapter = EmployeesAdapter(viewModel)
            binding.employeesList.adapter = adapter
        } else {
            Log.d("setupAdapter()", "ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupSnackbar() {
        viewModel.snackbarText.observe(viewLifecycleOwner, EventObserver {
            context?.getString(it)
                ?.let { view?.let { it1 -> Snackbar.make(it1, it, Snackbar.LENGTH_SHORT).show() } }
        })
    }

    private fun setupNavigation() {
        viewModel.selectItemEvent.observe(viewLifecycleOwner, EventObserver { it ->
            Log.d("SELECTED ID", "selected=${it}")

            val id = it
            val item: Employee? = viewModel.items.value?.first { it.id == id }

            openDetails(it, item?.firstName + " " + item?.lastName)
        })
    }

    private fun openDetails(empId: String, title: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToEmployeeDetailsFragment(empId,title)
        findNavController().navigate(action)
    }
}