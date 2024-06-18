package com.dicoding.capstone.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import com.dicoding.capstone.R
import kotlin.math.abs


private const val STROKE_WIDTH = 6f
class MyCanvas(context: Context) : View(context) {
    companion object{
        private const val  TAG = "MyCanvas"
    }

    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.white, null)
    private lateinit var canvas: Canvas
    private lateinit var bitmap: Bitmap

    // Paint settings for the pen
    private val penColor = ResourcesCompat.getColor(resources, R.color.black, null)
    private val paint = Paint().apply {
        color = penColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private var path = Path()
    private var motionX = 0f
    private var motionY = 0f
    private var currentX = 0f
    private var currentY = 0f

    private val paths = mutableListOf<Path>()
    private val undonePaths = mutableListOf<Path>()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (::bitmap.isInitialized) bitmap.recycle()

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        canvas.drawColor(backgroundColor)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        for (p in paths) {
            canvas.drawPath(p, paint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        motionX = event!!.x
        motionY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }

    private fun touchStart() {
        path = Path()
        path.moveTo(motionX, motionY)
        currentX = motionX
        currentY = motionY
        paths.add(path)
    }

    private fun touchMove() {
        val dx = abs(motionX - currentX)
        val dy = abs(motionY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            path.quadTo(currentX, currentY, (motionX + currentX) / 2, (motionY + currentY) / 2)
            currentX = motionX
            currentY = motionY
        }
        invalidate()
    }

    private fun touchUp() {
        path.lineTo(currentX, currentY)
    }

    fun undo() {
        if (paths.isNotEmpty()) {
            val lastPath = paths.removeAt(paths.size - 1)
            undonePaths.add(lastPath)
            invalidate()

        }
        Log.d(TAG, "Undo button clicked")
    }

    fun redo() {
        if (undonePaths.isNotEmpty()) {
            val lastUndonePath = undonePaths.removeAt(undonePaths.size - 1)
            paths.add(lastUndonePath)
            invalidate()
        }
        Log.d(TAG, "Redo button clicked")
    }

    fun reset() {
        paths.clear()
        undonePaths.clear()
        canvas.drawColor(backgroundColor)
        invalidate()
    }
}