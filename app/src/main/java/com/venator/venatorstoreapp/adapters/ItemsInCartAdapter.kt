package com.venator.venatorstoreapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venator.venatorstoreapp.Item
import com.venator.venatorstoreapp.R

class ItemsInCartAdapter(private val inflater: LayoutInflater, private val onClick: (Item) -> Unit): ListAdapter<Item, ItemsInCartAdapter.ViewHolder>(
    ItemDiffCallback
) {

    class ViewHolder(view: View, val onClick: (Item) -> Unit) : RecyclerView.ViewHolder(view) {
        private val image = view.findViewById<ImageView>(R.id.image)
        private val title = view.findViewById<TextView>(R.id.title)
        private val category = view.findViewById<TextView>(R.id.category)
        private val cost = view.findViewById<TextView>(R.id.cost)
        private val removeBtn = view.findViewById<ImageButton>(R.id.remove)
        private var item: Item? = null

        init {
            removeBtn.setOnClickListener {
                item?.let {
                    onClick(it)
                }
            }
        }

        fun bind(item: Item) {
            this.item = item
            image.setImageResource(item.imageId)
            title.text = item.title
            category.text = item.category.localizedName.toString()
            cost.text = item.cost.toString() + " зол."
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_in_cart, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    object ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.dateAdded == newItem.dateAdded &&
                    oldItem.imageId == newItem.imageId &&
                    oldItem.title == newItem.title &&
                    oldItem.description == newItem.description &&
                    oldItem.cost == newItem.cost &&
                    oldItem.category == newItem.category &&
                    oldItem.hidden == newItem.hidden
        }
    }
}