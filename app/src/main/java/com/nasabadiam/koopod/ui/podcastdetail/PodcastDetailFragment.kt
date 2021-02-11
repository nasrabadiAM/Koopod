package com.nasabadiam.koopod.ui.podcastdetail

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.AppBarLayout
import com.nasabadiam.koopod.R
import com.nasabadiam.koopod.databinding.FragmentPodcastDetailBinding
import com.nasabadiam.koopod.ui.player.PlayerViewModel
import com.nasabadiam.koopod.utils.collectAsState
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs
import kotlin.math.roundToInt

@AndroidEntryPoint
class PodcastDetailFragment : Fragment() {

    private val viewModel: PodcastDetailViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by viewModels()

    private val args by navArgs<PodcastDetailFragmentArgs>()

    private lateinit var viewBinding: FragmentPodcastDetailBinding

    private var expandAvatarSize: Float = 0F
    private var collapseImageSize: Float = 0F
    private var horizontalToolbarAvatarMargin: Float = 0F
    private var cashCollapseState: Pair<Int, Int>? = null
    private var avatarAnimateStartPointY: Float = 0F
    private var avatarCollapseAnimationChangeWeight: Float = 0F
    private var isCalculated = false
    private var verticalToolbarAvatarMargin = 0F

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentPodcastDetailBinding.inflate(inflater, container, false).apply {
            viewModel = this@PodcastDetailFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        initAppBarLayout()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onViewCreated(args.rssLink)
        collectAsState(viewModel.navigation) {
            it?.let { dir ->
                findNavController().navigate(dir)
            }
        }
        collectAsState(viewModel.playPauseAction) {
            playerViewModel.onPlayPauseAction(it)
        }
        collectAsState(playerViewModel.notifyItemPlayed) {
            it?.let { episodeItem -> viewModel.onItemPlayed(episodeItem) }
        }
        collectAsState(playerViewModel.notifyItemPaused) {
            it?.let { episodeItem -> viewModel.onItemPaused(episodeItem) }
        }
        collectAsState(viewModel.notifyItem) { (index, payload) ->
            viewBinding.recyclerView.adapter?.notifyItemChanged(index, payload)
        }
    }

    private fun initAppBarLayout() {
        expandAvatarSize = resources.getDimension(R.dimen.default_expanded_image_size)
        collapseImageSize = resources.getDimension(R.dimen.default_collapsed_image_size)
        horizontalToolbarAvatarMargin = resources.getDimension(R.dimen.default_margin)

        verticalToolbarMargin()
        viewBinding.appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (isCalculated.not()) {
                    avatarAnimateStartPointY = avatarAnimationStartPoint(appBarLayout)
                    avatarCollapseAnimationChangeWeight = 1 / (1 - avatarAnimateStartPointY)
                    verticalToolbarAvatarMargin = verticalToolbarMargin()
                    isCalculated = true
                }
                updateViews(abs(verticalOffset / appBarLayout.totalScrollRange.toFloat()))
            })

    }

    private fun avatarAnimationStartPoint(appBarLayout: AppBarLayout): Float {
        val expandedHeight =
            appBarLayout.height - (expandAvatarSize + horizontalToolbarAvatarMargin)
        return abs(expandedHeight / appBarLayout.totalScrollRange)
    }

    private fun verticalToolbarMargin() = (viewBinding.animToolbar.height - collapseImageSize) * 2

    private fun updateViews(offset: Float) {
        when (offset) {
            in 0.15F..1F -> {
                viewBinding.toolbarTextView.apply {
                    if (visibility != View.VISIBLE) visibility = View.VISIBLE
                    alpha = (1 - offset) * 0.35F
                }
            }

            in 0F..0.15F -> {
                viewBinding.toolbarTextView.alpha = (1f)
                viewBinding.podcastImage.alpha = 1f
            }
        }

        when {
            offset < SWITCH_BOUND -> Pair(TO_EXPANDED, cashCollapseState?.second ?: WAIT_FOR_SWITCH)
            else -> Pair(TO_COLLAPSED, cashCollapseState?.second ?: WAIT_FOR_SWITCH)
        }.apply {
            when {
                cashCollapseState != null && cashCollapseState != this -> {
                    when (first) {
                        TO_EXPANDED -> {
                            viewBinding.podcastImage.translationX = 0F

                            viewBinding.toolbarBackground.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    android.R.color.transparent
                                )
                            )
                            viewBinding.toolbarSingleLineTitleTextView.visibility = View.INVISIBLE
                        }
                        TO_COLLAPSED -> viewBinding.toolbarBackground.apply {
                            alpha = 0F
                            setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    android.R.color.transparent
                                )
                            )
                            animate().setDuration(250).alpha(1.0F)
                            viewBinding.toolbarSingleLineTitleTextView.apply {
                                visibility = View.VISIBLE
                                alpha = 0F
                                animate().setDuration(500).alpha(1.0f)
                            }
                        }
                    }
                    cashCollapseState = Pair(first, SWITCHED)
                }
                else -> {
                    cashCollapseState = Pair(first, WAIT_FOR_SWITCH)
                }
            }

            viewBinding.podcastImage.apply {
                when {
                    offset > avatarAnimateStartPointY -> {
                        val avatarCollapseAnimateOffset = getAvatarCollapseAnimationOffset(offset)
                        val avatarSize = getAvatarSize(avatarCollapseAnimateOffset)
                        this.layoutParams.also {
                            it.height = avatarSize.roundToInt()
                            it.width = avatarSize.roundToInt()
                        }
                        viewBinding.workaroundTextView.setTextSize(
                            TypedValue.COMPLEX_UNIT_PX,
                            offset
                        )

                        this.translationX = getTranslationX(avatarSize, avatarCollapseAnimateOffset)
                        this.translationY = getTranslationY(avatarSize, avatarCollapseAnimateOffset)
                    }
                    else -> this.layoutParams.also {
                        if (it.height != expandAvatarSize.toInt()) {
                            it.height = expandAvatarSize.toInt()
                            it.width = expandAvatarSize.toInt()
                            this.layoutParams = it
                        }
                        translationX = 0f
                    }
                }
            }
        }
    }

    private fun getTranslationY(
        avatarSize: Float,
        avatarCollapseAnimateOffset: Float
    ): Float {
        val height = (viewBinding.animToolbar.height - verticalToolbarAvatarMargin - avatarSize) / 2
        return height * avatarCollapseAnimateOffset
    }

    private fun getTranslationX(
        avatarSize: Float,
        avatarCollapseAnimateOffset: Float
    ): Float {
        val height = viewBinding.appBarLayout.width - horizontalToolbarAvatarMargin - avatarSize
        return (height / 2) * avatarCollapseAnimateOffset
    }

    private fun getAvatarSize(avatarCollapseAnimateOffset: Float): Float {
        val height = expandAvatarSize - collapseImageSize
        return expandAvatarSize - height * avatarCollapseAnimateOffset
    }

    private fun getAvatarCollapseAnimationOffset(offset: Float): Float {
        return (offset - avatarAnimateStartPointY) * avatarCollapseAnimationChangeWeight
    }

    companion object {
        const val SWITCH_BOUND = 0.8f
        const val TO_EXPANDED = 0
        const val TO_COLLAPSED = 1
        const val WAIT_FOR_SWITCH = 0
        const val SWITCHED = 1
    }
}