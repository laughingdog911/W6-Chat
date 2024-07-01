package com.sbl.homework.week_06.home.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sbl.homework.week_06.R
import com.sbl.homework.week_06.databinding.FriendsListBinding

class FriendsListAdapter: RecyclerView.Adapter<FriendsListAdapter.ViewHolder>(){
    var friendsList = mutableListOf<FriendsDataModel>()

    inner class ViewHolder(val binding: FriendsListBinding
    ): RecyclerView.ViewHolder(binding.root){
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

    override fun getItemCount(): Int = friendsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(friendsList[position])
    }

}
