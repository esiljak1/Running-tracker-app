package com.etf.unsa.ba.zavrsnirad_esiljak1

import com.google.firebase.database.*

class FirebaseDBInteractor private constructor(){
    private val rootNode = FirebaseDatabase.getInstance()
    private lateinit var database: DatabaseReference

    companion object{
        val instance = FirebaseDBInteractor()
    }

    fun getMyRuns(uid: String, databaseInterface: DatabaseInterface){
        database = rootNode.getReference("Runs").child(uid)

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                databaseInterface.onSuccess(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                databaseInterface.onFailure()
            }

        })
    }

    fun postRun(uid: String, run: Run){
        database = rootNode.getReference("Runs")

        database.child(uid).push().setValue(run)
    }
}