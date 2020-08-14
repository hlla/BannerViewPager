package com.example.zhpan.circleviewpager.epoxy

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.example.zhpan.circleviewpager.R
import com.example.zhpan.circleviewpager.bean.ArticleWrapper

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ArticleView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val tvTitle: TextView
    private val tvAuthor: TextView

    init {
        View.inflate(context, R.layout.article_item_view, this)
        tvTitle = findViewById(R.id.tv_title)
        tvAuthor = findViewById(R.id.tv_auther)
    }

    @ModelProp
    fun setArticle(article: ArticleWrapper.Article) {
        tvTitle.text = article.title
        tvAuthor.text = article.author
    }
}