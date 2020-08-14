package com.example.zhpan.circleviewpager.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.airbnb.epoxy.EpoxyRecyclerView
import com.example.zhpan.circleviewpager.R
import com.example.zhpan.circleviewpager.bean.ArticleWrapper
import com.example.zhpan.circleviewpager.bean.DataWrapper
import com.example.zhpan.circleviewpager.epoxy.HorizontalBannerView
import com.example.zhpan.circleviewpager.epoxy.articleView
import com.example.zhpan.circleviewpager.epoxy.horizontalBannerView
import com.example.zhpan.circleviewpager.epoxy.verticalBannerView
import com.example.zhpan.circleviewpager.net.BannerData
import com.example.zhpan.circleviewpager.net.RetrofitGnerator
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.zhpan.idea.net.common.ResponseObserver
import com.zhpan.idea.utils.RxUtil
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_epoxy.*

class EpoxyFragment : BaseFragment() {
    private lateinit var mSmartRefreshLayout: SmartRefreshLayout
    private lateinit var mEpoxyRecyclerView: EpoxyRecyclerView
    override val layout: Int = R.layout.fragment_epoxy
    override fun initTitle() {
    }


    override fun initView(savedInstanceState: Bundle?, view: View) {
        initRecyclerView(view)
        initRefreshLayout(view)
        fetchData(true)
    }

    private fun initRecyclerView(view: View) {
        mEpoxyRecyclerView=view.findViewById(R.id.epoxy_recycler_view)
    }

    private fun initRefreshLayout(view: View) {
        mSmartRefreshLayout=view.findViewById(R.id.refresh_layout)
        mSmartRefreshLayout.setRefreshHeader(MaterialHeader(requireContext()))
        mSmartRefreshLayout.setOnRefreshListener {
            fetchData(false)
        }
    }

    private fun fetchData(showLoading: Boolean) {
        Observable.zip<List<BannerData>, ArticleWrapper, DataWrapper>(getBannerObserver(), getArticleObserver(), BiFunction { bannerData, articles ->
            DataWrapper(articles.datas!!, bannerData)
        }).compose(RxUtil.rxSchedulerHelper(this, showLoading))
                .subscribe(object : ResponseObserver<DataWrapper>() {
                    override fun onSuccess(response: DataWrapper) {
                        if (response.dataBeanList.isEmpty()) {

                        }
                        val bannerData = BannerData().apply {
                            drawable = R.drawable.bg_card0
                            type = BannerData.TYPE_NEW
                            title = "这是一个自定义类型"
                        }

                        val dataList = mutableListOf<BannerData>()
                        dataList.addAll(response.dataBeanList)
                        if (!dataList.isEmpty()) {
                            dataList.add(1, bannerData)
                        }
                        mEpoxyRecyclerView.addItemDecoration(DividerItemDecoration(mContext,
                                DividerItemDecoration.VERTICAL))
                        mEpoxyRecyclerView.withModels {
                            horizontalBannerView {
                                id("horizontalBannerView")
                                fragment(this@EpoxyFragment)
                                data(dataList)
                            }
                            verticalBannerView {
                                id("verticalBannerView")
                                fragment(this@EpoxyFragment)
                                val picList = ArrayList(getPicList(4))
                                data(picList)
                            }
                            for (i in 0..response.articleList.size) {
                                if (i < 4) {
                                    articleView {
                                        id("article", i.toLong())
                                        article(response.articleList[i])
                                    }
                                } else if (i == 4) {
                                    verticalBannerView {
                                        id("verticalBannerView", i.toLong())
                                        fragment(this@EpoxyFragment)
                                        val picList = ArrayList(getPicList(3))
                                        data(picList)
                                    }
                                } else {
                                    articleView {
                                        id("article", (i - 1).toLong())
                                        article(response.articleList[i - 1])
                                    }
                                }

                            }
                        }

                    }

                    override fun onFinish() {
                        mSmartRefreshLayout.finishRefresh()
                    }
                })

    }

    private fun getBannerObserver(): Observable<List<BannerData>> {
        return RetrofitGnerator.getApiSerVice().bannerData.subscribeOn(Schedulers.io())
    }

    private fun getArticleObserver(): Observable<ArticleWrapper> {
        return RetrofitGnerator.getApiSerVice().article.subscribeOn(Schedulers.io())
    }

    companion object {
        fun getInstance() = EpoxyFragment()
    }

}