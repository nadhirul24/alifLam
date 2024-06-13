package com.dicoding.capstone.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.R

class MainActivity : AppCompatActivity() {
    // Data dummy untuk item
    private val itemTexts = arrayOf("Alif", "Lam Alif", "Ta","Tsa","Jim","Ha","Kha","Dal","Djal","Ra","Lam Alif","Sin","Syin","Lam","Lam Alif","Lam","lam","lam","lam","lam","lam","lam","lam","lam","lam","lam","lam")
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ambil referensi ke GridLayout dari XML
        val gridLayout = findViewById<GridLayout>(R.id.item)

        // Menghitung jumlah kolom yang diinginkan (di sini kita ingin 2 kolom)
        val numColumns = 2

        // Loop untuk membuat item sesuai dengan jumlah data dummy yang Anda miliki
        for (i in itemTexts.indices) {
            // Inflate layout item dari XML
            val itemView: View = LayoutInflater.from(this).inflate(R.layout.grid_item_layout, gridLayout, false   )

            // Ambil referensi ke komponen-komponen di dalam layout item
            val itemImageButton = itemView.findViewById<ImageButton>(R.id.item_image_button)
            val itemTextView = itemView.findViewById<TextView>(R.id.text_alif)

            // Set data dari data dummy ke komponen-komponen tersebut
            itemImageButton.setImageResource(itemImages[i])
            itemTextView.text = itemTexts[i]

            // Hitung posisi kolom dan baris untuk item ini
            val row = i / numColumns
            val col = i % numColumns

            // Tambahkan itemView ke GridLayout dengan menyesuaikan baris dan kolom
            val layoutParams = GridLayout.LayoutParams(

                GridLayout.spec(row, 1f),  // Baris
                GridLayout.spec(col, 1f)   // Kolom
            ).apply {

                setMargins(8,8,8,8)
            }
            layoutParams.width = 0
            layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT

            itemView.layoutParams = layoutParams

            // Tambahkan itemView ke GridLayout
            gridLayout.addView(itemView)
        }
    }
}
