package com.dark_phoenix09.app2pcon2k20.Adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.dark_phoenix09.app2pcon2k20.R
import com.dark_phoenix09.app2pcon2k20.ViewHolders.ProductViewHolder
import com.dark_phoenix09.app2pcon2k20.fragments.productFragment
import com.dark_phoenix09.app2pcon2k20.models.Product
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.squareup.picasso.Picasso

class ProductAdapter(val options: FirestoreRecyclerOptions<Product>,val activity: FragmentActivity?) :FirestoreRecyclerAdapter<Product,ProductViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        //TODO("Not yet implemented")
        var view:View=LayoutInflater.from(parent.context).inflate(R.layout.single_product_list_item,parent,false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int, model: Product) {
        //TODO("Not yet implemented")
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
}