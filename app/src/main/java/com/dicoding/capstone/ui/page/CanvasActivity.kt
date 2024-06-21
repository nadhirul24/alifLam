package com.dicoding.capstone.ui.page

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.databinding.ActivityCanvasBinding
import com.dicoding.capstone.ui.component.MyCanvas
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class CanvasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCanvasBinding
    private lateinit var myCanvas: MyCanvas
    private lateinit var tflite: Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCanvasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myCanvas = MyCanvas(this)
        binding.canvas.addView(myCanvas)

        val itemText = intent.getStringExtra("item_text") ?: ""
        val itemImage = intent.getIntExtra("item_image_res_id", -1)

        binding.apply {
            undoBtn.setOnClickListener { myCanvas.undo() }
            redoBtn.setOnClickListener { myCanvas.redo() }
            resetBtn.setOnClickListener { myCanvas.reset() }

            arabicName.text = itemText
            arabicImg.setImageResource(itemImage)
            thicknessPaint.max = 50
            thicknessPaint.progress = 6
            thicknessPaint.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    myCanvas.setStrokeWidth(progress.toFloat())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            submitBtn.setOnClickListener { processImage(itemText) }
        }

        // Load TFLite model
        val tfliteModel = loadModelFile("alifLam.tflite")
        tflite = Interpreter(tfliteModel)
    }

    private fun loadModelFile(modelPath: String): ByteBuffer {
        val assetFileDescriptor = assets.openFd(modelPath)
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun processImage(groundTruth: String) {
        // Get the current drawing from MyCanvas as Bitmap
        val bitmap = myCanvas.getBitmap()

        // Resize the bitmap if necessary to match model input size (assuming 32x32 input size)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 32, 32, true)

        // Convert Bitmap to ByteBuffer (model input format)
        val byteBuffer = convertBitmapToByteBuffer(resizedBitmap)

        // Run inference with TensorFlow Lite model
        val inputFeature0 = ByteBuffer.allocateDirect(4 * 32 * 32 * 1).apply {
            order(ByteOrder.nativeOrder())
            put(byteBuffer)
        }

        val outputFeature0 = ByteBuffer.allocateDirect(4 * characters.size).apply {
            order(ByteOrder.nativeOrder())
        }

        tflite.run(inputFeature0, outputFeature0)

        // Process the output buffer (e.g., decode the prediction)
        outputFeature0.rewind()
        val outputArray = FloatArray(characters.size)
        outputFeature0.asFloatBuffer().get(outputArray)

        val predictedLetter = decodePrediction(outputArray)
        val similarityScore = calculateSimilarityScore(predictedLetter, groundTruth)

        // Show result based on similarity score
        if (similarityScore >= 0.5) {
            // Success
            showResult("Success! Predicted: $predictedLetter", true)
        } else {
            // Failure, prompt user to try again
            showResult("Try Again! Predicted: $predictedLetter", false)
        }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * 32 * 32 * 1)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(32 * 32)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        for (pixelValue in intValues) {
            val normalizedPixelValue = ((pixelValue shr 16 and 0xFF) - 127.5f) / 127.5f
            byteBuffer.putFloat(normalizedPixelValue)
        }
        return byteBuffer
    }

    private val characters = arrayOf(
        "Alif", "Lam Alif", "Ta", "Tsa", "Jim", "Ha", "Kha", "Dal", "Djal", "Ra",
        "Sin", "Syin", "Lam", "Mim", "Nun", "Waw", "Ya", "Hamzah", "Ba", "Taa",
        "Tha", "Jeem", "Haa", "Khaa", "Daal", "Dhaal", "Raa", "Zaa", "Seen", "Sheen",
        "Saad", "Daad", "Taa", "Zaa", "Ayn", "Ghayn", "Fa", "Qaf", "Kaf", "Lam",
        "Meem", "Noon", "Ha", "Waw", "Ya"
    )

    private fun decodePrediction(outputData: FloatArray): String {
        val maxIndex = outputData.indices.maxByOrNull { outputData[it] } ?: -1
        return if (maxIndex != -1 && maxIndex < characters.size) {
            characters[maxIndex]
        } else {
            "Unknown"
        }
    }

    private fun calculateSimilarityScore(predicted: String, groundTruth: String): Float {
        return if (predicted == groundTruth) 1.0f else 0.0f
    }

    // CanvasActivity.kt

    private fun showResult(message: String, b: Boolean) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        if (message.startsWith("Success")) {
            val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("last_success_item", message) // Simpan informasi item terakhir yang berhasil
            editor.apply()
        }
    }

}

