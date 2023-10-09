package com.recoveryrecord.surveyandroid.example.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recoveryrecord.surveyandroid.example.ItemTouchHelperAdapter
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.model.Media

class MediaRankRecycleViewAdapter(
    private val dataModelArrayList: ArrayList<Media>,
    private val context: Context
) : RecyclerView.Adapter<MediaRankRecycleViewAdapter.ViewHolder>(), ItemTouchHelperAdapter {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.media_rv_item, parent, false)
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (media) = dataModelArrayList[position]
        holder.newsMedia.text = media
        val mediaDrawableId = when (media) {
            "中時" -> R.drawable.ct
            "中央社" -> R.drawable.cna
            "華視" -> R.drawable.cts
            "東森" -> R.drawable.ebc
            "自由時報" -> R.drawable.ltn
            "風傳媒" -> R.drawable.storm
            "聯合" -> R.drawable.udn
            "ettoday" -> R.drawable.ettoday
            "三立" -> R.drawable.setn
            else -> R.drawable.storm // Default drawable
        }

        val bitmap = (context.resources.getDrawable(mediaDrawableId) as BitmapDrawable).bitmap
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 36, 36, true)
        val drawable = BitmapDrawable(context.resources, scaledBitmap)
        holder.mediaIcon.setImageDrawable(drawable)
    }

    override fun getItemCount(): Int {
        return dataModelArrayList.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val fromNote = dataModelArrayList[fromPosition]
        dataModelArrayList.remove(fromNote)
        dataModelArrayList.add(toPosition, fromNote)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemSwiped(position: Int) {}

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsMedia: TextView = itemView.findViewById(R.id.text_view_media_rank)
        val mediaIcon: ImageView = itemView.findViewById(R.id.mediaiconView)
    }
}