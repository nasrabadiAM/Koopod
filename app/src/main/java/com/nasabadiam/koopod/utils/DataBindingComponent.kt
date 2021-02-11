package com.nasabadiam.koopod.utils

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.nasabadiam.koopod.R
import com.nasabadiam.koopod.ResourceState

class DataBindingComponent {

    companion object {

        @BindingAdapter("app:visibility")
        @JvmStatic
        fun bindVisibility(view: View, input: Any?) {
            view.visibility = when {
                input is Boolean -> if (input) View.VISIBLE else View.GONE
                input is String -> if (input.isEmpty()) View.GONE else View.VISIBLE
                input != null -> View.VISIBLE
                else -> View.GONE
            }
        }

        @BindingAdapter("app:loadingVisibility")
        @JvmStatic
        fun bindLoadingVisibility(view: View, isLoading: Boolean?) {
            return if (isLoading == true) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }

        @BindingAdapter("app:loadingVisibility")
        @JvmStatic
        fun bindLoadingVisibility(view: View, resource: ResourceState?) {
            return when (resource) {
                ResourceState.Loading -> view.visibility = View.VISIBLE
                else -> view.visibility = View.GONE
            }
        }

        @BindingAdapter("app:contentVisibility")
        @JvmStatic
        fun bindContentVisibility(view: View, resource: ResourceState?) {
            return when (resource) {
                ResourceState.Success -> view.visibility = View.VISIBLE
                else -> view.visibility = View.GONE
            }
        }

        @BindingAdapter("app:messageVisibility")
        @JvmStatic
        fun bindMessageVisibility(view: View, resource: ResourceState?) {
            return when (resource) {
                ResourceState.Failed,
                ResourceState.SuccessEmpty -> view.visibility = View.VISIBLE
                else -> view.visibility = View.GONE
            }
        }

        @BindingAdapter("app:imageUrl")
        @JvmStatic
        fun bindImageUrl(view: ImageView, url: String?) {
            Glide.with(view.context)
                .load(url)
                .into(view)
        }

        @BindingAdapter("app:textId")
        @JvmStatic
        fun bindImageUrl(view: TextView, textId: Int?) {
            textId?.let {
                if (textId == 0) return
                view.setText(textId)
            }
        }

        @BindingAdapter("app:htmlText")
        @JvmStatic
        fun bindHtmlText(view: TextView, text: String?) {
            text?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    view.text = Html.fromHtml(text)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("app:setState")
        fun setAnimateOnClick(view: ImageView, isPlaying: Boolean) {
            view.setImageDrawable(null)
            val front: Animatable = ContextCompat.getDrawable(
                view.context,
                R.drawable.ic_play_pause_vector_24_dp
            ) as Animatable
            val back: Animatable = ContextCompat.getDrawable(
                view.context,
                R.drawable.ic_pause_play_vector_24_dp
            ) as Animatable

            if (isPlaying) {
                view.setImageDrawable(front as Drawable)
                front.start()
                view.tag = 0
            } else {
                view.setImageDrawable(back as Drawable)
                (back as Animatable).start()
                view.tag = null
            }
        }

        @JvmStatic
        @BindingAdapter("app:setPlayPause")
        fun setPlayPauseIcon(view: ImageView, isPlaying: Boolean) {
            view.setImageDrawable(null)
            val playing = ContextCompat.getDrawable(
                view.context,
                R.drawable.ic_round_pause_24
            ) as Drawable
            val paused = ContextCompat.getDrawable(
                view.context,
                R.drawable.ic_round_play_arrow_24
            ) as Drawable

            if (isPlaying) {
                view.setImageDrawable(playing)
            } else {
                view.setImageDrawable(paused)
            }
        }
    }
}