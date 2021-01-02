package com.dark_phoenix09.app2pcon2k20.Activities

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.dark_phoenix09.app2pcon2k20.R
import com.dark_phoenix09.app2pcon2k20.models.UserInfo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_user_profile.address_txt
import kotlinx.android.synthetic.main.activity_user_profile.address_type_radio
import kotlinx.android.synthetic.main.activity_user_profile.area_txt
import kotlinx.android.synthetic.main.activity_user_profile.city
import kotlinx.android.synthetic.main.activity_user_profile.country
import kotlinx.android.synthetic.main.activity_user_profile.home_address
import kotlinx.android.synthetic.main.activity_user_profile.location_btn
import kotlinx.android.synthetic.main.activity_user_profile.office_address
import kotlinx.android.synthetic.main.activity_user_profile.postal_code_txt
import kotlinx.android.synthetic.main.activity_user_profile.profile_alt_number_txt
import kotlinx.android.synthetic.main.activity_user_profile.profile_name_txt
import kotlinx.android.synthetic.main.activity_user_profile.profile_number_txt
import kotlinx.android.synthetic.main.activity_user_profile.profile_pic
import kotlinx.android.synthetic.main.activity_user_profile.save_btn
import kotlinx.android.synthetic.main.activity_user_profile.tnc_check
import java.util.*


class userProfile : AppCompatActivity() {

    val RequestPermissionCode = 1
    var mLocation:Location?=null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var geocoder:Geocoder?=null
    lateinit var address:List<Address>
    val db :FirebaseFirestore = FirebaseFirestore.getInstance()
    val mAuth:FirebaseAuth = FirebaseAuth.getInstance()
    val mStorage =FirebaseStorage.getInstance().getReference()

    var uri:Uri?=null
    var cImage_url:String="default"
    var cName:String="default"
    var cMobile_no:String="default"
    var cAlt_no:String="default"
    var cAddress:String="default"
    var cArea:String="default"
    var cCity:String="default"
    var cCountry:String="default"
    var cPin_code:String="default"
    var cAddressType:String?="default"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this, Locale.getDefault())

        location_btn.setOnClickListener {
            //Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
            getLastLocation()
        }

        //retriving data from the database
        db.collection("users").document(mAuth.currentUser!!.uid).get().addOnSuccessListener {
            if(it.exists()){
                val userInfo = it.toObject(UserInfo::class.java)
                cImage_url=userInfo?.cImage_url.toString()
                if(userInfo?.cImage_url?.equals("default")==false || userInfo?.cImage_url == null || !userInfo.cImage_url.equals("null"))
                    Picasso.get().load(userInfo?.cImage_url).into(profile_pic)
                profile_name_txt.setText(userInfo?.cName)
                profile_number_txt.setText(userInfo?.cMobile_no)
                profile_alt_number_txt.setText(userInfo?.cAlt_no)
                address_txt.setText(userInfo?.cAddress)
                area_txt.setText(userInfo?.cArea)
                city.setText(userInfo?.cCity)
                country.setText(userInfo?.cCountry)
                postal_code_txt.setText(userInfo?.cPin_code)
                if(userInfo?.cAddressType.equals("h"))
                    home_address.isChecked=true
                else
                    office_address.isChecked=true
            }else{
                Toast.makeText(this,"Fill the details",Toast.LENGTH_LONG).show()
            }
        }
        //selecting image
        profile_pic.setOnClickListener{
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),RequestPermissionCode)
            }else{
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this)
            }
        }
        //setting data to the database
        save_btn.setOnClickListener {

            if(!tnc_check.isChecked){
                Toast.makeText(this, "Please accept terms and conditions.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            cAddress=address_txt.text.toString()
            cArea=area_txt.text.toString()
            cCity=city.text.toString()
            cPin_code=postal_code_txt.text.toString()
            cCountry=country.text.toString()
            cName=profile_name_txt.text.toString()
            cMobile_no=profile_number_txt.text.toString()
            cAlt_no=profile_alt_number_txt.text.toString()
            if(address_type_radio.checkedRadioButtonId==R.id.home_address)
                cAddressType="h"
            else
                cAddressType="o"

            if(cAlt_no.isNullOrEmpty())
                cAlt_no="default"
            if(uri!=null){
                var ref=mStorage.child("profilePic").child(mAuth.currentUser?.uid.toString())
                var uploadTask=ref.putFile(uri!!)
                uploadTask.addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        cImage_url= uri.toString()
                        //Toast.makeText(this, "uploaded", Toast.LENGTH_SHORT).show()
                        val userInfo = UserInfo(cImage_url,cName,cMobile_no,cAlt_no,cAddress,cArea,cCity,cCountry,cPin_code,cAddressType)
                        db.collection("users").document(mAuth.currentUser!!.uid).set(userInfo).addOnCompleteListener {
                            if(it.isSuccessful){
                                Toast.makeText(this,"Done!",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,dashboard::class.java))
                                finish()
                            }else{
                                Log.d("firestore",it.exception?.message.toString())
                                Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }else{
                val userInfo = UserInfo(cImage_url,cName,cMobile_no,cAlt_no,cAddress,cArea,cCity,cCountry,cPin_code,cAddressType)
                db.collection("users").document(mAuth.currentUser!!.uid).set(userInfo).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this,"Done!",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,dashboard::class.java))
                        finish()
                    }else{
                        Log.d("firestore",it.exception?.message.toString())
                        Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            var result=CropImage.getActivityResult(data)
            if(resultCode==RESULT_OK){
                profile_pic.setImageURI(result.uri)
                uri=result.uri
            }
        }

    }

    private fun getLastLocation(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),RequestPermissionCode)
            this.recreate()
        }else{
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                mLocation=it
                if(it!=null){
                    var latitude = it.latitude.toString().toDouble()
                    var longitude=it.longitude.toString().toDouble()
                    Toast.makeText(this,"lat: "+ latitude +"longi: "+longitude,Toast.LENGTH_LONG).show()
                   // Log.d("location","done")
                    try{
                        address= geocoder?.getFromLocation(latitude,longitude,1) as List<Address>
                        address_txt.setText(address.get(0).getAddressLine(0))
                        area_txt.setText(address.get(0).locality)
                        city.setText(address.get(0).subAdminArea)
                        country.setText(address.get(0).countryName)
                        postal_code_txt.setText(address.get(0).postalCode)
                    }catch(e:Exception){
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }
}