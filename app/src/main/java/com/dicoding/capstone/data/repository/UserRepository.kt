package com.dicoding.capstone.data.repository

import com.dicoding.capstone.data.user.User
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")

    fun registUser(user: User, callback: (Boolean, String?) -> Unit){
        userCollection.whereEqualTo("username", user.username).get()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    if(it.result?.isEmpty == true){
                        userCollection.add(user)
                            .addOnSuccessListener {
                                callback(true, "Pengguna sukses terdaftar!")
                            }
                            .addOnFailureListener { e ->
                                callback(false, e.message)
                            }
                    } else {
                        callback(false, "Pengguna sudah ada")
                    }
                } else{
                    callback(false, it.exception?.message)
                }
            }
    }

    fun loginUser(username: String, passwod: String, callback: (Boolean, String?) -> Unit){
        userCollection.whereEqualTo("username", username).whereEqualTo("password", passwod).get()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    if (it.result?.isEmpty == false){
                        callback(true, "Login Berhasil")
                    } else {
                        callback(false, it.exception?.message)
                    }
                }
            }
    }
}