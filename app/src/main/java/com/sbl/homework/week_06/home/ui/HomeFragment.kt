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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.sbl.homework.week_06.R
import com.sbl.homework.week_06.databinding.FragmentHomeBinding
import com.sbl.homework.week_06.frameworks.ui.MainActivity
import com.sbl.homework.week_06.home.helper.HomeHelper
import com.sbl.homework.week_06.home.models.FriendsDataModel
import com.sbl.homework.week_06.home.models.FriendsListAdapter
import com.sbl.homework.week_06.userManagement.helper.UserManagement
import com.sbl.homework.week_06.userManagement.models.UserInfoModel


class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: FriendsListAdapter

    val userManager = UserManagement()
    val helper = HomeHelper()
    var friendsList = MutableLiveData<MutableList<FriendsDataModel>>().apply {
        value = mutableListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.view = this
        binding.lifecycleOwner = this

        InfoDisplay()
        inFriendRecyclerView()
        testlist()

        return binding.root
    }

    fun inFriendRecyclerView(){
        val adapter = FriendsListAdapter()
        adapter.friendsList = friendsList.value!!
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context as MainActivity)
    }
    fun testlist(){
        with(friendsList.value){
            this?.add(FriendsDataModel("sam0493", "Sam Hueler", "Traveling"))
        }
    }


    fun InfoDisplay(){
        binding.userProfileName.text = userManager.getUserInfo()!!.name
        helper.getProfileMessage { binding.userProfileMessage.text = it}
        binding.friendCount.text =  "Friends (${friendsList.value!!.size})"
    }

    fun onClick(v:View){


        when (v) {
            binding.refreshButton -> {
                // TODO: refresh the screen with newly retrieved information
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
                        binding.userProfileMessage.text = editProfileMSGField.text
                        dialogInterface.dismiss()
                    }
                    show()
                }
                // TODO: 2. dialog to change the profilemessage(before editing -> after editing)
                // TODO: confirm button on the dialog -> updates the edited message and displays
                //       on the profile message textview
        }
            binding.addFriend -> {
                val friendsEmail = userManager.getUserInfo()!!.email
                var FriendEmail = EditText(context as MainActivity);
                AlertDialog.Builder(context as MainActivity).run {
                    setIcon(android.R.drawable.ic_input_add)
                    setTitle("친구 추가")
                    setMessage("친구의 이메일을 입력해주세요.")
                    setView(FriendEmail)
                    setNegativeButton("취소"){ dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    setPositiveButton("추가"){ dialogInterface, _ ->
                        helper.addFriend(friendsEmail, completion = {})
                        //TODO recycler view 에 넣어주기
                        dialogInterface.dismiss()
                    }
                    show()
                }

            }
    }

}
}