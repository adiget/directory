package com.ags.annada.directory.officerooms

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ags.annada.directory.R
import com.ags.annada.directory.databinding.FragmentOfficeRoomsBinding
import com.ags.annada.directory.datasource.room.entities.OfficeRoom
import com.ags.annada.directory.home.HomeFragmentDirections
import com.ags.annada.directory.utils.EventObserver
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfficeRoomsFragment : Fragment() {
    private val viewModel by viewModels<OfficeRoomsViewModel>()
    private lateinit var adapter: OfficeRoomsAdapter

    private lateinit var binding: FragmentOfficeRoomsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOfficeRoomsBinding.inflate(inflater, container, false).apply {
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
            adapter = OfficeRoomsAdapter(viewModel)
            binding.officeRoomsList.adapter = adapter
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
            val item: OfficeRoom? = viewModel.items.value?.first { it.id == id }

            openDetails(it, item?.name ?: "Details")
        })
    }

    private fun openDetails(roomId: String, title: String) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToOfficeRoomDetailsFragment(roomId, title)

        findNavController().navigate(action)
    }
}