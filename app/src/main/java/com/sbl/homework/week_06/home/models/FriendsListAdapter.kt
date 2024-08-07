package com.sbl.homework.week_06.home.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sbl.homework.week_06.databinding.FriendsListBinding
import com.sbl.homework.week_06.frameworks.ui.MainActivity
import com.sbl.homework.week_06.home.helper.HomeHelper

class FriendsListAdapter():
    RecyclerView.Adapter<FriendsListAdapter.ViewHolder>(){
    interface OnFriendClickListner{
        fun onFriendClick(view: FriendsListBinding, position: Int)
    }
    private lateinit var mOnFriendClickListner: OnFriendClickListner
    fun setOnFriendClickListener(onFriendClickListner: OnFriendClickListner){
        mOnFriendClickListner = onFriendClickListner
    }

    inner class ViewHolder(val binding: FriendsListBinding
    ): RecyclerView.ViewHolder(binding.root){

        init {
            binding.itemListView.setOnClickListener{
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    mOnFriendClickListner.onFriendClick(view = binding, position)
                }
            }
        }

        fun bind(friendsDataModel: FriendsDataModel){
            //binding.friendProfilePicture = friendsDataModel.ProfilePicture
            binding.friendName.text = friendsDataModel.name
            binding.profileMessage.text = friendsDataModel.profileMessage
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FriendsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = HomeHelper.friendsList.value?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(HomeHelper.friendsList.value!![position])
    }
}
