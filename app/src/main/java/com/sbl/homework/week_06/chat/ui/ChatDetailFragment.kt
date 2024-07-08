package com.sbl.homework.week_06.chat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sbl.homework.week_06.R
import com.sbl.homework.week_06.chat.helper.ChatHelper
import com.sbl.homework.week_06.chat.models.ChatListAdapter
import com.sbl.homework.week_06.databinding.FragmentChatDetailBinding
import com.sbl.homework.week_06.frameworks.ui.MainActivity
import com.sbl.homework.week_06.home.models.FriendsDataModel

class ChatDetailFragment(private val friendData: FriendsDataModel) : Fragment() {
    private lateinit var binding: FragmentChatDetailBinding
    private lateinit var adapter: ChatListAdapter

    val helper = ChatHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_detail, container, false)
        binding.view = this
        binding.lifecycleOwner = this
        binding.friendName.text = friendData.name

        inChatRecyclerView()
        //TODO: load earlier messages

        return binding.root
    }

    private fun inChatRecyclerView(){
        adapter = ChatListAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context as MainActivity)
        helper.getMessageList(friendData.uid, completion = {
            if (it){adapter.notifyDataSetChanged()}
            else {
                Toast.makeText(context as MainActivity, "오류: 이전 메세지를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onClick(v: View){
        var myMsgContent = binding.messageInput.text.toString()
        when (v) {
            binding.backBtn ->
                (activity as MainActivity).supportFragmentManager.popBackStack()

            binding.sendButton ->
                if (myMsgContent.isNotBlank()) {
                    helper.sendMessage(friendData.uid, myMsgContent, completion = {
                        if (it) {
                            binding.messageInput.setText("")
                        }else{
                            Toast.makeText(context as MainActivity, "오류: 네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
        }
    }
}