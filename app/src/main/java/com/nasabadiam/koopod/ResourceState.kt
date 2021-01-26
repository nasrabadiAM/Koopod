package com.nasabadiam.koopod

sealed class ResourceState {
    object Loading : ResourceState()
    object Success : ResourceState()
    object SuccessEmpty : ResourceState()
    object Failed : ResourceState()
}