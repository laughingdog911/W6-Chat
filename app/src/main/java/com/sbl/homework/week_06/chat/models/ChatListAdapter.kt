package com.sbl.homework.week_06.chat.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sbl.homework.week_06.chat.helper.ChatHelper
import com.sbl.homework.week_06.databinding.FragmentChatDetailBinding
import com.sbl.homework.week_06.databinding.MyMessageBinding
import com.sbl.homework.week_06.frameworks.ui.MainActivity

class ChatListAdapter():
        RecyclerView.Adapter<ChatListAdapter.ViewHolder>(){

            inner class ViewHolder(val binding: MyMessageBinding
            ): RecyclerView.ViewHolder(binding.root){
                fun bind(chatDataModel: ChatDataModel){
                    //binding.MyMessageTextView.text = chatDataModel.message
                    //binding.MeTextClock.text = chatDataModel.sentTime
                    // 내꺼, 너꺼 boolean: T -> my message binding.
                    // F -> friend message binding.
                }
            }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListAdapter.ViewHolder {
        val binding =
    }

    override fun onBindViewHolder(holder: ChatListAdapter.ViewHolder, position: Int) {
        holder.bind(ChatHelper.chatList.value!![position])
    }

    override fun getItemCount(): Int = ChatHelper.chatList.value?.size ?: 0

}
