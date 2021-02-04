package com.nasabadiam.koopod.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nasabadiam.koopod.databinding.FragmentSearchBinding
import com.nasabadiam.koopod.utils.collectAsState
import com.nasabadiam.koopod.utils.hideKeyboard
import com.nasabadiam.koopod.utils.openKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var viewBinding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSearchBinding.inflate(inflater, container, false).apply {
            viewModel = this@SearchFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.searchEditText.apply {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        viewModel.search(
                            text.toString()
                        )
                        true
                    }
                    else -> false
                }
            }

            requestFocus()
        }
        collectAsState(viewModel.navigation) {
            it?.let { dir ->
                findNavController().navigate(dir)
            }
        }
        collectAsState(viewModel.keyboardEvent) {
            when (it) {
                KeyboardEvent.OPEN -> {
                    viewBinding.searchEditText.openKeyboard()
                }
                KeyboardEvent.CLOSE -> {
                    viewBinding.searchEditText.hideKeyboard()
                }
            }
        }
    }
}