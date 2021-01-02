package com.dark_phoenix09.app2pcon2k20.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.dark_phoenix09.app2pcon2k20.R
import com.dark_phoenix09.app2pcon2k20.ViewHolders.OrderViewHolder
import com.dark_phoenix09.app2pcon2k20.models.myorder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class orderAdapter(val options: FirestoreRecyclerOptions<myorder>, val activity: FragmentActivity?): FirestoreRecyclerAdapter<myorder, OrderViewHolder>(options){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.order_single_item,parent,false)
        return OrderViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int, model: myorder) {
        holder.oName.text=model.pname
        holder.oPrice.text="Status : "+model.status
        holder.oQuantity.text=model.quantity
        holder.oTotal.text=model.total
        holder.oAddress.text=model.baddress
        holder.oNumber.text=model.bmobile
        holder.oMode.text=model.pmode
    }
}