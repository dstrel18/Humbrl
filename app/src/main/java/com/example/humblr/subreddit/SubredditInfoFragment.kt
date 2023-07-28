package com.example.humblr.subreddit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.humblr.adapter.SubredditInfoAdapter
import com.example.humblr.data.post.PostDto
import com.example.humblr.data.subredditsDto.SubredditDto
import com.example.humblr.databinding.FragmentSubredditInfoBinding
import com.example.humblr.utils.NEW
import com.example.humblr.utils.TAG_T
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SubredditInfoFragment : Fragment(), SubredditInfoAdapter.TextToolBar {


    private var _binding: FragmentSubredditInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RibbonViewModel by viewModels()
    private val postAdapter = SubredditInfoAdapter(this) { item -> onClick(item) }
    private val args: SubredditInfoFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubredditInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageList.adapter = postAdapter
        binding.imageList.layoutManager =
            StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)

        viewModel.getSubredditInfo(args.name)
        bindViewModel()
    }

    private fun bindViewModel() {
        binding.textToolbar.text = args.name.substring(2)
        viewModel.subredditInfo.onEach { it ->
            postAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)


    }

    private fun onClick(postDto: PostDto) {
        val id = postDto.data.id.toString()
        findNavController().navigate(
            SubredditInfoFragmentDirections.actionSubredditInfoFragmentToPostInfoFragment(
                id
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun textToolBar(postDto: PostDto) {
        binding.textToolbar.text = postDto.data.subreddit
        val name = postDto.data.author
        findNavController().navigate(
            SubredditInfoFragmentDirections.actionSubredditInfoFragmentToUserProfilFragment(
                name.toString()
            )
        )
    }
}