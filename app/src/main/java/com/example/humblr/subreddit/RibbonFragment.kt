package com.example.humblr.subreddit

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.humblr.adapter.SubredditAdapter
import com.example.humblr.data.subredditsDto.SubredditDto
import com.example.humblr.databinding.FragmentRibbonBinding
import com.example.humblr.presentation.MainActivity
import com.example.humblr.utils.NEW
import com.example.humblr.utils.POPULAR
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RibbonFragment : Fragment(), SubredditAdapter.Click {


    private var _binding: FragmentRibbonBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RibbonViewModel by viewModels()

    private val postAdapter = SubredditAdapter(this) { item -> onClickItem(item) }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showBottomMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRibbonBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    search(query)
                    bindViewModel()
                }
                return false
            }
        })

        binding.imageList.adapter = postAdapter
        binding.imageList.layoutManager =
            StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)

        viewModel.getSubreddit(NEW)
        bindViewModel()

        binding.newText.setOnClickListener {
            newSubreddit()
            bindViewModel()
        }

        binding.popular.setOnClickListener {
            popularSubreddit()
            bindViewModel()
        }

    }

    private fun bindViewModel() {
        viewModel.subreddit.onEach {
            postAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun newSubreddit() {
        binding.popular.setTextColor(Color.BLACK)
        binding.newText.setTextColor(Color.BLUE)
        viewModel.getSubreddit(NEW)
    }

    private fun popularSubreddit() {
        binding.popular.setTextColor(Color.BLUE)
        binding.newText.setTextColor(Color.BLACK)
        viewModel.getSubreddit(POPULAR)
    }

    private fun search(query: String) {
        binding.popular.setTextColor(Color.BLACK)
        binding.newText.setTextColor(Color.BLACK)
        viewModel.getSearch(query)
    }


    private fun onClickItem(subredditDto: SubredditDto) {
        val id = subredditDto.data.display_name_prefixed
        findNavController().navigate(
            RibbonFragmentDirections.actionRibbonFragmentToSubredditInfoFragment(
                id!!
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(sub: SubredditDto) {
        val isSubscriber = sub.data.user_is_subscriber
        var action = ""
        val name = sub.data.display_name_prefixed

        action = if (isSubscriber == true) {
            "unsub"

        } else {
            "sub"
        }

        viewModel.subscribe(action, name.toString())
    }
}