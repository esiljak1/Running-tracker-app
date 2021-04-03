package com.etf.unsa.ba.zavrsnirad_esiljak1

import com.google.firebase.database.DataSnapshot

interface DatabaseInterface {
    fun onSuccess(snapshot: DataSnapshot?)
    fun onFailure()
}