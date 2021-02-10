package com.nasabadiam.koopod.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.nasabadiam.koopod.ResourceState

class DataBindingComponent {

    companion object {

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
    }
}