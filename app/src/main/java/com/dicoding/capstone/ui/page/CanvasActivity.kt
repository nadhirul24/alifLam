package com.dicoding.capstone.ui.page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.databinding.ActivityCanvasBinding
import com.dicoding.capstone.ui.component.MyCanvas

class CanvasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCanvasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCanvasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myCanvas = MyCanvas(this)
        binding.canvas.addView(myCanvas)



        binding.undoBtn.setOnClickListener {
            myCanvas.undo()
        }

        binding.redoBtn.setOnClickListener {
            myCanvas.redo()
        }

        binding.resetBtn.setOnClickListener {
            myCanvas.reset()
        }
    }
}