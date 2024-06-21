package com.dicoding.capstone.ui.page.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dicoding.capstone.R
import com.dicoding.capstone.data.repository.UserRepository
import com.dicoding.capstone.ui.page.LoginActivity
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ProfilFragment : Fragment() {

    private lateinit var selectImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var profileImageView: ImageView
    private lateinit var userRepository: UserRepository
    private lateinit var username: String
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userRepository = UserRepository()

        selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val imageUri = result.data?.data
                imageUri?.let { uploadImageToFirebase(it) }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)

        profileImageView = view.findViewById(R.id.profile_image)
        loadingProgressBar = view.findViewById(R.id.loading_indicator)

        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "") ?: ""

        val welcomeTextView = view.findViewById<TextView>(R.id.username_profile)
        welcomeTextView.text = getString(R.string.welcome_text, username)
        loadProfileImage()

        val editProfileButton = view.findViewById<Button>(R.id.edtProfileBtn)
        editProfileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            selectImageLauncher.launch(intent)
        }
        val aboutButton = view.findViewById<Button>(R.id.aboutBtn)
        aboutButton.setOnClickListener {
            val intent = Intent(requireContext(), AboutActivity::class.java)
            startActivity(intent)
        }


        val logoutButton = view.findViewById<Button>(R.id.logoutBtn)
        logoutButton.setOnClickListener {
            logoutUser()
        }

        return view
    }


    private fun loadProfileImage() {

        userRepository.getUser(username) { user ->
            loadingProgressBar.visibility = View.GONE

            if (user != null && user.profileImageUrl.isNotEmpty()) {
                Glide.with(this)
                    .load(user.profileImageUrl)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(profileImageView)
            } else {
                Glide.with(this)
                    .load(R.drawable.ic_profile)
                    .into(profileImageView)
            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
        val profileImagesRef = storageRef.child("profile_images/${UUID.randomUUID()}.jpg")

        loadingProgressBar.visibility = View.VISIBLE

        val uploadTask = profileImagesRef.putFile(imageUri)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            profileImagesRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                deleteOldProfileImage {
                    saveProfileImageUrl(imageUrl)
                }
            }.addOnFailureListener { e ->
                Log.e("ProfilFragment", "Failed to get download URL", e)
                loadingProgressBar.visibility = View.GONE
            }
        }.addOnFailureListener { e ->
            Log.e("ProfilFragment", "Failed to upload image", e)
            loadingProgressBar.visibility = View.GONE
        }
    }

    private fun deleteOldProfileImage(onDeleted: () -> Unit) {
        userRepository.getUser(username) { user ->
            if (user != null && user.profileImageUrl.isNotEmpty()) {
                val oldImageUrl = user.profileImageUrl
                val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl)
                storageRef.delete().addOnSuccessListener {
                    onDeleted.invoke()
                }.addOnFailureListener { e ->
                    Log.e("ProfilFragment", "Failed to delete old image", e)
                    onDeleted.invoke()
                }
            } else {
                onDeleted.invoke()
            }
        }
    }

    private fun saveProfileImageUrl(imageUrl: String) {
        userRepository.updateProfileImageUrl(username, imageUrl) { success, message ->
            if (success) {
                updateSharedPreferences("profile_image_url", imageUrl)
                loadProfileImage()
            } else {
                Log.e("ProfilFragment", "Failed to save image URL to Firestore: $message")
            }
            loadingProgressBar.visibility = View.GONE
        }
    }

    private fun updateSharedPreferences(key: String, value: String) {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun logoutUser() {
        // Hapus data dari SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("username")
        editor.remove("profile_image_url")
        editor.apply()

        // Kembali ke halaman login
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
