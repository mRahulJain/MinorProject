package com.android.collegeproject.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.collegeproject.R
import com.android.collegeproject.helper.SwipeListener
import com.android.collegeproject.model.NewsModelClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_news_tile.view.*

class NewsAdapter(val context: Context, val newsModelClass: NewsModelClass) :
    RecyclerView.Adapter<NewsAdapter.NameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.activity_news_tile, parent, false)
        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        if(newsModelClass.articles.size < 10) {
            return newsModelClass.articles.size
        }
        return 10
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        val base = newsModelClass.articles[position].urlToImage
        holder.itemView.activity_news_tile_headline.text = newsModelClass.articles[position].title
        holder.itemView.activity_news_tile_headline.isSelected = true
        Picasso.with(context).load(base).into(holder.itemView.activity_news_img)
        holder.itemView.activity_news_tile_description.text = newsModelClass.articles[position].description
        holder.itemView.activity_news_tile_main.setOnTouchListener(object : SwipeListener(context){
            override fun onSwipeBottom() {
            }
            override fun onSwipeLeft() {
            }
            override fun onSwipeTop() {
            }
            override fun onSwipeRight() {
                (context as Activity).finish()
            }
        })
    }


    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}