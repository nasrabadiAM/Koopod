package com.nasabadiam.koopod.ui.podcastdetail

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
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

@AndroidEntryPoint
class PodcastDetailFragment : Fragment() {

    private val viewModel: PodcastDetailViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by viewModels()

    private val args by navArgs<PodcastDetailFragmentArgs>()

    private lateinit var podcastImage: ImageView
    private var EXPAND_AVATAR_SIZE: Float = 0F
    private var COLLAPSE_IMAGE_SIZE: Float = 0F
    private var horizontalToolbarAvatarMargin: Float = 0F
    private lateinit var toolbar: Toolbar
    private lateinit var appBarLayout: AppBarLayout
    private var cashCollapseState: Pair<Int, Int>? = null
    private lateinit var titleToolbarText: AppCompatTextView
    private lateinit var titleToolbarTextSingle: AppCompatTextView
    private lateinit var invisibleTextViewWorkAround: AppCompatTextView
    private lateinit var background: FrameLayout

    /**/
    private var avatarAnimateStartPointY: Float = 0F
    private var avatarCollapseAnimationChangeWeight: Float = 0F
    private var isCalculated = false
    private var verticalToolbarAvatarMargin = 0F

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPodcastDetailBinding.inflate(inflater, container, false).apply {
            viewModel = this@PodcastDetailFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        EXPAND_AVATAR_SIZE = resources.getDimension(R.dimen.default_expanded_image_size)
        COLLAPSE_IMAGE_SIZE = resources.getDimension(R.dimen.default_collapsed_image_size)
        horizontalToolbarAvatarMargin = resources.getDimension(R.dimen.default_margin)
        /* collapsingAvatarContainer = findViewById(R.id.stuff_container)*/
        appBarLayout = binding.appBarLayout
        toolbar = binding.animToolbar
        podcastImage = binding.podcastImage
        titleToolbarText = binding.tvProfileName
        titleToolbarTextSingle = binding.tvProfileNameSingle
        background = binding.flBackground
        invisibleTextViewWorkAround = binding.tvWorkaround


        (toolbar.height - COLLAPSE_IMAGE_SIZE) * 2
        /**/
        appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
                if (isCalculated.not()) {
                    avatarAnimateStartPointY =
                        Math.abs((appBarLayout.height - (EXPAND_AVATAR_SIZE + horizontalToolbarAvatarMargin)) / appBarLayout.totalScrollRange)
                    avatarCollapseAnimationChangeWeight = 1 / (1 - avatarAnimateStartPointY)
                    verticalToolbarAvatarMargin = (toolbar.height - COLLAPSE_IMAGE_SIZE) * 2
                    isCalculated = true
                }
                /**/
                updateViews(Math.abs(i / appBarLayout.totalScrollRange.toFloat()))
            })

        return binding.root
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
    }

    private fun updateViews(offset: Float) {
        /* apply levels changes*/
        when (offset) {
            in 0.15F..1F -> {
                titleToolbarText.apply {
                    if (visibility != View.VISIBLE) visibility = View.VISIBLE
                    alpha = (1 - offset) * 0.35F
                }
            }

            in 0F..0.15F -> {
                titleToolbarText.alpha = (1f)
                podcastImage.alpha = 1f
            }
        }

        /** collapse - expand switch*/
        when {
            offset < SWITCH_BOUND -> Pair(TO_EXPANDED, cashCollapseState?.second ?: WAIT_FOR_SWITCH)
            else -> Pair(TO_COLLAPSED, cashCollapseState?.second ?: WAIT_FOR_SWITCH)
        }.apply {
            when {
                cashCollapseState != null && cashCollapseState != this -> {
                    when (first) {
                        TO_EXPANDED -> {
                            /* set avatar on start position (center of parent frame layout)*/
                            podcastImage.translationX = 0F
                            /**/
                            background.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    android.R.color.transparent
                                )
                            )
                            /* hide top titles on toolbar*/
                            titleToolbarTextSingle.visibility = View.INVISIBLE
                        }
                        TO_COLLAPSED -> background.apply {
                            alpha = 0F
                            setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    android.R.color.transparent
                                )
                            )
                            animate().setDuration(250).alpha(1.0F)

                            /* show titles on toolbar with animation*/
                            titleToolbarTextSingle.apply {
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

            /* Collapse avatar img*/
            podcastImage.apply {
                when {
                    offset > avatarAnimateStartPointY -> {
                        val avatarCollapseAnimateOffset =
                            (offset - avatarAnimateStartPointY) * avatarCollapseAnimationChangeWeight
                        val avatarSize =
                            EXPAND_AVATAR_SIZE - (EXPAND_AVATAR_SIZE - COLLAPSE_IMAGE_SIZE) * avatarCollapseAnimateOffset
                        this.layoutParams.also {
                            it.height = Math.round(avatarSize)
                            it.width = Math.round(avatarSize)
                        }
                        invisibleTextViewWorkAround.setTextSize(TypedValue.COMPLEX_UNIT_PX, offset)

                        this.translationX =
                            ((appBarLayout.width - horizontalToolbarAvatarMargin - avatarSize) / 2) * avatarCollapseAnimateOffset
                        this.translationY =
                            ((toolbar.height - verticalToolbarAvatarMargin - avatarSize) / 2) * avatarCollapseAnimateOffset
                    }
                    else -> this.layoutParams.also {
                        if (it.height != EXPAND_AVATAR_SIZE.toInt()) {
                            it.height = EXPAND_AVATAR_SIZE.toInt()
                            it.width = EXPAND_AVATAR_SIZE.toInt()
                            this.layoutParams = it
                        }
                        translationX = 0f
                    }
                }
            }
        }
    }

    companion object {
        const val SWITCH_BOUND = 0.8f
        const val TO_EXPANDED = 0
        const val TO_COLLAPSED = 1
        const val WAIT_FOR_SWITCH = 0
        const val SWITCHED = 1
    }
}