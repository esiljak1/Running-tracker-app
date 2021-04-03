package com.etf.unsa.ba.zavrsnirad_esiljak1

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseDBInteractor {
    private val rootNode = FirebaseDatabase.getInstance()
    private lateinit var database: DatabaseReference

    fun getMyRuns(uid: String){
        database = rootNode.getReference("Runs").child(uid)

        database.get().addOnSuccessListener {
            //Dio koda koji se izvrsava kada se pokupe svi podaci iz baze
        }.addOnFailureListener{
            //Dio koda koji se izvrsava kada dodje do pogreske pri prikupljanju podataka iz baze
        }
    }

    fun postRun(uid: String, run: Run){
        database = rootNode.getReference("Runs")

        database.child(uid).child(run.endOfTheRun.toString()).setValue(run)
    }
}