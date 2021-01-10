package com.ags.annada.directory.officerooms.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ags.annada.directory.databinding.FragmentOfficeRoomDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfficeRoomDetailsFragment : Fragment() {
    private val viewModel by viewModels<OfficeRoomDetailsViewModel>()
    private lateinit var binding: FragmentOfficeRoomDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOfficeRoomDetailsBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner

    }
}