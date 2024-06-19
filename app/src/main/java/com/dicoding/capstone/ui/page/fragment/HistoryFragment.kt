package com.dicoding.capstone.ui.page.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dicoding.capstone.R

class HistoryFragment : Fragment() {

    private lateinit var profileImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")

        val welcomeTextView = view.findViewById<TextView>(R.id.username_history)
        welcomeTextView.text = getString(R.string.welcome_text, username)

        profileImageView = view.findViewById(R.id.profile_image)
        loadProfileImage()

        return view
    }

    private fun loadProfileImage() {
        // Ambil URL foto profil dari SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val profileImageUrl = sharedPreferences.getString("profile_image_url", "")

        if (profileImageUrl != null && profileImageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(profileImageUrl)
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
