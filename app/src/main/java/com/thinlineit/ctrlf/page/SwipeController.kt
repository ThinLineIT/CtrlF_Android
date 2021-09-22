package com.thinlineit.ctrlf.page

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.thinlineit.ctrlf.R
import com.thinlineit.ctrlf.page.PageActivity.Companion.dpWidth
import com.thinlineit.ctrlf.page.SwipeController.ItemState.FLATTED
import com.thinlineit.ctrlf.page.SwipeController.ItemState.FLATTING
import com.thinlineit.ctrlf.page.SwipeController.ItemState.FOLDED
import kotlin.math.max

class SwipeController(private val listener: ItemTouchHelperListener) :
    ItemTouchHelper.Callback() {
    private var itemViewState = FOLDED
    private var deleteButton: RectF? = null
    private var modifyButton: RectF? = null
    private var isSwipeDone = false
    private var buttonDrawer = ButtonDrawer()

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int = makeMovementFlags(0, ItemTouchHelper.START)

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int =
        if (isSwipeDone) {
            isSwipeDone = false
            0
        } else super.convertToAbsoluteDirection(flags, layoutDirection)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val isButtonShowing = dX <= EXTEND_BUTTON_WIDTH / 2
        if (itemViewState == FOLDED && isButtonShowing) {
            itemViewState = FLATTING
        }
        val adjustDX =
            if (itemViewState == FLATTED) EXTEND_BUTTON_WIDTH else max(dX, EXTEND_BUTTON_WIDTH)
        setTouchListener(recyclerView, viewHolder)
        buttonDrawer.drawButtons(canvas, viewHolder.itemView)
        super.onChildDraw(
            canvas,
            recyclerView,
            viewHolder,
            adjustDX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ) {
        recyclerView.setOnTouchListener { _, event ->
            when (itemViewState) {
                FOLDED, FLATTING -> {
                    isSwipeDone =
                        event.action == MotionEvent.ACTION_CANCEL ||
                        event.action == MotionEvent.ACTION_UP
                    if (isSwipeDone && itemViewState == FLATTING) {
                        itemViewState = FLATTED
                        setItemsClickable(recyclerView, false)
                    }
                }
                FLATTED -> {
                    clickWhenFlatted(viewHolder, event)
                    initView(recyclerView, viewHolder)
                }
            }
            false
        }
    }

    private fun clickWhenFlatted(
        viewHolder: RecyclerView.ViewHolder,
        event: MotionEvent
    ) {
        if (deleteButton?.contains(event.x, event.y) == true) {
            listener.onDelete(viewHolder.itemView.context)
        } else if (modifyButton?.contains(event.x, event.y) == true) {
            listener.onModify(viewHolder.itemView.context)
        }
    }

    private fun initView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        itemViewState = FOLDED
        isSwipeDone = false
        deleteButton = null
        modifyButton = null
        setItemsClickable(recyclerView, true)
        super.clearView(recyclerView, viewHolder)
    }

    private fun setItemsClickable(recyclerView: RecyclerView, isClickable: Boolean) =
        recyclerView.children.forEach {
            it.isClickable = isClickable
        }

    inner class ButtonDrawer {
        private val margin = 10f
        private val corner = 5f

        private val deleteButtonPaint = Paint().apply {
            color = Color.RED
        }
        private val modifyButtonPaint = Paint().apply {
            color = Color.BLUE
        }
        private val textPaint = Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
            textSize = 25f
        }

        fun drawButtons(canvas: Canvas, itemView: View) {
            deleteButton = RectF(
                itemView.right - dpWidth,
                itemView.top + margin,
                itemView.right - margin,
                itemView.bottom - margin,
            ).also { deleteButton ->
                canvas.drawRoundRect(deleteButton, corner, corner, deleteButtonPaint)
                val text = itemView.context.getString(R.string.general_delete)
                canvas.drawText(
                    text,
                    deleteButton.centerX() - textPaint.measureText(text) / 2,
                    deleteButton.centerY() + textPaint.textSize / 2,
                    textPaint
                )
            }

            modifyButton = RectF(
                itemView.right - (2 * dpWidth),
                itemView.top + margin,
                itemView.right - margin - dpWidth,
                itemView.bottom - margin
            ).also { modifyButton ->
                canvas.drawRoundRect(modifyButton, corner, corner, modifyButtonPaint)
                val text = itemView.context.getString(R.string.general_modify)
                canvas.drawText(
                    text,
                    modifyButton.centerX() - textPaint.measureText(text) / 2,
                    modifyButton.centerY() + textPaint.textSize / 2,
                    textPaint
                )
            }
        }
    }

    enum class ItemState {
        FOLDED, FLATTING, FLATTED
    }

    companion object {
        private val EXTEND_BUTTON_WIDTH by lazy { -2 * dpWidth }
    }
}
