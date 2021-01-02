package com.dark_phoenix09.app2pcon2k20.Adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.dark_phoenix09.app2pcon2k20.R
import com.dark_phoenix09.app2pcon2k20.ViewHolders.ProductViewHolder
import com.dark_phoenix09.app2pcon2k20.fragments.productFragment
import com.dark_phoenix09.app2pcon2k20.models.mycart
import com.squareup.picasso.Picasso

class cartAdapter(val data:Array<mycart>, val activity: FragmentActivity?): RecyclerView.Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.single_product_list_item,parent,false)
        return ProductViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        var model=data.get(position)
        holder.pName.text=model.name
        holder.pPrice.text="Rs. ${model.price}"
        Picasso.get().load(model.image).into(holder.pImage)
        holder.pLayout.setOnClickListener {
            val productFragment= productFragment()
            var argument = Bundle()
            argument.putString("code",model.code)
            productFragment.arguments=argument
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,productFragment)?.addToBackStack(null)?.commit()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}