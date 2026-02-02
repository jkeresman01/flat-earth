package com.keresman.flatearth.adapter

import android.content.Context
import android.os.Bundle
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
import androidx.appcompat.app.AlertDialog
import com.keresman.flatearth.framework.deleteById
import com.keresman.flatearth.framework.startActivity
import com.keresman.flatearth.model.Item
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemAdapter(
    private val context: Context,
    private val items: MutableList<Item>
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            context.startActivity<ItemPagerActivity>(
                Bundle().apply { putInt(ITEM_POS, position) }
            )
        }

        holder.itemView.setOnLongClickListener {
            deleteItem(position)
            true
        }
    }

    private fun deleteItem(position: Int) {
        AlertDialog.Builder(context).apply {
            setTitle(R.string.delete)
            setMessage(context.getString(R.string.really_delete_item))
            setIcon(R.drawable.delete)
            setNegativeButton(context.getString(R.string.cancel), null)
            setPositiveButton(context.getString(R.string.ok)) { _, _ ->
                val item = items[position]
                context.deleteById(NASA_PROVIDER_CONTENT_URI, item._id!!)
                File(item.picturePath).delete()
                items.removeAt(position)
                notifyDataSetChanged()
            }
            show()
        }
    }

    override fun getItemCount() = items.count()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvItem = itemView.findViewById<TextView>(R.id.tvItem)
        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)

        fun bind(item: Item) {
            tvItem.text = item.title
            Picasso.get()
                .load(File(item.picturePath))
                .error(R.drawable.nasa)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
        }
    }
}