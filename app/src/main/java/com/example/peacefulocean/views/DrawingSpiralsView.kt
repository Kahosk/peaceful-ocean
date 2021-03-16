package com.example.peacefulocean.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * This view allows to draw points onMultitouch
 */

class DrawingSpiralsView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        setupDrawing()
    }

    //Canvas size
    private var widthC = 0
    private var heightC = 0

    private var pointX = 0F
    private var pointY = 0F

    //drawing and canvas paint
    private var canvasPaint: Paint? = Paint(Paint.DITHER_FLAG)

    //canvas
    private var drawCanvas: Canvas? = null

    //canvas bitmap
    private var canvasBitmap: Bitmap? = null

    val colorList = listOf(Color.BLUE,Color.RED,Color.GREEN)


    private fun setupDrawing() {
        canvasPaint = Paint(Paint.DITHER_FLAG)


    }

    private fun setupPaint(paint: Paint, size: Float, color: Int) {
        paint.color = color
        paint.setAntiAlias(true);
        paint.setStrokeWidth(size);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthC = w
        heightC = h
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(canvasBitmap!!, 0F, 0F, canvasPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return onTouchDelayedEvent(event)
    }

    private fun onTouchDelayedEvent(event: MotionEvent?): Boolean {
        val pointerCount = event!!.pointerCount
        for (i in 0 until pointerCount) {
            val colorId:Int = event.getPointerId(i)%3
            drawSpiral(event.getX(i), event.getY(i), 10F, colorList.get(colorId))
            val x = event.getX(i)
            val y = event.getY(i)
            GlobalScope.launch {
                delay(1000)
                drawSpiral(x, y, 20F, Color.BLACK)
            }
        }
        return true
    }

    private fun paintPoint(x: Float, y: Float, size: Float, paintColor: Int) {
        val drawPath = Path()
        val drawPaint = Paint()
        setupPaint(drawPaint, size, paintColor)

        downEvent(drawPath, x, y)
        moveEvent(drawPath, x, y)
        upEvent(drawPath, drawPaint)
        invalidate()
    }
    private fun downEvent(draw: Path, touchX: Float, touchY: Float) {
        draw.moveTo(touchX, touchY)
    }
    private fun moveEvent(draw: Path, touchX: Float, touchY: Float)  {
        draw.lineTo(touchX, touchY)
    }
    private fun upEvent(draw: Path, drawPaint: Paint) {
        drawCanvas!!.drawPath(draw, drawPaint)
        draw.reset()
    }

    private fun drawSpiral(x: Float, y: Float, size: Float, paintColor: Int) {
        // value of theta corresponding to end of last coil
        // value of theta corresponding to end of last coil
        val thetaMax:Float = 15F * Math.PI.toFloat()

        // How far to step away from center for each side.
        val awayStep:Float = 100F / thetaMax

        // distance between points to plot
        val chord = 10F

        // For every side, step around and away from center.
        // start at the angle corresponding to a distance of chord
        // away from centre.

        var theta:Float = chord / awayStep
        while (theta <= thetaMax) {
            // How far away from center
            val away = awayStep * theta
            //
            // How far around the center.
            val around = theta + 10000000
            //
            // Convert 'around' and 'away' to X and Y.
            //
            val pointX = x + Math.cos(around.toDouble()).toFloat() * away
            val pointY = y + Math.sin(around.toDouble()).toFloat() * away
            // Now that you know it, do it.
            GlobalScope.launch {
                delay(0)
                paintPoint(pointX, pointY, size, paintColor)
            }

            // to a first approximation, the points are on a circle
            // so the angle between them is chord/radius
            theta += chord / away
        }

    }

}