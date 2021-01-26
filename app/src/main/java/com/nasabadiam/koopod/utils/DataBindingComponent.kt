package com.nasabadiam.koopod.utils

import android.view.View
import androidx.databinding.BindingAdapter
import com.nasabadiam.koopod.ResourceState

class DataBindingComponent {

    companion object {

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
    }
}