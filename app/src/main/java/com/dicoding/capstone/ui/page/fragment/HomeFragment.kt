package com.dicoding.capstone.ui.page.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dicoding.capstone.R
import com.dicoding.capstone.ui.page.CanvasActivity

class HomeFragment : Fragment() {

    // Data dummy untuk item
    private val itemTexts = arrayOf(
        "Alif", "Ba", "Ta", "Tsa", "Jim", "Ha", "Kha", "Dal", "Dzal", "Ra",
        "Za", "Sin", "Syin", "Shad", "Dhad", "Tha", "Zha", "Ain", "Ghain",
        "Fa", "Qaf", "Lam", "Mim", "Nun", "Waw", "Ha", "Ya","Lam","Lam"
    )
    private val itemImages = intArrayOf(
        R.drawable.item_image1, R.drawable.item_image2, R.drawable.item_image3,
        R.drawable.item_image4, R.drawable.item_image5, R.drawable.item_image6,
        R.drawable.item_image7, R.drawable.item_image8, R.drawable.item_image9,
        R.drawable.item_image10, R.drawable.item_image11, R.drawable.item_image12,
        R.drawable.item_image13, R.drawable.item_image14, R.drawable.item_image15,
        R.drawable.item_image16, R.drawable.item_image17, R.drawable.item_image18,
        R.drawable.item_image19, R.drawable.item_image20, R.drawable.item_image21,
        R.drawable.item_image22, R.drawable.item_image23, R.drawable.item_image24,
        R.drawable.item_image25, R.drawable.item_image26, R.drawable.item_image27,
        R.drawable.item_image28,R.drawable.item_image28
    )

    private lateinit var username: String
    private lateinit var profileImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val sharedPreferences =
            requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "") ?: ""

        val welcomeTextView = view.findViewById<TextView>(R.id.username_home)
        welcomeTextView.text = getString(R.string.welcome_text, username)

        profileImageView = view.findViewById(R.id.profile_image)
        loadProfileImage()

        val gridLayout = view.findViewById<GridLayout>(R.id.item)
        val numColumns = 2

        for (i in itemTexts.indices) {
            val itemView: View =
                inflater.inflate(R.layout.grid_item_layout, gridLayout, false)

            val itemImageButton = itemView.findViewById<ImageButton>(R.id.item_image_button)
            val itemTextView = itemView.findViewById<TextView>(R.id.item_text)

            itemImageButton.setImageResource(itemImages[i])
            itemTextView.text = itemTexts[i]

            itemImageButton.setOnClickListener {
                val intent = Intent(requireActivity(), CanvasActivity::class.java).apply {
                    putExtra("item_text", itemTexts[i])
                    putExtra("item_image_res_id", itemImages[i])
                }
                startActivity(intent)
            }

            val row = i / numColumns
            val col = i % numColumns

            val layoutParams = GridLayout.LayoutParams(
                GridLayout.spec(row, 1f),  // Baris
                GridLayout.spec(col, 1f)   // Kolom
            ).apply {
                setMargins(8, 8, 8, 8)
            }
            layoutParams.width = 0
            layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT

            itemView.layoutParams = layoutParams
            gridLayout.addView(itemView)
        }

        return view
    }

    private fun loadProfileImage() {
        val sharedPreferences =
            requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val profileImageUrl = sharedPreferences.getString("profile_image_url", "")

        Glide.with(this)
            .load(profileImageUrl)
            .placeholder(R.drawable.ic_profile)  // Placeholder image while loading or if URL is empty
            .error(R.drawable.ic_profile)        // Image to show if loading fails
            .into(profileImageView)
    }

}

