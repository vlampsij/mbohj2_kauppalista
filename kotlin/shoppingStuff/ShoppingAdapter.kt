package com.example.kauppalistatodo.shoppingStuff

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kauppalistatodo.R


class ShoppingAdapter(private val shoppingItems: List<ShoppingItem>,
                      var itemListener: OnItemClickListener? = null) :
                        RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shopping_list, parent, false)
        return ShoppingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingAdapter.ShoppingViewHolder, position: Int) {
        holder.bind(shoppingItems[position])
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    interface OnItemClickListener {
        fun onClicked(noteId: Int)
    }

    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tavara: TextView = itemView.findViewById(R.id.tvShopAddItem) //muistion teksti
        private val id: TextView = itemView.findViewById(R.id.tvIdShop) //muistion id
        private val btnDelete: ImageView = itemView.findViewById(R.id.imgDeleteShop) //poista nappula

        fun bind(item: ShoppingItem) {
            tavara.text = item.item
            id.text = item.id.toString()

            // Set click listener for delete button
            btnDelete.setOnClickListener {
                itemListener?.onClicked(item.id)
            }
        }
    }
}
