package com.recoveryrecord.surveyandroid.example

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.recoveryrecord.surveyandroid.example.model.News
import com.recoveryrecord.surveyandroid.example.ui.Util.englishToChinese
import java.text.SimpleDateFormat

class NewsRecycleViewAdapter(
    private val dataModelArrayList: ArrayList<News>,
    private val context: Context
): RecyclerView.Adapter<NewsRecycleViewAdapter.ViewHolder>() {
    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // passing our layout file for displaying our card item
        return ViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.news_rv_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // setting data to our views in Recycler view items.
        val model = dataModelArrayList[position]
        holder.newsTitle.text = model.title
        model.pubDate?.toDate()?.let { date ->
            @SuppressLint("SimpleDateFormat") val formatter = SimpleDateFormat("MMM dd HH:mm")
            val formattedDate = formatter.format(date)
            holder.newsPubTime.text = formattedDate
        }

        model.image?.let {
            holder.progressBar.visibility = View.VISIBLE
            holder.newsImg.adjustViewBounds = true
            holder.newsImg.maxHeight = 200
            Glide.with(context) // Pass the context
                .load(it) // Pass the image URL
//                .placeholder(R.drawable.ic_baseline_downloading_24) // Placeholder image while loading (optional)
//                .error(R.drawable.error) // Error image if the download fails (optional)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache strategy (optional)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.newsImg.visibility = View.GONE
                        holder.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.newsImg.visibility = View.VISIBLE
                        holder.progressBar.visibility = View.GONE
                        return false
                    }

                })
                .into(holder.newsImg) // ImageView to load the image into
        } ?: run {
            holder.newsImg.visibility = View.GONE
        }
        holder.newsMedia.text = englishToChinese[model.media] ?: ""
    }

    override fun getItemCount(): Int {
        // returning the size of array list.
        return dataModelArrayList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ViewHolder @RequiresApi(api = Build.VERSION_CODES.O) constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val newsTitle: TextView
        val newsPubTime: TextView
        val newsMedia: TextView
        val newsImg: ImageView
        val progressBar: ProgressBar

        init {
            // initializing the views of recycler views.
            newsTitle = itemView.findViewById(R.id.text_view_title)
            newsPubTime = itemView.findViewById(R.id.text_view_pubtime)
            newsMedia = itemView.findViewById(R.id.text_view_media)
            newsImg = itemView.findViewById(R.id.imgView)
            progressBar = itemView.findViewById(R.id.loadingProgressBar)
            // 點擊項目時
            itemView.setOnClickListener {
                val (_, media, id) = dataModelArrayList[adapterPosition]
                val intent = Intent()
                intent.setClass(context, NewsModuleActivity::class.java)
                intent.putExtra("trigger_by", "self_trigger")
                intent.putExtra("news_id", id)
                intent.putExtra("media", media)
                context.startActivity(intent)
            }
        }
    }
}