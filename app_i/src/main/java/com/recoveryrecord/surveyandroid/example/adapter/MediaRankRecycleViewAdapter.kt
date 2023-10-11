package com.recoveryrecord.surveyandroid.example.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recoveryrecord.surveyandroid.example.ItemTouchHelperAdapter
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.model.Media
import com.recoveryrecord.surveyandroid.example.ui.MediaType

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
            MediaType.Chinatimes.chineseId -> R.drawable.ct
            MediaType.Cna.chineseId -> R.drawable.cna
            MediaType.Cts.chineseId -> R.drawable.cts
            MediaType.Ebc.chineseId -> R.drawable.ebc
            MediaType.Ltn.chineseId -> R.drawable.ltn
            MediaType.Storm.chineseId -> R.drawable.storm
            MediaType.Udn.chineseId -> R.drawable.udn
            MediaType.Ettoday.chineseId -> R.drawable.ettoday
            MediaType.Setn.chineseId -> R.drawable.setn
            else -> R.drawable.storm // Default drawable
        }
//loadImageWithGlide(context, mediaDrawableId)
//        val bitmap = (context.resources.getDrawable(mediaDrawableId) as BitmapDrawable).bitmap
//        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 36, 36, true)
//        val drawable = BitmapDrawable(context.resources, scaledBitmap)
        holder.mediaIcon.apply {
            setBackgroundResource(mediaDrawableId)
            layoutParams = RelativeLayout.LayoutParams(100, 100)
        }
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