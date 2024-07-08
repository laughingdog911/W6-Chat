package com.sbl.homework.week_06.home.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sbl.homework.week_06.R
import com.sbl.homework.week_06.chat.ui.ChatDetailFragment
import com.sbl.homework.week_06.databinding.FragmentHomeBinding
import com.sbl.homework.week_06.databinding.FriendsListBinding
import com.sbl.homework.week_06.frameworks.ui.MainActivity
import com.sbl.homework.week_06.home.helper.HomeHelper
import com.sbl.homework.week_06.home.models.FriendsListAdapter
import com.sbl.homework.week_06.userManagement.helper.UserManagement


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: FriendsListAdapter

    private val userManager = UserManagement()
    private val helper = HomeHelper()
    private var newestUserProfileMessage: String = ""
    private var newestFriendsCount: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.view = this
        binding.lifecycleOwner = this

        newestUserProfileMessage = binding.userProfileMessage.text.toString()
        newestFriendsCount = binding.friendCount.text.toString()

        infoDisplay()                                       // 처음에 나와야 할 정보들
        inFriendRecyclerView()                              // 친구들 리사이클러 뷰에 넣어주기?

        return binding.root
    }

    private fun inFriendRecyclerView() {
        adapter = FriendsListAdapter()
        binding.recyclerView.adapter = adapter              // recyclerView의 adapter를 내가 만든 adapter로 지정
        binding.recyclerView.layoutManager = LinearLayoutManager(context as MainActivity)
        adapter.setOnFriendClickListener(object : FriendsListAdapter.OnFriendClickListner {
            override fun onFriendClick(view: FriendsListBinding, position: Int) {
                val transactionManager =
                    (activity as MainActivity).supportFragmentManager

                transactionManager.beginTransaction().replace(R.id.HomeView, ChatDetailFragment(HomeHelper.friendsList.value!![position])).addToBackStack("")
                    .commit()
            }
        })
        helper.getFriends {                      // layoutManager 지정
            if (it) {                                  // getFriends 함수 call
                Log.d("HomeF.", HomeHelper.friendsList.value!!.toString())
                adapter.notifyDataSetChanged()              // friendsList의 값이 업데이트 되면 알림
            } else {
                Toast.makeText(context as MainActivity, "오류: 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun infoDisplay() {
        binding.userProfileName.text = userManager.getUserInfo()!!.name
        helper.getProfileMessage { newestUserProfileMessage = it }
        newestFriendsCount = "Friends (${HomeHelper.friendsList.value!!.size})"
    }

    fun onClick(v: View) {

        when (v) {
            binding.refreshButton -> {
                binding.userProfileMessage.text = newestUserProfileMessage
                binding.friendCount.text = newestFriendsCount
                inFriendRecyclerView()
            }

            binding.editButton -> {
                val editProfileMSGField = EditText(context as MainActivity)
                AlertDialog.Builder(context as MainActivity).run {
                    setIcon(android.R.drawable.ic_menu_edit)
                    setTitle("프로필 메세지 편집")
                    setView(editProfileMSGField)
                    setNegativeButton("취소") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    setPositiveButton("확인") { dialogInterface, _ ->
                        newestUserProfileMessage = editProfileMSGField.text.toString()
                        helper.setProfileMessage(newestUserProfileMessage, completion = {})
                        dialogInterface.dismiss()
                    }
                    show()
                }
            }

            binding.addFriend -> {
                val emailField = EditText(context as MainActivity)
                AlertDialog.Builder(context as MainActivity).run {
                    setIcon(android.R.drawable.ic_input_add)
                    setTitle("친구 추가")
                    setMessage("친구의 이메일을 입력해주세요.")
                    setView(emailField)
                    setNegativeButton("취소") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    setPositiveButton("추가") { dialogInterface, _ ->
                        if (emailField.text.isEmpty()){
                            Toast.makeText(
                                context as MainActivity,
                                "친구의 이메일을 입력해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        helper.addFriend(emailField.text.toString(), completion = {
                            if (it) {
                                dialogInterface.dismiss()
                            }else{
                                Toast.makeText(
                                    context as MainActivity,
                                    "오류: 없는 이메일 주소입니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                        inFriendRecyclerView()
                    }
                    show()
                }

            }
        }

    }
}