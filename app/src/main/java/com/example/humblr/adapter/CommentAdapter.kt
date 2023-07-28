package com.example.humblr.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import com.bumptech.glide.Glide
import com.example.humblr.data.post.PostDto
import com.example.humblr.databinding.MyItemSubredditInfoBinding

class CommentAdapter(val onClick: (PostDto) -> Unit) :
    PagingDataAdapter<PostDto, MyViewHolderInfo>(MyDiffUtilCallback()) {

    override fun onBindViewHolder(holder: MyViewHolderInfo, position: Int) {
        val item = getItem(position)
        with(holder.binding) {

            header.text = item?.data?.body
            postCreatorName.text = item?.data?.author
            comments.isVisible = false
            signed.isVisible = false
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
}