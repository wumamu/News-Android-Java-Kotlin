package com.recoveryrecord.surveyandroid.example.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.recoveryrecord.surveyandroid.example.R

fun loadImageWithGlide(
    context: Context,
    imageUrl: String?,
    imageView: ImageView,
    progressBar: ProgressBar
) {
    progressBar.visibility = View.VISIBLE
    imageView.adjustViewBounds = true
    imageView.maxHeight = 200
    Glide.with(context)
        .load(imageUrl)
//        .placeholder(R.drawable.ic_baseline_downloading_24)
        .error(R.drawable.error)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                // Hide the progress bar when the image loading fails
                progressBar.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                // Hide the progress bar when the image is loaded successfully
                progressBar.visibility = View.GONE
                return false
            }
        })
        .override(500, 0)
        .into(imageView)
}