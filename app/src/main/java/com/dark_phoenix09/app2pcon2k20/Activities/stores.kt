package com.dark_phoenix09.app2pcon2k20.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dark_phoenix09.app2pcon2k20.R
import com.dark_phoenix09.app2pcon2k20.fragments.mainDashboardFragment
import com.dark_phoenix09.app2pcon2k20.fragments.productSectionFragment

class stores : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stores)

        var type=intent.getStringExtra("type")
        if(type.equals("s"))
            supportFragmentManager.beginTransaction().add(R.id.fragment_container,mainDashboardFragment()).commit()
        else if(type.equals("c")){
            val fragment = productSectionFragment()
            val argument=Bundle()
            argument.putString("type","cart")
            fragment.arguments=argument
            supportFragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit()
        }else if(type.equals("m")){
            val fragment = productSectionFragment()
            val argument=Bundle()
            argument.putString("type","m")
            fragment.arguments=argument
            supportFragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit()
        }

    }
}