package com.example.humblr.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.humblr.adapter.CommentAdapter
import com.example.humblr.adapter.MyViewHolderInfo
import com.example.humblr.adapter.SubredditInfoAdapter
import com.example.humblr.data.post.PostDto
import com.example.humblr.databinding.FragmentFavoritesBinding
import com.example.humblr.state.LoadState
import com.example.humblr.utils.COMMENTS
import com.example.humblr.utils.LINKS
import com.example.humblr.utils.TAG_T
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FavoritesFragment : Fragment(), SubredditInfoAdapter.TextToolBar {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModels()
    private var activated = false
    private val subscribedAdapter = SubredditInfoAdapter(this) { item -> onClick(item) }
    private val commentAdapter = CommentAdapter {}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageList.layoutManager =
            StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)

        bindViewModelSubreddit(subscribedAdapter, LINKS)

        binding.apply {
            comments.setOnClickListener {
                bindViewModelComment(commentAdapter, COMMENTS)
            }
            subreddits.setOnClickListener {
                bindViewModelSubreddit(subscribedAdapter, LINKS)
            }
        }
    }

    private fun bindViewModelSubreddit(
        adapter: PagingDataAdapter<PostDto, MyViewHolderInfo>,
        type: String,
    ) {
        binding.subreddits.isActivated = true
        binding.comments.isActivated = false
        binding.imageList.adapter = adapter
        viewModel.getSubredditSave(type)
        viewModel.subscribedSave.onEach {
            subscribedAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun bindViewModelComment(
        adapter: PagingDataAdapter<PostDto, MyViewHolderInfo>,
        type: String,
    ) {
        binding.subreddits.isActivated = false
        binding.comments.isActivated = true
        viewModel.getCommentSave(type)
        binding.imageList.adapter = adapter

        viewModel.commentSave.onEach {
            adapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun onClick(item: PostDto) {
        val id = item.data.id
        findNavController().navigate(
            FavoritesFragmentDirections.actionFavoritesFragmentToPostInfoFragment(
                id.toString()
            )
        )
    }

    private fun activated(btm: Button) {
        if (activated) {
            activated = false
            btm.isActivated = activated
        } else {
            activated = true
            btm.isActivated = activated
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun textToolBar(postDto: PostDto) {
    }
}