package com.dark_phoenix09.app2pcon2k20.fragments

import android.R.attr
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dark_phoenix09.app2pcon2k20.R
import com.dark_phoenix09.app2pcon2k20.models.Order
import com.dark_phoenix09.app2pcon2k20.models.Product
import com.dark_phoenix09.app2pcon2k20.models.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.fragment_payment.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [paymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class paymentFragment : Fragment(), AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var myView:View

    var total:Int=0
    var price:Int=0
    var qty:Int=0
    lateinit var code:String

    val db=FirebaseFirestore.getInstance()
    val mAuth=FirebaseAuth.getInstance()

    lateinit var pName:String

    lateinit var buyer_name:String
    lateinit var buyer_address:String
    lateinit var buyer_no:String

    lateinit var mode:String


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
        myView=inflater.inflate(R.layout.fragment_payment, container, false)
        code= arguments?.getString("code").toString()
        ArrayAdapter.createFromResource(
            myView.context,
            R.array.number,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            myView.quantity_spinner.adapter=adapter
        }
        myView.quantity_spinner.onItemSelectedListener=this

        db.collection("products").document(code).get().addOnCompleteListener {
            if(it.isSuccessful){
                val product=it.result.toObject(Product::class.java)
                pay_name_txt.text=product?.name
                pName= product?.name.toString()
                pay_price_txt.text=product?.price
                price= pay_price_txt.text.toString().toInt()
                total=qty*price
                myView.pay_total_txt.text=total.toString()
                Picasso.get().load(product?.image).into(myView.pay_image)
            }else{
                Log.d("pay product", it.exception?.message.toString())
                Toast.makeText(myView.context, "Error occurred", Toast.LENGTH_SHORT).show()
            }
        }

        db.collection("users").document(mAuth.currentUser?.uid.toString()).get().addOnCompleteListener {
            if(it.isSuccessful){
                val user=it.result.toObject(UserInfo::class.java)
                buyer_name= user?.cName.toString()
                buyer_address= user?.cAddress.toString()
                buyer_no=user?.cMobile_no.toString()

                myView.pay_buyer_name_txt.text=buyer_name
                myView.pay_buyer_address_txt.text=buyer_address
            }else{
                Log.d("baukal", it.exception?.message.toString())
                Toast.makeText(myView.context, "Error", Toast.LENGTH_SHORT).show()
            }
        }

        myView.confirm_payment_btn.setOnClickListener {

            val orderID:String=(mAuth.currentUser?.uid.toString())+(System.currentTimeMillis().toString())

            //UPI Payment
            if(payment_method.checkedRadioButtonId==R.id.g_pay){
                //Toast.makeText(myView.context, "g-pay", Toast.LENGTH_SHORT).show()
                var uri:Uri=Uri.parse("upi://pay").buildUpon()
                    .appendQueryParameter("pn","Dil'e Nadaan")
                    .appendQueryParameter("pa", "rishab007mishra@oksbi")
                    .appendQueryParameter("am", total.toString())
                    .appendQueryParameter("cu", "INR")
                    .build();
                var upiPay= Intent(Intent.ACTION_VIEW)
                upiPay.setData(uri)
                upiPay.`package`="com.google.android.apps.nbu.paisa.user"
                var chooser:Intent= Intent.createChooser(upiPay, "Pay With")
                if(null != chooser.resolveActivity(myView.context.packageManager)) {
                    activity?.startActivityForResult(chooser, 1234);
                } else {
                    Toast.makeText(
                        myView.context,
                        "No UPI app found, please install one to continue",
                        Toast.LENGTH_SHORT
                    ).show();
                }
            }
            else if(payment_method.checkedRadioButtonId==R.id.pop){
                mode="POP"
                val order=Order(
                    orderID,
                    code,
                    qty.toString(),
                    total.toString(),
                    pName,
                    buyer_name,
                    buyer_address,
                    buyer_no,
                    mode,
                    "o"
                )
                db.collection("order").document(orderID).set(order).addOnCompleteListener {
                    if(it.isSuccessful){
                        db.collection("users").document(mAuth.currentUser?.uid.toString()).collection(
                            "ordre"
                        ).document(orderID)
                            .set(order).addOnCompleteListener { task->
                                if(task.isSuccessful){
                                    Toast.makeText(
                                        myView.context,
                                        "Order completed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }else{
                                    Log.d("Makhbool", task.exception?.message.toString())
                                    Toast.makeText(
                                        myView.context,
                                        task.exception?.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }else{
                        Log.d("Lalit", it.exception?.message.toString())
                        Toast.makeText(myView.context, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }else if(payment_method.checkedRadioButtonId==R.id.cod){
                mode="COD"
                val order=Order(
                    orderID,
                    code,
                    qty.toString(),
                    total.toString(),
                    pName,
                    buyer_name,
                    buyer_address,
                    buyer_no,
                    mode,
                    "o"
                )
                db.collection("order").document(orderID).set(order).addOnCompleteListener {
                    if(it.isSuccessful){
                        db.collection("users").document(mAuth.currentUser?.uid.toString()).collection(
                            "ordre"
                        ).document(orderID)
                            .set(order).addOnCompleteListener { task->
                                if(task.isSuccessful){
                                    Toast.makeText(
                                        myView.context,
                                        "Order completed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }else{
                                    Log.d("Bauji", task.exception?.message.toString())
                                    Toast.makeText(
                                        myView.context,
                                        task.exception?.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }else{
                        Log.d("Babar", it.exception?.message.toString())
                        Toast.makeText(myView.context, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return myView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1234){
            if (RESULT_OK === resultCode || resultCode === 11) {
                if (attr.data != null) {
                    val trxt: String = data?.getStringExtra("response").toString()
                    Log.e("UPI", "onActivityResult: $trxt")
                    val dataList: ArrayList<String> = ArrayList()
                    dataList.add(trxt)
                    upiPaymentDataOperation(dataList)
                } else {
                    Log.e("UPI", "onActivityResult: " + "Return data is null")
                    val dataList: ArrayList<String> = ArrayList()
                    dataList.add("nothing")
                    upiPaymentDataOperation(dataList)
                }
            } else {
                //when user simply back without payment
                Log.e("UPI", "onActivityResult: " + "Return data is null")
                val dataList: ArrayList<String> = ArrayList()
                dataList.add("nothing")
                upiPaymentDataOperation(dataList)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment paymentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            paymentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun upiPaymentDataOperation(data: ArrayList<String>) {
        if (isConnectionAvailable(myView.context)) {
            var str = data[0]
            Log.e("UPIPAY", "upiPaymentDataOperation: $str")
            var paymentCancel = ""
            if (str == null) str = "discard"
            var status = ""
            var approvalRefNo = ""
            val response = str.split("&".toRegex()).toTypedArray()
            for (i in response.indices) {
                val equalStr = response[i].split("=".toRegex()).toTypedArray()
                if (equalStr.size >= 2) {
                    if (equalStr[0].toLowerCase() == "Status".toLowerCase()) {
                        status = equalStr[1].toLowerCase()
                    } else if (equalStr[0].toLowerCase() == "ApprovalRefNo".toLowerCase() || equalStr[0].toLowerCase() == "txnRef".toLowerCase()) {
                        approvalRefNo = equalStr[1]
                    }
                } else {
                    paymentCancel = "Payment cancelled by user."
                }
            }
            if (status == "success") {
                //Code to handle successful transaction here.
                Toast.makeText(myView.context, "Transaction successful.", Toast.LENGTH_SHORT)
                    .show()
                //push order

                mode="COD"
                val orderID:String=(mAuth.currentUser?.uid.toString())+(System.currentTimeMillis().toString())
                val order=Order(
                    orderID,
                    code,
                    qty.toString(),
                    total.toString(),
                    pName,
                    buyer_name,
                    buyer_address,
                    buyer_no,
                    mode,
                    "o"
                )
                db.collection("order").document(orderID).set(order).addOnCompleteListener {
                    if(it.isSuccessful){
                        db.collection("users").document(mAuth.currentUser?.uid.toString()).collection(
                            "ordre"
                        ).document(orderID)
                            .set(order).addOnCompleteListener { task->
                                if(task.isSuccessful){
                                    Toast.makeText(
                                        myView.context,
                                        "Order completed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }else{
                                    Log.d("Bauji", task.exception?.message.toString())
                                    Toast.makeText(
                                        myView.context,
                                        task.exception?.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }else{
                        Log.d("Babar", it.exception?.message.toString())
                        Toast.makeText(myView.context, "Error", Toast.LENGTH_SHORT).show()
                    }
                }

                Log.e("UPI", "payment successfull: $approvalRefNo")
            } else if ("Payment cancelled by user." == paymentCancel) {
                Toast.makeText(myView.context, "Payment cancelled by user.", Toast.LENGTH_SHORT)
                    .show()
                Log.e("UPI", "Cancelled by user: $approvalRefNo")
            } else {
                Toast.makeText(
                    myView.context,
                    "Transaction failed.Please try again",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("UPI", "failed payment: $approvalRefNo")
            }
        } else {
            Log.e("UPI", "Internet issue: ")
            Toast.makeText(
                myView.context,
                "Internet connection is not available. Please check and try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun isConnectionAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val netInfo = connectivityManager.activeNetworkInfo
            if (netInfo != null && netInfo.isConnected
                && netInfo.isConnectedOrConnecting
                && netInfo.isAvailable
            ) {
                return true
            }
        }
        return false
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        //TODO("Not yet implemented")
        qty=p2+1
        total=price*qty
        myView.pay_total_txt.text=total.toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //TODO("Not yet implemented")
    }
}