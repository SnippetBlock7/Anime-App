package com.example.assignment.util

import android.widget.ImageView
import com.bumptech.glide.Glide

fun loadImage(view: ImageView, url: String?) {
    if (url.isNullOrEmpty()) {
        // Handle null case: set a default
        view.setImageResource(android.R.drawable.stat_notify_error)
    } else {
        Glide.with(view.context)
            .load(url)
            .placeholder(android.R.drawable.progress_horizontal)
            .error(android.R.drawable.stat_notify_error)
            .centerCrop()
            .into(view)
    }
}