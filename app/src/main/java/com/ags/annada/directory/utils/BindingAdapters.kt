package com.ags.annada.directory.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ags.annada.directory.datasource.room.entities.Employee
import com.ags.annada.directory.datasource.room.entities.OfficeRoom
import com.ags.annada.directory.employees.EmployeesAdapter
import com.ags.annada.directory.officerooms.OfficeRoomsAdapter
import com.squareup.picasso.Picasso


@BindingAdapter("app:employees")
fun setEmployees(listView: RecyclerView, items: List<Employee>?) {
    items?.let {
        (listView.adapter as EmployeesAdapter).submitList(items)
    }
}

@BindingAdapter("app:rooms")
fun setOfficeRooms(listView: RecyclerView, items: List<OfficeRoom>?) {
    items?.let {
        (listView.adapter as OfficeRoomsAdapter).submitList(items)
    }
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    Picasso.get()
        .load(imageUrl)
        .into(view);
}