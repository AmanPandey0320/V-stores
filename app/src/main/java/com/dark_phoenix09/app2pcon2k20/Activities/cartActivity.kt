package com.dark_phoenix09.app2pcon2k20.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dark_phoenix09.app2pcon2k20.R
import com.dark_phoenix09.app2pcon2k20.fragments.productSectionFragment

class cartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val fragment = productSectionFragment()
        val argument=Bundle()
        argument.putString("type","cart")
        fragment.arguments=argument
        supportFragmentManager.beginTransaction().add(R.id.fragment_container_cart,fragment).commit()
    }
}