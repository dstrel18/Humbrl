package com.example.humblr.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionScene.Transition.TransitionOnClick
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.humblr.data.post.PostDto
import com.example.humblr.data.subredditsDto.SubredditDto
import com.example.humblr.databinding.MyItemRibbonBinding
import com.example.humblr.databinding.MyItemSubredditInfoBinding
import com.example.humblr.utils.TAG_T

class SubredditInfoAdapter(val text: TextToolBar, val onClick: (PostDto) -> Unit) :
    PagingDataAdapter<PostDto, MyViewHolderInfo>(MyDiffUtilCallback()) {

    override fun onBindViewHolder(holder: MyViewHolderInfo, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            header.text = item?.data?.title
            postCreatorName.text = item?.data?.author
            commentsNumber.text = item?.data?.num_comments.toString()
            signed.isVisible = false
//            text.textToolBar(item!!)
            postCreatorName.setOnClickListener {
                text.textToolBar(item!!)
            }

            item?.let {
                Glide.with(photo).load(it.data.url_overridden_by_dest).into(photo)
            }
        }
        holder.binding.root.setOnClickListener {
            item?.let {
                onClick(item)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderInfo {
        return MyViewHolderInfo(
            MyItemSubredditInfoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    interface TextToolBar {
        fun textToolBar(postDto: PostDto)
    }
}

class MyDiffUtilCallback : DiffUtil.ItemCallback<PostDto>() {
    override fun areItemsTheSame(
        oldItem: PostDto, newItem: PostDto
    ): Boolean = oldItem.data.id == newItem.data.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: PostDto, newItem: PostDto
    ): Boolean = oldItem == newItem
}

class MyViewHolderInfo(val binding: MyItemSubredditInfoBinding) :
    RecyclerView.ViewHolder(binding.root)