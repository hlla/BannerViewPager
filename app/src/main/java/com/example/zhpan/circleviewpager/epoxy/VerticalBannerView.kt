package com.example.zhpan.circleviewpager.epoxy

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.example.zhpan.circleviewpager.R
import com.example.zhpan.circleviewpager.adapter.ImageResourceAdapter
import com.example.zhpan.circleviewpager.fragment.EpoxyFragment
import com.example.zhpan.circleviewpager.viewholder.ImageResourceViewHolder
import com.zhpan.bannerview.BannerViewPager

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT, saveViewState = true)
class VerticalBannerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val mViewPagerVertical: BannerViewPager<Int, ImageResourceViewHolder>

    init {
        View.inflate(context, R.layout.vertal_view_item, this)
        mViewPagerVertical = findViewById(R.id.banner_view2)
        mViewPagerVertical.apply {
            setIndicatorSliderGap(resources.getDimensionPixelOffset(R.dimen.dp_4))
            setIndicatorSliderWidth(resources.getDimensionPixelOffset(R.dimen.dp_4), resources.getDimensionPixelOffset(R.dimen.dp_10))
            setIndicatorSliderColor(ContextCompat.getColor(context, R.color.red_normal_color), ContextCompat.getColor(context, R.color.red_checked_color))
            setOrientation(ViewPager2.ORIENTATION_VERTICAL)
            adapter = ImageResourceAdapter(0)
            create()
        }
    }

    @ModelProp
    fun setData(PicList: List<Int>) {
        mViewPagerVertical.refreshData(PicList)
    }

    @ModelProp
    fun setFragment(fragment: EpoxyFragment) {
        mViewPagerVertical.setLifecycleRegistry(fragment.lifecycle)
    }
}