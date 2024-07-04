package com.sbl.homework.week_06.chat.models

import android.content.Context
import android.os.Message
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sbl.homework.week_06.R
import com.sbl.homework.week_06.chat.helper.ChatHelper
import com.sbl.homework.week_06.databinding.FriendMessageBinding
import com.sbl.homework.week_06.databinding.MyMessageBinding
import com.sbl.homework.week_06.frameworks.ui.MainActivity
import java.util.concurrent.TimeUnit

class ChatListAdapter(var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var messageMe: String
    private lateinit var messageFriend: String

    var helper = ChatHelper()
    var messages: MutableList<ChatDataModel> = ArrayList()

    fun loadEarlierChat(message: MutableList<ChatDataModel>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    fun addFirst(message: ChatDataModel) {
        messages.add(0, message)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when(viewType) {
            messageMe -> {
                MeUserHolder(layoutInflater.inflate(R.layout.my_message, parent, false))
            }
            messageFriend -> {
                FriendUserHolder(layoutInflater.inflate(R.layout.friend_message, parent, false))
            }
            else -> MeUserHolder(layoutInflater.inflate(R.layout.my_message, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages.get(position)

        when (message) {
            is Us
        }
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            messageMe -> {
                holder as MeUserHolder
                holder.bindView(context, messages.get(position) as Message)
            }
            messageFriend -> {
                holder as FriendUserHolder
                holder.bindView(context, messages.get(position) as Message)
            }
        }
    }

    class MeUserHolder(val bindingMe: MyMessageBinding): RecyclerView.ViewHolder(bindingMe.root) {
        val messageText = bindingMe.MyMessageTextView
        val messageTime = bindingMe.MetextClock

        fun bindView(context: Context, message: Message) {
            messageText.//TODO: 내가 보내는 메세지로 설정
            messageTime.text = // TODO: 내가 문자 보낸 시간 표시
        }
    }

    class FriendUserHolder(val bindingFriend: FriendMessageBinding) :RecyclerView.ViewHolder(bindingFriend.root){
        val messageText = bindingFriend.FriendMessageTextView
        val messageTime = bindingFriend.FriendTextClock

        fun bindView(context: Context, message: Message) {
            messageText.//TODO: 내가 받은 메세지로 설정(친구가 보내는 메세지)

            messageTime.text = DateUtil.formatTime(message.sentAt) // TODO: 친구가 문자를 보낸 시간을 표시
        }
    }

    object DateUtil {
        fun formatTime()
    }
}