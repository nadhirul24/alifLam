package com.dicoding.capstone.ui.page.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dicoding.capstone.R

class HomeFragment : Fragment() {
    // Data dummy untuk item
    private val itemTexts = arrayOf("Alif", "Lam Alif", "Ta", "Tsa", "Jim", "Ha", "Kha", "Dal", "Djal", "Ra", "Lam Alif", "Sin", "Syin", "Lam", "Lam Alif", "Lam", "lam", "lam", "lam", "lam", "lam", "lam", "lam", "lam", "lam", "lam", "lam")
    private val itemImages = intArrayOf(
        R.drawable.item_image1, R.drawable.item_image2, R.drawable.item_image3,
        R.drawable.item_image4, R.drawable.item_image5, R.drawable.item_image6,
        R.drawable.item_image7, R.drawable.item_image8, R.drawable.item_image9,
        R.drawable.item_image10, R.drawable.item_image11, R.drawable.item_image12,
        R.drawable.item_image13, R.drawable.item_image14, R.drawable.item_image15,
        R.drawable.item_image16, R.drawable.item_image17, R.drawable.item_image18,
        R.drawable.item_image19, R.drawable.item_image20, R.drawable.item_image21,
        R.drawable.item_image22, R.drawable.item_image23, R.drawable.item_image24,
        R.drawable.item_image25, R.drawable.item_image26, R.drawable.item_image27
    )

    private var username: String? = null // Variabel untuk menyimpan username

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menerima username dari argument
        username = arguments?.getString("username")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Atur teks pada welcome_text
        val welcomeTextView = view.findViewById<TextView>(R.id.welcome_text)
        welcomeTextView.text = getString(R.string.welcome_text)

        val gridLayout = view.findViewById<GridLayout>(R.id.item)
        val numColumns = 2

        // Loop untuk membuat item sesuai dengan jumlah data dummy yang Anda miliki
        for (i in itemTexts.indices) {
            // Inflate layout item dari XML
            val itemView: View = inflater.inflate(R.layout.grid_item_layout, gridLayout, false)

            val itemImageButton = itemView.findViewById<ImageButton>(R.id.item_image_button)
            val itemTextView = itemView.findViewById<TextView>(R.id.item_text)

            itemImageButton.setImageResource(itemImages[i])
            itemTextView.text = itemTexts[i]

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


}
