package com.dark_phoenix09.app2pcon2k20.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dark_phoenix09.app2pcon2k20.Adapters.ProductAdapter
import com.dark_phoenix09.app2pcon2k20.R
import com.dark_phoenix09.app2pcon2k20.ViewHolders.ProductViewHolder
import com.dark_phoenix09.app2pcon2k20.models.Product
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.fragment_main_dashboard.*
import kotlinx.android.synthetic.main.fragment_main_dashboard.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [mainDashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class mainDashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var myView: View
    lateinit var adapter:FirestoreRecyclerAdapter<Product,ProductViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view:View= inflater.inflate(R.layout.fragment_main_dashboard, container, false)
        myView=view
        //all product recycler view

        var query=FirebaseFirestore.getInstance().collection("products")
        var option=FirestoreRecyclerOptions.Builder<Product>().setQuery(query,Product::class.java).build()
        adapter=ProductAdapter(option,activity)
        myView.all_product_list.layoutManager=LinearLayoutManager(context)
        myView.all_product_list.setHasFixedSize(true)
        myView.all_product_list.adapter=adapter

        //scan initiation
        view.scan_btn.setOnClickListener {
            var integrator=IntentIntegrator.forSupportFragment(this)
            integrator.setOrientationLocked(false)
            integrator.initiateScan()
        }

        myView.accessories_btn.setOnClickListener {
            val fragment=productSectionFragment()
            val argument=Bundle()
            argument.putString("type","mobiles")
            fragment.arguments=argument
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,fragment)?.addToBackStack(null)?.commit()
        }
        myView.appliances_btn.setOnClickListener {
            val fragment=productSectionFragment()
            val argument=Bundle()
            argument.putString("type","appliances")
            fragment.arguments=argument
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,fragment)?.addToBackStack(null)?.commit()
        }
        myView.beauty_btn.setOnClickListener {
            val fragment=productSectionFragment()
            val argument=Bundle()
            argument.putString("type","beauty")
            fragment.arguments=argument
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,fragment)?.addToBackStack(null)?.commit()
        }
        myView.fashion_btn.setOnClickListener {
            val fragment=productSectionFragment()
            val argument=Bundle()
            argument.putString("type","fashion")
            fragment.arguments=argument
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,fragment)?.addToBackStack(null)?.commit()
        }
        myView.grocery_btn.setOnClickListener {
            val fragment=productSectionFragment()
            val argument=Bundle()
            argument.putString("type","grocery")
            fragment.arguments=argument
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,fragment)?.addToBackStack(null)?.commit()
        }
        myView.kitchen_btn.setOnClickListener {
            val fragment=productSectionFragment()
            val argument=Bundle()
            argument.putString("type","chefzone")
            fragment.arguments=argument
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,fragment)?.addToBackStack(null)?.commit()
        }
        myView.offers_btn.setOnClickListener {
            val fragment=productSectionFragment()
            val argument=Bundle()
            argument.putString("type","specialoffer")
            fragment.arguments=argument
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,fragment)?.addToBackStack(null)?.commit()
        }
        myView.stationaty_btn.setOnClickListener {
            val fragment=productSectionFragment()
            val argument=Bundle()
            argument.putString("type","stationary")
            fragment.arguments=argument
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,fragment)?.addToBackStack(null)?.commit()
        }
        myView.toys_btn.setOnClickListener {
            val fragment=productSectionFragment()
            val argument=Bundle()
            argument.putString("type","kidszone")
            fragment.arguments=argument
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,fragment)?.addToBackStack(null)?.commit()
        }


        return view
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==IntentIntegrator.REQUEST_CODE){
            var result:IntentResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
            if(result!=null){
                if(result.contents==null)
                    Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show()
                else {
                    val productFragment=productFragment()
                    var argument =Bundle()
                    argument.putString("code",result.contents)
                    productFragment.arguments=argument
                    activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,productFragment)?.addToBackStack(null)?.commit()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment mainDashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                mainDashboardFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}