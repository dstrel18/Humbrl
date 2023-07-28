package com.example.humblr.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.humblr.data.profile.ClickedUserProfileDto
import com.example.humblr.data.profile.FriendsListingDto
import com.example.humblr.databinding.ItemFriendsListBinding
import com.example.humblr.databinding.PhotoDownladUserBinding

class FriendsListAdapter(
    val onClick: (ClickedUserProfileDto) -> Unit
) : RecyclerView.Adapter<FriendsViewHolder>() {

    private var data: List<ClickedUserProfileDto> = emptyList()

    fun setData(data: List<ClickedUserProfileDto>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val binding = ItemFriendsListBinding.inflate(LayoutInflater.from(parent.context))
        return FriendsViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val item = data.getOrNull(position)
        with(holder.binding) {
            nameProfile.text = item?.data?.name
            userName.text = "@${item?.data?.name}"

            item?.let {
                Glide.with(imageProfile.context).load(it.data.snoovatar_img).into(imageProfile)
            }
        }
        holder.binding.root.setOnClickListener {
            item?.let {
                onClick(item)
            }
        }
    }

    override fun getItemCount(): Int = data.size

}

class FriendsViewHolder(val binding: ItemFriendsListBinding) : RecyclerView.ViewHolder(binding.root)
