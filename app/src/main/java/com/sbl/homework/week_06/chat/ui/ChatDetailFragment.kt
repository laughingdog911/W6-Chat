package com.sbl.homework.week_06.chat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sbl.homework.week_06.R
import com.sbl.homework.week_06.chat.helper.ChatHelper
import com.sbl.homework.week_06.chat.models.ChatDataModel
import com.sbl.homework.week_06.databinding.FragmentChatDetailBinding
import com.sbl.homework.week_06.frameworks.ui.MainActivity
import com.sbl.homework.week_06.frameworks.ui.StartActivity

class ChatDetailFragment(private val uid: String) : Fragment() {
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


        //TODO: load earlier messages

        return binding.root
    }

    fun onClick(v: View){
        val myMsgContent = binding.messageInput.text.toString()
        when (v) {
            binding.backBtn ->
                (activity as MainActivity).supportFragmentManager.popBackStack()

            binding.sendButton ->
                helper.sendMessage(uid ,myMsgContent, completion = {
                    if (it) {
                        myMsgContent.
                    }
                } )

        }
    }
}