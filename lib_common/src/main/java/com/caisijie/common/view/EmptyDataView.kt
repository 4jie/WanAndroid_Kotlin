package com.caisijie.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewbinding.ViewBinding
import com.caisijie.common.R
import com.caisijie.common.databinding.ViewEmptyDataBinding

/**
 * @author caisijie
 * @date 2023/9/5
 * @description 空数据的View
 */
class EmptyDataView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var mBinding: ViewEmptyDataBinding

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mBinding = ViewEmptyDataBinding.inflate(LayoutInflater.from(context), this, true)
        obtainAttributes(context, attrs)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?) {
        attrs?.let {
            val ta=context.obtainStyledAttributes(attrs, R.styleable.EmptyDataView)
        }
    }

}