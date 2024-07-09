package com.sbl.homework.week_06.userManagement.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sbl.homework.week_06.R
import com.sbl.homework.week_06.databinding.FragmentRegisterBinding
import com.sbl.homework.week_06.frameworks.ui.MainActivity
import com.sbl.homework.week_06.userManagement.helper.UserManagement
import com.sbl.homework.week_06.userManagement.models.UserTypeModel
import java.util.Calendar
import com.sbl.homework.week_06.frameworks.ui.StartActivity as StartActivity

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    var userType: String = "Student"

    private val helper = UserManagement()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.view = this
        binding.lifecycleOwner = this

        return binding.root
    }

    fun onClick(v: View) {
        val phoneNumber = binding.phoneNo.text.toString()
        val email = binding.email.text.toString()
        val name = binding.name.text.toString()
        val getPW = binding.pw.text.toString()
        val getCheckPW = binding.checkPw.text.toString()
        val infoList: MutableList<String> =
            mutableListOf(email, name, phoneNumber, getPW, getCheckPW)

        when (v) {
            binding.backBtn -> {
                (activity as StartActivity).supportFragmentManager.popBackStack()
            }

            binding.stdBtn -> {
                userType = UserTypeModel.STUDENT.getString()
            }

            binding.profBtn -> {
                userType = UserTypeModel.PROFESSOR.getString()
            }

            binding.mplBtn -> {
                userType = UserTypeModel.EMPLOYEE.getString()
            }

            binding.confirmButton -> {
                val empty = "empty"
                for (i in infoList.indices) {
                    if (infoList[i].isEmpty()) {
                        infoList[i] = empty
                    }
                }
                if (infoList.any { it == empty }) {                            //1. 공백필드
                    makeAlertDialog(
                        "Empty Field", "Oups, You missed a field!", activity as StartActivity,)
                } else if (!email.contains("@")) {                        //2. 잘못된 이메일 형식
                    makeAlertDialog("Wrong Email Format", "You forgot to put @ ;)", activity as StartActivity)
                } else if (getPW.length <= 7) {                                //3. 안전하지 않은 비밀번호
                    makeAlertDialog("Unsafe Password", "Make it longer than 8 characters for safety!", activity as StartActivity)
                } else if (getCheckPW != getPW) {                              //4. 비밀번호 확인과 불일치
                    makeAlertDialog("Unmatched Password", "They aren't the same as the one you put up there!", activity as StartActivity)
                } else {                                                                        //5. 마지막 회원가입 확인
                    AlertDialog.Builder(activity as StartActivity).run {
                        setTitle("회원가입")
                        setMessage(
                            "다음 정보로 가입을 진행하시겠습니까?\n" +
                                    "이름: ${name}\n" +
                                    "이메일: ${email}\n" +
                                    "전화 번호: ${phoneNumber}\n" +
                                    "멤버 유형: $userType"
                        )
                        setIcon(android.R.drawable.ic_dialog_info)
                        setNegativeButton("아니오") { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        setPositiveButton("예") { dialogInterface, _ ->
                            dialogInterface.dismiss()
                            binding.confirmButton.visibility = View.INVISIBLE
                            binding.progressCircular.visibility =
                                View.VISIBLE                          //6. Progress Circular

                            val convertedUserType = helper.convertKoreanStringToUserType(userType)
                            helper.signUp(email,
                                getPW,
                                name,
                                phoneNumber,
                                convertedUserType,
                                completion = {                                       //8. signUp 함수 완료 여부
                                    if (it) {
                                        val intent = Intent(context as StartActivity, MainActivity::class.java) //activity 바꿔주기
                                        (context as StartActivity).startActivity(intent)                    //시작하는 activity(startActivity())로 StartActivity를 지정, intent 전달.
                                        (context as StartActivity).finish()                                 //HomeView로 전환 후 이 fragment의 생명주기 끝.
                                    } else {                                          //F: alert dialog
                                        AlertDialog.Builder(activity as StartActivity).run {
                                            setIcon(android.R.drawable.ic_dialog_alert)
                                            setMessage("회원가입에 실패했습니다. 잠시 후 다시 시도해주세요.")
                                            setPositiveButton("확인", null)
                                            show()
                                        }
                                    }
                                })
                        }

                        show()
                    }
                }
            }
        }
    }
}

