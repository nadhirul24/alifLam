package com.dicoding.capstone.data.repository
import com.google.firebase.firestore.FirebaseFirestore

data class User(
    var fullname: String = "",
    var username: String = "",
    var password: String = "",
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