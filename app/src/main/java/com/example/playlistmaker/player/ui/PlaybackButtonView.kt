package com.example.playlistmaker.player.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R
import kotlin.math.min

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    var onTouchListener: (() -> Unit)? = null
    private var imageBitmap: Bitmap?
    private val playButton: Bitmap?
    private val pauseButton: Bitmap?
    private var imageRect = RectF(0f, 0f, 0f, 0f)
    private var isPlaying = false

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                playButton = getDrawable(R.styleable.PlaybackButtonView_playbackImagePlayResId)?.toBitmap()
                pauseButton = getDrawable(R.styleable.PlaybackButtonView_playbackImagePauseResId)?.toBitmap()
                imageBitmap = playButton

            } finally {
                recycle()
            }
        }
    }

     fun playButtonImageChange() {
        if(isPlaying) {
            imageBitmap = playButton
        } else {
            imageBitmap = pauseButton
        }
         invalidate()
         isPlaying = !isPlaying
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())

    }
    override fun onDraw(canvas: Canvas?) {
        if (imageBitmap != null) {
            canvas?.drawBitmap(imageBitmap!!, null, imageRect, null)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                playButtonImageChange()
                onTouchListener?.invoke()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }
}