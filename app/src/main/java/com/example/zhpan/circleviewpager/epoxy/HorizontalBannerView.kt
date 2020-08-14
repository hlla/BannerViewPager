package com.example.zhpan.circleviewpager.epoxy

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.example.zhpan.circleviewpager.R
import com.example.zhpan.circleviewpager.activity.WebViewActivity
import com.example.zhpan.circleviewpager.adapter.HomeAdapter
import com.example.zhpan.circleviewpager.fragment.EpoxyFragment
import com.example.zhpan.circleviewpager.net.BannerData
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.BaseViewHolder
import com.zhpan.idea.utils.ToastUtils
import com.zhpan.indicator.IndicatorView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT, saveViewState = true)
class HorizontalBannerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val mViewPagerHorizontal: BannerViewPager<BannerData, BaseViewHolder<BannerData>>
    private val mIndicator: Group
    private val mIndicatorView: IndicatorView
    private val mTvTitle: TextView

    init {
        View.inflate(context, R.layout.horizontal_view_item, this)
        mViewPagerHorizontal = findViewById(R.id.bvp)
        mIndicator = findViewById(R.id.indicator)
        mIndicatorView = findViewById(R.id.indicator_view)
        mTvTitle = findViewById(R.id.tv_title)
        mViewPagerHorizontal.apply {
            setIndicatorSliderRadius(context.getResources().getDimensionPixelSize(R.dimen.dp_3))
            setIndicatorView(mIndicatorView)// 这里为了设置标题故用了自定义Indicator,如果无需标题则没必要添加此行代码
            setIndicatorSliderColor(ContextCompat.getColor(context, R.color.red_normal_color), ContextCompat.getColor(context, R.color.red_checked_color))
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mTvTitle.text = data[position].title
                }
            })
            setOnPageClickListener { position ->
                val bannerData: BannerData = data[position]
                if (bannerData.type != BannerData.TYPE_NEW) {
                    WebViewActivity.start(context, bannerData.title, bannerData.url)
                } else {
                    Toast.makeText(context, "position:$position ${bannerData.title} currentItem:${currentItem}", Toast.LENGTH_SHORT).show()
                }
            }
            adapter = HomeAdapter()
            create()
        }
    }

    @ModelProp
    fun setData(dataList: List<BannerData>) {
        if (dataList.isEmpty()) {
            mIndicator.visibility = View.GONE
        } else {
            mIndicator.visibility = View.VISIBLE
        }
        mViewPagerHorizontal.refreshData(dataList)
    }

    @ModelProp
    fun setFragment(fragment: EpoxyFragment) {
        mViewPagerHorizontal.setLifecycleRegistry(fragment.lifecycle)
    }

}