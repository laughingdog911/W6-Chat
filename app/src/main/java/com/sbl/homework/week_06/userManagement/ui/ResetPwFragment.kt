package com.sbl.homework.week_06.userManagement.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sbl.homework.week_06.R
import com.sbl.homework.week_06.databinding.FragmentResetPwBinding
import com.sbl.homework.week_06.frameworks.ui.StartActivity
import com.sbl.homework.week_06.userManagement.helper.UserManagement


class ResetPwFragment : Fragment() {
    private lateinit var binding: FragmentResetPwBinding

    val helper = UserManagement()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_reset_pw, container, false)
        binding.view = this
        binding.lifecycleOwner = this

        return binding.root
    }

    fun onClick(v: View) {
        val email = binding.email.text.toString()

        when (v) {
            binding.backBtn -> {                                                                // 로그인 화면으로 전환
                (activity as StartActivity).supportFragmentManager.popBackStack()
            }

            binding.sendButton -> {
                if (email.isEmpty()) {                                                          //1. 공백 필드
                    AlertDialog.Builder(activity as StartActivity).run {
                        setIcon(android.R.drawable.ic_dialog_alert)
                        setTitle("공백 필드")
                        setMessage("이메일을 입력해주세요.")
                        setPositiveButton("확인", null)
                        show()
                    }
                } else {
                    helper.sendPasswordResetMail(email, completion = {
                        if (it){
                            AlertDialog.Builder(activity as StartActivity).run {      //2. 전송 완료
                                setIcon(android.R.drawable.ic_dialog_email)
                                setTitle("전송 완료")
                                setMessage("입력하신 이메일로 비밀번호 재설정 링크를 전송했습니다. \n 링크를 확인하여 비밀번호를 다시 설정해 주세요.")
                                setNeutralButton("확인", null)
                                show()
                            }
                        }else{
                            AlertDialog.Builder(activity as StartActivity).run {
                                setIcon(android.R.drawable.ic_dialog_alert)
                                setTitle("전송 실패")
                                setMessage("링크 전송에 실패했습니다. \n 입력하신 이메일 주소, 네트워크 상태를 다시 확인해주세요.")
                                setNeutralButton("확인", null)
                                show()
                            }
                        }
                    })
                }
            }
        }
    }
}