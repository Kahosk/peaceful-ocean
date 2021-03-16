package com.example.peacefulocean.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*


class DrawingView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        setupDrawing()
    }

    //drawing path 1
    private var drawPath1: Path? = Path()
    private var erasePath1: Path? = Path()

    //drawing and canvas paint 1
    private var drawPaint1: Paint? = Paint()
    private var erasePaint1: Paint? = Paint()

    //drawing path 2
    private var drawPath2: Path? = Path()
    private var erasePath2: Path? = Path()

    //drawing and canvas paint 2
    private var drawPaint2: Paint? = Paint()
    private var erasePaint2: Paint? = Paint()

    //drawing and canvas paint
    private var canvasPaint: Paint? = Paint(Paint.DITHER_FLAG)

    //initial color
    private val paintColor = -0x9a0000
    private val eraseColor = -0x000FF0


    //canvas
    private var drawCanvas: Canvas? = null

    //canvas bitmap
    private var canvasBitmap: Bitmap? = null


    private fun setupDrawing() {

        canvasPaint = Paint(Paint.DITHER_FLAG);

        setupPaint(drawPaint1!!, 10F, paintColor)
        setupPaint(erasePaint1!!, 15F, eraseColor)
        setupPaint(drawPaint2!!, 10F, eraseColor)
        setupPaint(erasePaint2!!, 15F, paintColor)
    }

    private fun setupPaint(paint:Paint, size:Float, color:Int) {
        paint.color = color

        paint.setAntiAlias(true);
        paint.setStrokeWidth(size);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            canvas.drawBitmap(canvasBitmap!!, 0F, 0F, canvasPaint)
            canvas.drawPath(drawPath1!!, drawPaint1!!)
            canvas.drawPath(erasePath1!!, erasePaint1!!)
            canvas.drawPath(drawPath2!!, drawPaint2!!)
            canvas.drawPath(erasePath2!!, erasePaint2!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val pointerCount = event!!.pointerCount
        var touch1 = false
        var touch2 = false
        for (i in 0 until pointerCount) {
            if (event!!.getPointerId(i) == 0) {
                touch1 = onTouchDelayedEvent(i, event, drawPath1!!, drawPaint1!!, erasePath1!!, erasePaint1!!)
            }
            if (event!!.getPointerId(i) == 1) {
                touch1 = onTouchDelayedEvent(i, event, drawPath2!!, drawPaint2!!, erasePath2!!, erasePaint2!!)
            }
        }
        return touch1
    }

    private fun downEvent(draw:Path, erase:Path, touchX:Float, touchY:Float) {
        draw.moveTo(touchX, touchY)
        erase.moveTo(touchX, touchY)
    }
    private fun moveEvent(draw:Path, erase:Path, touchX:Float, touchY:Float)  {
        draw.lineTo(touchX, touchY)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                erase.lineTo(touchX, touchY)
                invalidate()
            }
        }, 300)
    }
    private fun upEvent(draw:Path, erase:Path, drawPaint:Paint, erasePaint:Paint) {
        drawCanvas!!.drawPath(draw, drawPaint)
        draw.reset()
        Timer().schedule(object : TimerTask() {
            override fun run() {
                drawCanvas!!.drawPath(erase, erasePaint)
                erase.reset()
                invalidate()
            }
        }, 300)
    }

    private fun onTouchDelayedEvent(i:Int, event: MotionEvent?, drawPath: Path, drawPaint: Paint, erasePath: Path, erasePaint: Paint): Boolean {
        val touchX = event!!.getX(i)
        val touchY = event.getY(i)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (event.getPointerId(i) == 0)
                downEvent(drawPath, erasePath, touchX, touchY)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.getPointerId(i) == 1)
                downEvent(drawPath, erasePath, touchX, touchY)
            }
            MotionEvent.ACTION_MOVE -> {
                moveEvent(drawPath, erasePath, touchX, touchY)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                if (event.getPointerId(i) == 1)
                upEvent(drawPath, erasePath, drawPaint, erasePaint)
            }
            MotionEvent.ACTION_UP -> {
                if (event.getPointerId(i) == 0)
                upEvent(drawPath, erasePath, drawPaint, erasePaint)
            }
            else -> return false
        }
        invalidate()
        return true
    }

}