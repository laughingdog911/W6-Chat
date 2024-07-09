package com.sbl.homework.week_06.userManagement.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.appcompat.widget.DialogTitle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.auth.User
import com.sbl.homework.week_06.R
import com.sbl.homework.week_06.databinding.FragmentLoginBinding
import com.sbl.homework.week_06.frameworks.ui.MainActivity
import com.sbl.homework.week_06.userManagement.helper.UserManagement
import com.sbl.homework.week_06.frameworks.ui.StartActivity as StartActivity


fun makeAlertDialog(title: String, message: String, activity: Activity) {
    AlertDialog.Builder(activity).run {
        setIcon(android.R.drawable.ic_dialog_alert)
        setTitle(title)
        setMessage(message)
        setPositiveButton("OK", null)
        show()
    }
}
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    val helper = UserManagement()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.view = this
        binding.lifecycleOwner = this

        return binding.root
    }



    fun onClick(v: View) {
        val email = binding.email.text.toString()
        val getPW = binding.pw.text.toString()
        val infoList: MutableList<String> = mutableListOf(email, getPW)

        when (v) {
            binding.resetPWButton -> {
                val transactionManager =                                                        // 비밀번호 재설정 버튼: 비밀번호 재설정 화면으로 전환
                    (activity as StartActivity).supportFragmentManager.beginTransaction()
                transactionManager.replace(R.id.entryPoint, ResetPwFragment()).addToBackStack("").commit()
            }

            binding.registerButton -> {                                                         // 화원가입 버튼 : 회원가입 화면으로 전환
                val transactionManager =
                    (activity as StartActivity).supportFragmentManager.beginTransaction()
                transactionManager.replace(R.id.entryPoint, RegisterFragment()).addToBackStack("").commit()
            }

            binding.loginButton -> {                                                            // 로그인 버튼 :
                val empty = "empty"                                                             //이메일, 비밀번호 란이 비었는지 확인:
                for (i in infoList.indices) {
                    if (infoList[i].isEmpty()) {
                        infoList[i] = empty
                    }
                }
                if (infoList.any { it == empty }) {                                             // 1. 공백 필드
                    makeAlertDialog(
                        "Empty Field", "You missed a field! ;)", activity as StartActivity)
                }else {                                                                         //2. 다 채워 넣고 로그인 버튼을 눌렀을 때:
                    binding.progressCircular.visibility = VISIBLE                               //progress 띄우기.
                    helper.signIn(email, getPW, completion = {                                  //UserManagement 에서 가져온 signIn 함수:
                        if (it) {                                                               //signIn 함수 결과 값(T):
                            val intent = Intent(context as StartActivity, MainActivity::class.java) //activity 바꿔주기
                            (context as StartActivity).startActivity(intent)                    //시작하는 activity(startActivity())로 StartActivity를 지정, intent 전달.
                            (context as StartActivity).finish()                                 //HomeView로 전환 후 이 fragment의 생명주기 끝.
                        }else{
                            makeAlertDialog("Error", "Oups, let's try that again! :)", activity as StartActivity)
                            binding.progressCircular.visibility = INVISIBLE
                        }
                    })

                }
            }
        }

    }
}