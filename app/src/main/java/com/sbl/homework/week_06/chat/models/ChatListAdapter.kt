package com.sbl.homework.week_06.chat.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sbl.homework.week_06.chat.helper.ChatHelper
import com.sbl.homework.week_06.databinding.FragmentChatDetailBinding
import com.sbl.homework.week_06.databinding.MessageBubbleBinding
import com.sbl.homework.week_06.frameworks.ui.MainActivity

class ChatListAdapter():
        RecyclerView.Adapter<ChatListAdapter.ViewHolder>(){

            inner class ViewHolder(val binding: MessageBubbleBinding
            ): RecyclerView.ViewHolder(binding.root){
                fun bind(chatDataModel: ChatDataModel){
                    if (chatDataModel.isMine.not()){
                        binding.MeMessage.visibility = View.INVISIBLE
                        binding.FriendMessageTextView.text = chatDataModel.message
                        binding.FriendTextClock.text = chatDataModel.sentTime
                    }else{
                        binding.friendsMessage.visibility = View.INVISIBLE
                        binding.MyMessageTextView.text = chatDataModel.message
                        binding.MeTextClock.text = chatDataModel.sentTime
                    }
                }
            }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListAdapter.ViewHolder {
        val binding = MessageBubbleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatListAdapter.ViewHolder, position: Int) {
        holder.bind(ChatHelper.chatList.value!![position])
    }

    override fun getItemCount(): Int = ChatHelper.chatList.value?.size ?: 0

}
