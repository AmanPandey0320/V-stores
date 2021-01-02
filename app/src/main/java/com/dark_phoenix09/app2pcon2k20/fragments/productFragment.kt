package com.dark_phoenix09.app2pcon2k20.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.room.Room
import com.dark_phoenix09.app2pcon2k20.R
import com.dark_phoenix09.app2pcon2k20.models.Product
import com.dark_phoenix09.app2pcon2k20.models.mycart
import com.dark_phoenix09.app2pcon2k20.myCart.myCartDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_product.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [productFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class productFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var myView: View

    val db=FirebaseFirestore.getInstance()
    var cProduct:Product?=null
    val mStorage=FirebaseStorage.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView=inflater.inflate(R.layout.fragment_product, container, false)
        val value=arguments?.getString("code")

        db.collection("products").document(value.toString()).get().addOnCompleteListener {
            if(it.isSuccessful){
                val product=it.result.toObject(Product::class.java)
                cProduct=product
                view?.product_name?.text=product?.name
                view?.product_price?.text=product?.price
                view?.product_description?.text=product?.description
                Picasso.get().load(product?.image).into(view?.product_image)
            }else{
                Log.d("product view set",it.exception?.message.toString())
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
            }
        }

        myView.add_to_cart_btn.setOnClickListener {
            val cartDb=Room.databaseBuilder(myView.context,myCartDatabase::class.java,"cart_db").allowMainThreadQueries().build()
            val cart=mycart(cProduct?.image.toString(),cProduct?.name.toString(),cProduct?.description.toString(),cProduct?.price.toString(),cProduct?.code.toString(),cProduct?.type.toString())
            cartDb.cartDAO().insertIntoCart(cart)
            Toast.makeText(myView.context,"Item added to cart",Toast.LENGTH_SHORT).show()
        }

        myView.buy_now_btn.setOnClickListener {
            val paymentFragment=paymentFragment()
            val argument=Bundle()
            argument.putString("code",value)
            paymentFragment.arguments=argument
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,paymentFragment)?.addToBackStack(null)?.commit()
        }

        return myView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment productFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            productFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}