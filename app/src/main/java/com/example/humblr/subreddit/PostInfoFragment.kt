package com.example.humblr.subreddit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.humblr.R
import com.example.humblr.data.post.PostDto
import com.example.humblr.data.post.PostListingDto
import com.example.humblr.databinding.InfoNewsBinding
import com.example.humblr.utils.TAG_T
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostInfoFragment : Fragment() {

    private var _binding: InfoNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RibbonViewModel by viewModels()
    private val args: PostInfoFragmentArgs by navArgs()
    private var nameProfile: String = ""
    private var nameProfileComment = ""
    private var idComment = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = InfoNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val namePost = args.id
        viewModel.getInfoPost(namePost)

        binding.apply {
            nameUser.setOnClickListener {
                onClickName(nameProfileComment)
            }
            name.setOnClickListener {
                onClickName(nameProfile)
            }

            save.setOnClickListener {
                viewModel.savePost("t3_$namePost")
                Snackbar.make(
                    binding.root, getString(R.string.savePost), BaseTransientBottomBar.LENGTH_SHORT
                ).show()
            }
        }

        bindViewModel()
    }


    private fun bindViewModel() {

        viewModel.postInfo.observe(viewLifecycleOwner) {

            val postDto = it.first().data.children.first().data

            showPostInfo(it)
            shareLink(postDto)
            setVote(postDto, binding.likedImage)
            nameProfile = postDto.author!!
            if (postDto.num_comments > 0) {

                val postDtoComment = it.elementAtOrNull(1)?.data?.children?.first()?.data
                idComment = postDtoComment?.name.toString()
                val nameUserComment = postDtoComment?.author
                Log.d(TAG_T, "name - $nameProfile")
                viewModel.getAnotherUserProfile(nameUserComment.toString())
                setVote(postDtoComment!!, binding.likedComments)
                showComment(it)
            }
        }
    }

    private fun setVote(data: PostDto.PostDataDto, btm: ImageView) {
        if (data.likes == null) {
            btm.isActivated = false
        } else {
            btm.isActivated = data.likes!!
        }
        val name = data.name
        var dir = 1
        btm.setOnClickListener {
            btm.isActivated = !btm.isActivated
            if (data.likes == true) {
                dir = 0
            } else {
                dir = 1
            }
            viewModel.setVote(name, dir)
        }
    }

    private fun showPostInfo(postInfo: List<PostListingDto>) {
        val item = postInfo.first().data.children.first().data

        val comment = resources.getQuantityString(
            R.plurals.count_comment, item.num_comments, item.num_comments
        )
        binding.apply {
            textToolbar.text = item.title
            textNews.text = item.selftext
            name.text = item.author
            textCommentsPost.text = comment
            likesNumber.text = item.score.toString()
        }

        Glide.with(this).load(item.url_overridden_by_dest).into(binding.photoImage)
    }

    private fun showComment(postInfo: List<PostListingDto>) {
        binding.bottomComments.isVisible = false
        val comment = postInfo.elementAtOrNull(1)?.data?.children?.first()?.data
        binding.view2.isVisible = true

        viewModel.profile.observe(viewLifecycleOwner) {

            nameProfileComment = it.data.name.toString()
            val photo = it.data.snoovatar_img
            Glide.with(this).load(photo).into(binding.imageProfile)
        }

        binding.apply {
            textComments.text = comment?.body
            nameUser.text = comment?.author
            likesNumberComments.text = comment?.score.toString()
        }

        binding.saveComment.setOnClickListener {
            viewModel.savePost(idComment)
            Snackbar.make(
                binding.root, getString(R.string.saveComment), BaseTransientBottomBar.LENGTH_SHORT
            ).show()

        }
    }

    private fun onClickName(name: String) {
        findNavController().navigate(
            PostInfoFragmentDirections.actionPostInfoFragmentToUserProfilFragment(
                name
            )
        )
    }

    private fun shareLink(data: PostDto.PostDataDto) {
        binding.share.setOnClickListener {
            shareLinkOnSubreddit(getString(R.string.share_url, data.permalink ?: ""))
        }
    }

    private fun shareLinkOnSubreddit(url: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = getString(R.string.share_text)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
