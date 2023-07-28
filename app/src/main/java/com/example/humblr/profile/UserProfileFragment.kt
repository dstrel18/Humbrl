package com.example.humblr.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.humblr.R
import com.example.humblr.adapter.SubredditInfoAdapter
import com.example.humblr.data.post.PostDto
import com.example.humblr.data.profile.ProfileDto
import com.example.humblr.databinding.FragmentUserAccountBinding
import com.example.humblr.utils.TAG_T
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class UserProfileFragment : Fragment(), SubredditInfoAdapter.TextToolBar {

    private var _binding: FragmentUserAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserViewModel by viewModels()
    private val args: UserProfileFragmentArgs by navArgs()
    private val adapter = SubredditInfoAdapter(this) { item -> onClick(item) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserAccountBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = args.userName

        binding.imageNewsUser.adapter = adapter
        binding.imageNewsUser.layoutManager =
            StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)

        viewModel.getAnotherUserProfile(name)
        viewModel.getUserContent(name)

        viewModel.profileUser.observe(viewLifecycleOwner) {
            showUserInfo(it.data)
            isFriends(it.data.is_friend!!)
        }

        binding.bottomInFriends.setOnClickListener {
            inFriends(name)
        }
        bindViewModel()

    }

    private fun bindViewModel() {
        viewModel.subredditUserInfo.onEach {
            val adapter = adapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun inFriends(name: String) {
        if (!binding.bottomInFriends.isActivated) {
            binding.bottomInFriends.isActivated = true
            viewModel.makeFriends(name)
            binding.textBottomFriends.text =
                getString(R.string.in_friends)
            Snackbar.make(
                binding.root, getString(R.string.friends_now), BaseTransientBottomBar.LENGTH_SHORT
            ).show()
        } else {
            binding.bottomInFriends.isActivated = false
            binding.textBottomFriends.text =
                getString(R.string.subscribe)
            Snackbar.make(
                binding.root,
                getString(R.string.removed_from_friends),
                BaseTransientBottomBar.LENGTH_SHORT
            ).show()

        }
    }

    private fun onClick(item: PostDto) {
        val id = item.data.id.toString()
        findNavController().navigate(
            UserProfileFragmentDirections.actionUserProfilFragmentToPostInfoFragment(
                id
            )
        )
    }

    private fun isFriends(check: Boolean) {
        binding.bottomInFriends.isActivated = check
        if (check) binding.textBottomFriends.text =
            getString(R.string.in_friends) else binding.textBottomFriends.text =
            getText(R.string.subscribe)
    }

    @SuppressLint("SetTextI18n")
    private fun showUserInfo(userInfo: ProfileDto) {

        val comment = resources.getQuantityString(
            R.plurals.count_comment, userInfo.comment_karma!!, userInfo.comment_karma
        )

        binding.nameProfile.text = userInfo.name
        val text = "@${userInfo.name}"
        binding.profileAccount.text = text
        binding.numberComments.text =
            comment
        Glide.with(this).load(userInfo.snoovatar_img).circleCrop().into(binding.imageProfile)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun textToolBar(postDto: PostDto) {

    }
}
