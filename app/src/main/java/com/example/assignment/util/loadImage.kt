package com.example.assignment.util

import android.widget.ImageView
import com.bumptech.glide.Glide

fun loadImage(view: ImageView, url: String?) {
    if (url.isNullOrEmpty()) {
        // Handle null case: Clear the image or set a default
        view.setImageResource(android.R.drawable.stat_notify_error)
    } else {
        Glide.with(view.context)
            .load(url)
            .placeholder(android.R.drawable.progress_horizontal) // Loading state
            .error(android.R.drawable.stat_notify_error)        // Failed state
            .centerCrop()
            .into(view)
    }
}