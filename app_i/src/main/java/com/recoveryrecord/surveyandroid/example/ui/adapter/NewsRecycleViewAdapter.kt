package com.recoveryrecord.surveyandroid.example.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.activity.NewsContentActivity
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_ID_KEY
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_MEDIA_KEY
import com.recoveryrecord.surveyandroid.example.config.Constants.NO_VALUE
import com.recoveryrecord.surveyandroid.example.config.Constants.TRIGGER_BY_KEY
import com.recoveryrecord.surveyandroid.example.config.Constants.TRIGGER_BY_SELF
import com.recoveryrecord.surveyandroid.example.model.MediaType
import com.recoveryrecord.surveyandroid.example.model.News
import com.recoveryrecord.surveyandroid.example.util.loadImageWithGlide
import java.text.SimpleDateFormat

class NewsRecycleViewAdapter(
    private val dataModelArrayList: ArrayList<News>,
    private val context: Context,
    private val showImg: Boolean = true,
    private val fragmentManager: FragmentManager,
) : RecyclerView.Adapter<NewsRecycleViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        // passing our layout file for displaying our card item
        return ViewHolder(
            LayoutInflater.from(
                context,
            ).inflate(R.layout.news_rv_item, parent, false),
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        // setting data to our views in Recycler view items.
        val model = dataModelArrayList[position]
        holder.newsTitle.text = model.title
        model.pubDate?.toDate()?.let { date ->
            @SuppressLint("SimpleDateFormat")
            val formatter = SimpleDateFormat("MMM dd HH:mm")
            val formattedDate = formatter.format(date)
            holder.newsPubTime.text = formattedDate
        }

        model.image.takeIf { showImg && it != NO_VALUE }?.let {
            holder.newsImg.adjustViewBounds = true
            holder.newsImg.maxHeight = 200
            loadImageWithGlide(context, it, holder.newsImg, holder.progressBar)
        } ?: run {
            holder.newsImg.visibility = View.GONE
            holder.imgCard.visibility = View.GONE
        }
        holder.newsMedia.text = MediaType.getChinese(model.media)
    }

    override fun getItemCount(): Int {
        // returning the size of array list.
        return dataModelArrayList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val newsTitle: TextView
        val newsPubTime: TextView
        val newsMedia: TextView
        val newsImg: ImageView
        val progressBar: ProgressBar
        val imgCard: CardView

        init {
            // initializing the views of recycler views.
            newsTitle = itemView.findViewById(R.id.text_view_title)
            newsPubTime = itemView.findViewById(R.id.text_view_pubtime)
            newsMedia = itemView.findViewById(R.id.text_view_media)
            newsImg = itemView.findViewById(R.id.imgView)
            progressBar = itemView.findViewById(R.id.loadingProgressBar)
            imgCard = itemView.findViewById(R.id.imgCard)
//            // 點擊項目時
            itemView.setOnClickListener {
                val (_, media, id) = dataModelArrayList[adapterPosition]
                val intent = Intent()
                intent.setClass(context, NewsContentActivity::class.java)
                intent.putExtra(TRIGGER_BY_KEY, TRIGGER_BY_SELF)
                intent.putExtra(NEWS_ID_KEY, id)
                intent.putExtra(NEWS_MEDIA_KEY, media) // english
                context.startActivity(intent)
            }
            // Handle item click within the fragment.
//            itemView.setOnClickListener {
//                val position = absoluteAdapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    val (_, media, id) = dataModelArrayList[position]
//
//                    // Create a bundle to pass data to the fragment.
//                    val bundle = bundleOf(
//                        TRIGGER_BY_KEY to TRIGGER_BY_SELF,
//                        NEWS_ID_KEY to id,
//                        NEWS_MEDIA_KEY to media
//                    )
//
//                    // Create the fragment instance and set the arguments.
//                    val fragment = NewsContentFragment()
//                    fragment.arguments = bundle
//
//                    // Replace the current fragment with the NewsContentFragment.
//                    fragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container, fragment)
//                        .addToBackStack(null) // Optional, to add to the back stack
//                        .commit()
//                }
//            }
        }
    }
}
