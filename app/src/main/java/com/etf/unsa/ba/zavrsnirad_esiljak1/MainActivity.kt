package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ba.zavrsnirad_esiljak1.R

class MainActivity : AppCompatActivity() {

    private var startup: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fm = supportFragmentManager

        startup = fm.findFragmentByTag("startup")

        if(startup == null){
            startup = FirstFragment()
            fm.beginTransaction().replace(R.id.view, startup as FirstFragment, "startup").commit()
        }else{
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

    }





}