package com.dicoding.capstone.data.repository

import com.google.firebase.firestore.FirebaseFirestore

data class User(
    var fullname: String = "",
    var username: String = "",
    var password: String = "",
    var profileImageUrl: String = ""
)

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
                        callback(false, "Nama Pengguna Sudah Digunakan")
                    }
                } else{
                    callback(false, it.exception?.message)
                }
            }
    }

    fun loginUser(username: String, password: String, callback: (Boolean, String?, User?) -> Unit){
        userCollection.whereEqualTo("username", username).whereEqualTo("password", password).get()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    if (it.result?.isEmpty == false){
                        val user = it.result?.documents?.first()?.toObject(User::class.java)
                        callback(true, "Login Berhasil", user)
                    } else {
                        callback(false, "Username atau password salah", null)
                    }
                } else {
                    callback(false, it.exception?.message, null)
                }
            }
    }
    fun updateProfileImageUrl(username: String, imageUrl: String, callback: (Boolean, String?) -> Unit) {
        userCollection.whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    document.reference
                        .update("profileImageUrl", imageUrl)
                        .addOnSuccessListener {
                            callback(true, "Image URL updated successfully")
                        }
                        .addOnFailureListener { e ->
                            callback(false, e.message)
                        }
                } else {
                    callback(false, "User not found")
                }
            }
            .addOnFailureListener { e ->
                callback(false, e.message)
            }
    }
    fun getUser(username: String, callback: (User?) -> Unit) {
        userCollection.whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val user = document.toObject(User::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                callback(null)
            }
    }
}
