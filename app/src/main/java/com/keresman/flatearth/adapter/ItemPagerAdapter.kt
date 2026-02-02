package com.keresman.flatearth.adapter

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keresman.flatearth.ITEM_POS
import com.keresman.flatearth.ItemPagerActivity
import com.keresman.flatearth.NASA_PROVIDER_CONTENT_URI
import com.keresman.flatearth.R
import com.keresman.flatearth.handler.loadImage
import com.keresman.flatearth.framework.startActivity
import com.keresman.flatearth.model.Item

class ItemPagerAdapter(
    private val context: Context,
    private val items: MutableList<Item>
) : RecyclerView.Adapter<ItemPagerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_pager, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = items[position]
        holder.bind(item)

        holder.ivRead.setOnClickListener {
            updateItem(position)
        }
    }

    private fun updateItem(position: Int) {
        val item = items[position]
        item.read = !item.read
        context.contentResolver.update(
            ContentUris.withAppendedId(NASA_PROVIDER_CONTENT_URI, item._id!!),
            ContentValues().apply {
                put(Item::read.name, item.read)
            },
            null,
            null
        )

        notifyItemChanged(position)
    }

    override fun getItemCount() = items.count()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val tvItem = itemView.findViewById<TextView>(R.id.tvItem)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        private val tvExplanation = itemView.findViewById<TextView>(R.id.tvExplanation)
        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        val ivRead = itemView.findViewById<ImageView>(R.id.ivRead)

        fun bind(item: Item){
            tvItem.text = item.title
            tvDate.text = item.date
            tvExplanation.text = item.explanation
            ivRead.setImageResource(if(item.read) R.drawable.green_flag else R.drawable.red_flag)

            loadImage(ivItem, item.picturePath)
        }

    }
}
