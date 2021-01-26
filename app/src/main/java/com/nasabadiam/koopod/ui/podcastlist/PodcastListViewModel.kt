package com.nasabadiam.koopod.ui.podcastlist

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.nasabadiam.koopod.ResourceState
import com.nasabadiam.koopod.utils.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PodcastListViewModel : BaseViewModel() {

    private val _state = MutableStateFlow<ResourceState>(ResourceState.Loading)
    val state: StateFlow<ResourceState> = _state

    private val _data = MutableStateFlow<List<PodcastItem>>(listOf())
    val data: StateFlow<List<PodcastItem>> = _data

    private val _navigation = MutableStateFlow<NavDirections?>(null)
    val navigation: StateFlow<NavDirections?> = _navigation

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    init {
        _state.value = ResourceState.Loading
        viewModelScope.launch {
            delay(1000)
            _state.emit(ResourceState.SuccessEmpty)
            _message.emit("No Data Available to show!")


            ////
            delay(1000)
            _state.emit(ResourceState.Failed)
            _message.emit("Check your Internet connection!")



            delay(1000)
            _state.emit(ResourceState.Success)
            val imageUrl =
                "https://ssl-static.libsyn.com/p/assets/3/2/4/8/32482d0e2dd140d2/channelb-logo-red.png"
            _data.emit(
                listOf(
                    PodcastItem("id1", "پادکست علی بندری", imageUrl, "در هر اپیزود پادکست فارسی چنل\u200Cبی گزارش یک ماجرای واقعی را به نقل از یک رسانه\u200Cی معتبر انگلیسی\u200Cزبان برای شما تعریف می\u200Cکنیم."),
                    PodcastItem("id2", "پادکست ده صبح", imageUrl, "desc"),
                    PodcastItem("id3", " کتابخوان", imageUrl, "desc"),
                    PodcastItem("id4", "سلام به همه", imageUrl, "desc"),
                    PodcastItem("id5", "رادیو کاتلین", imageUrl, "desc"),
                    PodcastItem("id6", "پادکست آن", imageUrl, "desc"),
                    PodcastItem("id7", "عزت نفس", imageUrl, "desc"),
                    PodcastItem("id8", "ناوکست", imageUrl, "desc"),
                    PodcastItem("id8", "چنل بی", imageUrl, "desc"),
                    PodcastItem("id9", "بی پلاس", imageUrl, "desc")
                )
            )
        }
    }

    fun onPodcastItemClicked(item: PodcastItem) {
        viewModelScope.launch {
            _navigation.emit(PodcastListFragmentDirections.toPodCastDetail(item.id))
        }
    }
}

data class PodcastItem(val id: String, val name: String, val image: String, val description: String)