package com.recoveryrecord.surveyandroid.example.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.activity.NewsModuleActivity
import com.recoveryrecord.surveyandroid.example.model.News
import java.text.SimpleDateFormat

class ReadingHistoryRecycleViewAdapter    // constructor class for our Adapter
    (private val dataModelArrayList: ArrayList<News>, private val context: Context) :
    RecyclerView.Adapter<ReadingHistoryRecycleViewAdapter.ViewHolder>() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // passing our layout file for displaying our card item
        return ViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.readinghistory_rv_item, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // setting data to our views in Recycler view items.
        val model = dataModelArrayList[position]
        holder.newsTitle.text = model.title
        model.pubDate?.toDate()?.let {
            model.pubDate?.toDate()?.let { date ->
                @SuppressLint("SimpleDateFormat") val formatter = SimpleDateFormat("MMM dd HH:mm")
                val formattedDate = formatter.format(date)
                holder.newsPubTime.text = formattedDate
            }
        }

        holder.newsImg.visibility = View.GONE
        //        if(model.getImage()!=null && model.getImage()!="NA"){
//            new NewsRecycleViewAdapter.DownloadImageTask(holder.newsImg).execute(model.getImage());
//
//            holder.newsImg.setAdjustViewBounds(true);
//            holder.newsImg.setMaxHeight(200);
//        } else {
//            holder.newsImg.setVisibility(View.GONE);
//        }
        var media_name = model.media //from data base
        when (media_name) {
            "cna" -> media_name = "中央社"
            "chinatimes" -> media_name = "中時"
            "cts" -> media_name = "華視"
            "ebc" -> media_name = "東森"
            "ltn" -> media_name = "自由時報"
            "storm" -> media_name = "風傳媒"
            "udn" -> media_name = "聯合"
            "ettoday" -> media_name = "ettoday"
            "setn" -> media_name = "三立"
            else -> {}
        }
        holder.newsMedia.text = media_name
    }

    override fun getItemCount(): Int {
        // returning the size of array list.
        return dataModelArrayList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // creating variables for our
        // views of recycler items.
        val newsTitle: TextView
        val newsPubTime: TextView
        val newsMedia: TextView
        val newsImg: ImageView

        //        private ImageView courseIV;
        init {
            // initializing the views of recycler views.
            newsTitle = itemView.findViewById(R.id.text_view_title)
            newsPubTime = itemView.findViewById(R.id.text_view_pubtime)
            newsMedia = itemView.findViewById(R.id.text_view_media)
            newsImg = itemView.findViewById(R.id.imgView)
            //            courseIV = itemView.findViewById(R.id.idIVimage);
//            itemView.setCardBackgroundColor(Color.parseColor("#e0efff"));
            // 點擊項目時
            itemView.setOnClickListener {
                //                    Toast.makeText(view.getContext(), "click " +getAdapterPosition(),Toast.LENGTH_SHORT).show();
                val (_, media, id) = dataModelArrayList[adapterPosition]
                val intent = Intent()
                intent.setClass(context, NewsModuleActivity::class.java)
                intent.putExtra("trigger_by", "self_trigger")
                intent.putExtra("news_id", id)
                intent.putExtra("media", media)
                Log.d("lognewsselect", "MY ID  $id")
                Log.d("lognewsselect", "MY IDddd  $media")
                context.startActivity(intent)
                //TestActivityRecognitionActivity.this.finish();
            }
        }
    }

    //image download ###############################################################################
//    private class DownloadImageTask(var bmImage: ImageView) : AsyncTask<String?, Void?, Bitmap?>() {
//        protected override fun doInBackground(vararg urls: String): Bitmap? {
//            val urldisplay = urls[0]
//            var mIcon11: Bitmap? = null
//            try {
//                val `in` = URL(urldisplay).openStream()
//                mIcon11 = BitmapFactory.decodeStream(`in`)
//            } catch (e: Exception) {
//                Log.e("Error", e.message!!)
//                e.printStackTrace()
//            }
//            return mIcon11
//        }
//
//        override fun onPostExecute(result: Bitmap?) {
//            bmImage.setImageBitmap(result)
//        }
//    }
}