package com.keresman.flatearth.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.keresman.flatearth.NASA_PROVIDER_CONTENT_URI
import com.keresman.flatearth.R
import com.keresman.flatearth.adapter.ItemAdapter
import com.keresman.flatearth.databinding.FragmentItemsBinding
import com.keresman.flatearth.framework.queryItems
import com.keresman.flatearth.mapping.toItems
import com.keresman.flatearth.model.Item
import java.io.File

class ItemsFragment : Fragment() {

    private lateinit var binding: FragmentItemsBinding
    private lateinit var items: MutableList<Item>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemsBinding.inflate(inflater, container, false)

        items = requireContext()
            .queryItems(NASA_PROVIDER_CONTENT_URI)
            ?.toItems()
            ?.toMutableList()
            ?: mutableListOf()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (items.any { !File(it.picturePath).exists() }) {
            Toast.makeText(
                requireContext(),
                getString(R.string.images_not_downloaded),
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.rvItems.apply {
            adapter = ItemAdapter(requireContext(), items)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}