package com.keresman.flatearth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.keresman.flatearth.adapter.ItemPagerAdapter
import com.keresman.flatearth.databinding.ActivityItemPagerBinding
import com.keresman.flatearth.framework.queryItems
import com.keresman.flatearth.mapping.toItems
import com.keresman.flatearth.model.Item

const val ITEM_POS = "com.keresman.flatearth.item_position"
class ItemPagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemPagerBinding
    private lateinit var items: MutableList<Item>
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initPager()
    }

    private fun initPager() {
        binding.viewPager2.adapter = ItemPagerAdapter(this, items)
        binding.viewPager2.currentItem = position
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun init() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        position = intent.getIntExtra(ITEM_POS, position)
        items = queryItems(NASA_PROVIDER_CONTENT_URI)
            ?.toItems()
            ?.toMutableList()
            ?: mutableListOf()
    }
}