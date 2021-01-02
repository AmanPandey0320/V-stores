package com.dark_phoenix09.app2pcon2k20.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.single_product_list_item.view.*

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val pImage:ImageView=itemView.img_p
    val pName:TextView=itemView.list_name_txt
    val pPrice:TextView=itemView.list_price_txt
    val pLayout=itemView.list_layout
}