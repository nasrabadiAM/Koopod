package com.nasabadiam.koopod.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout

class FlingBehavior constructor(
    context: Context?,
    attrs: AttributeSet?
) : AppBarLayout.Behavior(context, attrs) {

    private var isPositive = false

    override fun onNestedFling(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        velocityX: Float,
        velocityYInput: Float,
        consumedInput: Boolean
    ): Boolean {
        var velocityOfY = velocityYInput
        var consumed = consumedInput
        if (velocityYInput > 0 && !isPositive || velocityYInput < 0 && isPositive) {
            velocityOfY = velocityYInput * -1
        }
        if (target is RecyclerView && velocityOfY < 0) {
            val firstChild = target.getChildAt(0)
            val childAdapterPosition = target.getChildAdapterPosition(firstChild)
            consumed = childAdapterPosition > TOP_CHILD_FLING_THRESHOLD
        }
        return super.onNestedFling(
            coordinatorLayout,
            child,
            target,
            velocityX,
            velocityOfY,
            consumed
        )
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        isPositive = dy > 0
    }

    companion object {
        private const val TOP_CHILD_FLING_THRESHOLD = 3
    }
}