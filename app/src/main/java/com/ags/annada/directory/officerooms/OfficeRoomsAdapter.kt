package com.ags.annada.directory.officerooms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ags.annada.directory.databinding.ItemEmployeeBinding
import com.ags.annada.directory.databinding.ItemOfficeRoomBinding
import com.ags.annada.directory.datasource.room.entities.Employee
import com.ags.annada.directory.datasource.room.entities.OfficeRoom

class OfficeRoomsAdapter(
    private val viewModel: OfficeRoomsViewModel
) : ListAdapter<OfficeRoom, OfficeRoomsAdapter.ViewHolder>(OfficeRoomDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(viewModel, item)
    }

    class ViewHolder private constructor(val binding: ItemOfficeRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: OfficeRoomsViewModel, item: OfficeRoom) {
            binding.viewmodel = viewModel
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemOfficeRoomBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class OfficeRoomDiffCallback : DiffUtil.ItemCallback<OfficeRoom>() {
    override fun areItemsTheSame(oldItem: OfficeRoom, newItem: OfficeRoom): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: OfficeRoom, newItem: OfficeRoom): Boolean {
        return oldItem == newItem
    }
}
