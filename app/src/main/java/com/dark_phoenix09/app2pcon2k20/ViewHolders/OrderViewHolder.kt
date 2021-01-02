package com.dark_phoenix09.app2pcon2k20.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.order_single_item.view.*



class OrderViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){
    val oName: TextView =itemView.order_name_txt
    val oPrice: TextView =itemView.order_price_txt
    val oLayout=itemView.order_layout
    val oQuantity=itemView.order_quantity_txt
    val oTotal=itemView.order_total_txt
    val oMode=itemView.order_mode_txt
    val oAddress=itemView.order_address_txt
    val oNumber=itemView.order_no_txt
}