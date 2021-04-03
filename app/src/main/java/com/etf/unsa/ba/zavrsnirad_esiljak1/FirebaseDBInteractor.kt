package com.etf.unsa.ba.zavrsnirad_esiljak1

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseDBInteractor private constructor(){
    private val rootNode = FirebaseDatabase.getInstance()
    private lateinit var database: DatabaseReference

    companion object{
        val instance = FirebaseDBInteractor()
    }

    fun getMyRuns(uid: String, databaseInterface: DatabaseInterface){
        database = rootNode.getReference("Runs").child(uid)

        database.get().addOnSuccessListener {
            databaseInterface.onSuccess(it.value)
        }.addOnFailureListener{
            databaseInterface.onFailure()
        }
    }

    fun postRun(uid: String, run: Run){
        database = rootNode.getReference("Runs")

        database.child(uid).push().setValue(run)
    }
}