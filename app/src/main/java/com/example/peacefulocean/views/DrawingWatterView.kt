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

class DrawingWatterView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        setupDrawing()
    }

    //Canvas size
    private var widthC = 0
    private var heightC = 0

    //drawing and canvas paint
    private var canvasPaint: Paint? = Paint(Paint.DITHER_FLAG)

    //canvas
    private var drawCanvas: Canvas? = null

    //canvas bitmap
    private var canvasBitmap: Bitmap? = null

    val colorList = listOf(Color.MAGENTA,Color.YELLOW,Color.GREEN, Color.BLUE,Color.RED,Color.CYAN)


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
            val colorId:Int = event.getPointerId(i)%6
            val x = event.getX(i)
            val y = event.getY(i)
            drawCircle(event.getX(i), event.getY(i), 1F, colorList.get(colorId)*y.toInt()*x.toInt())
            GlobalScope.launch {
                delay(1000)
                drawCircle(x, y, 3F, Color.BLACK)
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

    private fun drawCircle(x: Float, y: Float, size: Float, paintColor: Int) {
        paintPoint(x, y, size, paintColor)
        val drawPaint = Paint()
        var theta:Float = 100F
        while (theta <= widthC || theta <= heightC) {
            // Now that you know it, do it.
            val loop = theta
            GlobalScope.launch {
                setupPaint(drawPaint, size, paintColor)
                delay(loop.toLong())
                drawCanvas!!.drawCircle(x, y, loop, drawPaint)
                invalidate()
            }

            // to a first approximation, the points are on a circle
            // so the angle between them is chord/radius
            theta += 100F
        }

    }

}