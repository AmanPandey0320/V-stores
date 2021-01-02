package com.dark_phoenix09.app2pcon2k20.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dark_phoenix09.app2pcon2k20.Adapters.ProductAdapter
import com.dark_phoenix09.app2pcon2k20.Adapters.cartAdapter
import com.dark_phoenix09.app2pcon2k20.Adapters.orderAdapter
import com.dark_phoenix09.app2pcon2k20.R
import com.dark_phoenix09.app2pcon2k20.ViewHolders.OrderViewHolder
import com.dark_phoenix09.app2pcon2k20.ViewHolders.ProductViewHolder
import com.dark_phoenix09.app2pcon2k20.models.Order
import com.dark_phoenix09.app2pcon2k20.models.Product
import com.dark_phoenix09.app2pcon2k20.models.myorder
import com.dark_phoenix09.app2pcon2k20.myCart.myCartDatabase
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.fragment_main_dashboard.view.*
import kotlinx.android.synthetic.main.fragment_product_section.*
import kotlinx.android.synthetic.main.fragment_product_section.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [productSectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class productSectionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var myView:View
    lateinit var adapter: FirestoreRecyclerAdapter<Product, ProductViewHolder>
    lateinit var oAdapter: FirestoreRecyclerAdapter<myorder,OrderViewHolder>

    val db=FirebaseFirestore.getInstance()
    val mAuth=FirebaseAuth.getInstance()


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
        myView= inflater.inflate(R.layout.fragment_product_section, container, false)

        val type= arguments?.getString("type")

        Toast.makeText(myView.context, type, Toast.LENGTH_SHORT).show()

        if(type.equals("m")){
            val type="beauty"
            var query= FirebaseFirestore.getInstance().collection(type)
            var option= FirestoreRecyclerOptions.Builder<Product>().setQuery(query, Product::class.java).build()
            adapter= ProductAdapter(option,activity)
            //my order
            val oQuery=db.collection("users").document(mAuth.currentUser?.uid.toString()).collection("ordre")
            val oOptions=FirestoreRecyclerOptions.Builder<myorder>().setQuery(oQuery,myorder::class.java).build()
            oAdapter=orderAdapter(oOptions,activity)
            myView.section_product_list.layoutManager=LinearLayoutManager(context)
            myView.section_product_list.setHasFixedSize(true)
            myView.section_product_list.adapter=oAdapter

        }else if(type.equals("cart")){

            val type="beauty"
            var query= FirebaseFirestore.getInstance().collection(type)
            var option= FirestoreRecyclerOptions.Builder<Product>().setQuery(query, Product::class.java).build()
            adapter= ProductAdapter(option,activity)
            //my order
            val oQuery=db.collection("users").document(mAuth.currentUser?.uid.toString()).collection("ordre")
            val oOptions=FirestoreRecyclerOptions.Builder<myorder>().setQuery(oQuery,myorder::class.java).build()
            oAdapter=orderAdapter(oOptions,activity)
            myView.section_product_list.layoutManager=LinearLayoutManager(context)
            myView.section_product_list.setHasFixedSize(true)
            myView.section_product_list.adapter=oAdapter

            Toast.makeText(myView.context, "cart", Toast.LENGTH_SHORT).show()
            var cardDB=Room.databaseBuilder(myView.context,myCartDatabase::class.java,"cart_db").allowMainThreadQueries().build()
            var data=cardDB.cartDAO().getCart()
            val adapter=cartAdapter(data,activity)
            myView.section_product_list.setHasFixedSize(true)
            myView.section_product_list.layoutManager=LinearLayoutManager(myView.context)
            myView.section_product_list.adapter=adapter
            
        }else{
            val oQuery=db.collection("users").document(mAuth.currentUser?.uid.toString()).collection("ordre")
            val oOptions=FirestoreRecyclerOptions.Builder<myorder>().setQuery(oQuery,myorder::class.java).build()
            oAdapter=orderAdapter(oOptions,activity)
            val type=arguments?.getString("type").toString()
            var query= FirebaseFirestore.getInstance().collection(type)
            var option= FirestoreRecyclerOptions.Builder<Product>().setQuery(query, Product::class.java).build()
            adapter= ProductAdapter(option,activity)
            myView.section_product_list.layoutManager=LinearLayoutManager(context)
            myView.section_product_list.setHasFixedSize(true)
            myView.section_product_list.adapter=adapter
        }

        return myView
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
        oAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
        oAdapter.stopListening()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment productSectionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            productSectionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}