package com.sbl.homework.week_06.home.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.sbl.homework.week_06.R
import com.sbl.homework.week_06.databinding.FragmentHomeBinding
import com.sbl.homework.week_06.frameworks.ui.MainActivity
import com.sbl.homework.week_06.home.helper.HomeHelper
import com.sbl.homework.week_06.home.models.FriendsDataModel
import com.sbl.homework.week_06.home.models.FriendsListAdapter
import com.sbl.homework.week_06.userManagement.helper.UserManagement


class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: FriendsListAdapter

    private val userManager = UserManagement()
    private val helper = HomeHelper()
    private var friendsList = MutableLiveData<MutableList<FriendsDataModel>>().apply {
        value = mutableListOf()
    }
    private var newestUserProfileMessage:String = binding.userProfileMessage.text.toString()
    private var newestFriendsCount:String = binding.friendCount.text.toString()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.view = this
        binding.lifecycleOwner = this

        InfoDisplay()                                       // 처음에 나와야 할 정보들
        inFriendRecyclerView()                              // 친구들 리사이클러 뷰에 넣어주기?

        return binding.root
    }

    fun inFriendRecyclerView(){
        val adapter = FriendsListAdapter(friendsList.value)
        helper.getFriends {  }
        adapter.friendsList = friendsList.value!!
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context as MainActivity)
    }


    fun InfoDisplay(){
        binding.userProfileName.text = userManager.getUserInfo()!!.name
        helper.getProfileMessage { newestUserProfileMessage = it}
        newestFriendsCount =  "Friends (${friendsList.value!!.size})"
        inFriendRecyclerView()
        //TODO : friend card views
    }

    fun onClick(v:View){

        when (v) {
            binding.refreshButton -> {
                binding.userProfileMessage.text = newestUserProfileMessage
                binding.friendCount.text = newestFriendsCount
                adapter.friendsList
                // TODO: refresh the screen with newly retrieved information
                // TODO 1. User Profile Message
                // TODO 2. friend Count
                // TODO 3. friend Card view added
            }

            binding.editButton -> {
                val editProfileMSGField = EditText(context as MainActivity)
                AlertDialog.Builder(context as MainActivity).run {
                    setIcon(android.R.drawable.ic_menu_edit)
                    setTitle("프로필 메세지 편집")
                    setView(editProfileMSGField)
                    setNegativeButton("취소"){ dialogInterface, _->
                        dialogInterface.dismiss()
                    }
                    setPositiveButton("확인"){ dialogInterface, _ ->
                        newestUserProfileMessage = editProfileMSGField.text.toString()
                        dialogInterface.dismiss()
                    }
                    show()
                }
        }
            binding.addFriend -> {
                val friendsEmail = userManager.getUserInfo()!!.email
                val emailField = EditText(context as MainActivity);
                AlertDialog.Builder(context as MainActivity).run {
                    setIcon(android.R.drawable.ic_input_add)
                    setTitle("친구 추가")
                    setMessage("친구의 이메일을 입력해주세요.")
                    setView(emailField)
                    setNegativeButton("취소"){ dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    setPositiveButton("추가"){ dialogInterface, _ ->
                        helper.addFriend(friendsEmail, completion = {})
                        inFriendRecyclerView()
                        dialogInterface.dismiss()
                    }
                    show()
                }

            }
    }

}
}