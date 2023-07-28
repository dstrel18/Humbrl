package com.example.humblr.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionScene.Transition.TransitionOnClick
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.humblr.data.subredditsDto.SubredditDto
import com.example.humblr.databinding.MyItemRibbonBinding

class SubredditAdapter(
    val click: Click,
    val onClick: (SubredditDto) -> Unit
) : PagingDataAdapter<SubredditDto, MyViewHolder>(DiffUtilCallback()) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            header.text = item?.data?.title
            postCreatorName.text = item?.data?.display_name
            signed.isActivated = item?.data?.user_is_subscriber!!
            signed.setOnClickListener {
                signed.isActivated = !signed.isActivated

                click.onClick(item)
            }
        }

        holder.binding.root.setOnClickListener {
            item?.let {
                onClick(item)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            MyItemRibbonBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    interface Click {
        fun onClick(sub: SubredditDto)
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<SubredditDto>() {
    override fun areItemsTheSame(
        oldItem: SubredditDto, newItem: SubredditDto
    ): Boolean = oldItem.data.id == newItem.data.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: SubredditDto, newItem: SubredditDto
    ): Boolean = oldItem == newItem
}

class MyViewHolder(val binding: MyItemRibbonBinding) : RecyclerView.ViewHolder(binding.root)