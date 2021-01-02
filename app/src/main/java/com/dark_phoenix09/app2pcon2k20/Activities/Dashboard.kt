package com.dark_phoenix09.app2pcon2k20.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.dark_phoenix09.app2pcon2k20.R
import com.dark_phoenix09.app2pcon2k20.entryPoint.SignIn
import com.dark_phoenix09.app2pcon2k20.weather.ApiService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class dashboard : AppCompatActivity() {

    val RequestPermissionCode = 1
    var mLocation: Location?=null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var geocoder: Geocoder?=null
    lateinit var address:List<Address>

    var mAuth:FirebaseAuth = FirebaseAuth.getInstance()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        setSupportActionBar(app_toolbar)

        store_card.setOnClickListener {
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,stores::class.java)
            intent.putExtra("type","s")
            startActivity(intent)
        }

        //weather
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this, Locale.getDefault())
        updateWeather()

//        val apiService = ApiService()
//        GlobalScope.launch (Dispatchers.Main){
//            var currentWeather = apiService.getWeather("Asansol").await()
//            Log.d("weather",currentWeather.toString())
//            var image_url= currentWeather.current.weatherIcons[0]
//            Picasso.get().load(image_url).into(weather_img)
//            temp_txt.setText(currentWeather.current.temperature.toString()+" Celcius")
//            feels_like_txt.setText("Feels like "+currentWeather.current.feelslike.toString()+" Celcius")
//            precipitation_txt.setText(currentWeather.current.precip.toString()+" mm")
//            visibility_txt.setText(currentWeather.current.visibility.toString()+" m")
//            wind_txt.setText(currentWeather.current.windSpeed.toString()+" Kph")
//
//        }


        val toggle = ActionBarDrawerToggle(this,drawer_layout,app_toolbar,
            R.string.nav_open,
            R.string.nav_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val listener = NavigationView.OnNavigationItemSelectedListener {
            if(it.itemId == R.id.logout_btn){
                mAuth.signOut()
                startActivity(Intent(this,SignIn::class.java))
                finish()
            }
            if(it.itemId == R.id.setting_btn){
                startActivity(Intent(this, userProfile::class.java))
            }
            if(it.itemId == R.id.my_cart_btn){
                val intent = Intent(this,stores::class.java)
                intent.putExtra("type","c")
                startActivity(intent)
            }
            if(it.itemId==R.id.myorder_btn){
                val intent = Intent(this,stores::class.java)
                intent.putExtra("type","m")
                startActivity(intent)
            }
            return@OnNavigationItemSelectedListener true
        }

        navigation.setNavigationItemSelectedListener(listener)

    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        if(mAuth.currentUser == null){
            Toast.makeText(this, "Sign-In to continue", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,SignIn::class.java))
            finish()
        }else{
            Toast.makeText(this,"Welcome",Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateWeather(){
        Log.d("caught","weather update")
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),RequestPermissionCode)
            this.recreate()
        }else{
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                mLocation=it
                if(it!=null){
                    val latitude = it.latitude.toString().toDouble()
                    val longitude=it.longitude.toString().toDouble()
                    Toast.makeText(this,"lat: "+ latitude +"longi: "+longitude,Toast.LENGTH_LONG).show()
                    // Log.d("location","done")
                    try{
                        address= geocoder?.getFromLocation(latitude,longitude,1) as List<Address>
                        var city=address.get(0).subAdminArea
                        Log.d("city",city)
                        val apiService = ApiService()
                        GlobalScope.launch (Dispatchers.Main){
                            val currentWeather = apiService.getWeather(city).await()
                            Log.d("weather",currentWeather.toString())
                            val image_url= currentWeather.current.weatherIcons[0]
                            Picasso.get().load(image_url).into(weather_img)
                            temp_txt.setText(currentWeather.current.temperature.toString()+" Celcius")
                            feels_like_txt.setText("Feels like "+currentWeather.current.feelslike.toString()+" Celcius")
                            precipitation_txt.setText(currentWeather.current.precip.toString()+" mm")
                            visibility_txt.setText(currentWeather.current.visibility.toString()+" m")
                            wind_txt.setText(currentWeather.current.windSpeed.toString()+" Kph")

                        }
                    }catch(e:Exception){
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

}