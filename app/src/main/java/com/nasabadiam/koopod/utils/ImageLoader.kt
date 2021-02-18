package com.nasabadiam.koopod.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

object ImageLoader {

    fun loadImage(
        context: Context,
        imageURI: String,
        target: ImageLoaderTarget
    ) {

        val requestOption = RequestOptions()
        Glide.with(context)
            .asDrawable()
            .load(Uri.parse(imageURI))
            .apply(requestOption).into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    target.onResourceReady(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    target.onLoadCleared(placeholder)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    target.onLoadFailed()
                }
            })
    }
}

interface ImageLoaderTarget {

    fun onResourceReady(resource: Drawable)
    fun onLoadFailed() {}
    fun onLoadCleared(placeholder: Drawable?) {}
}