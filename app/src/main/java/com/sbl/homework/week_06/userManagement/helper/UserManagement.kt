package com.sbl.homework.week_06.userManagement.helper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sbl.homework.week_06.userManagement.models.UserInfoModel
import com.sbl.homework.week_06.userManagement.models.UserTypeModel

class UserManagement {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    companion object {
        private var userInfo: UserInfoModel? = null
    }

    fun signIn(email: String, password: String, completion: (Boolean) -> Unit){                 // signIn 함수: email, password를 받아서 가입된 정보와 비교
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { signInResult ->// email, password를 받았는가? 결과:
            if(signInResult.isSuccessful){                                                      // 받았다면:
                getUserInfo {                                                                   // 유저 정보 가져오기
                    completion(it)                                                              // 유저 정보 가져오는 작업 완료되면
                    return@getUserInfo                                                          // getUserInfo: T/F 결과 = singIn: T/F 결과
                }
            } else{                                                                             // 못 받았다면:
                completion(false)                                                               // getUserInfo: T/F 결과 = singIn: T/F 결과
                return@addOnCompleteListener
            }
        }.addOnFailureListener {                                                                // 실패 이벤트 리스너
            it.printStackTrace()                                                                //
            completion(false)                                                                   // 유저 정보 가져오는 작업 실패:
            return@addOnFailureListener                                                         // 실패한 결과 값을 실패 이벤트 리스너한테 반환
        }
    }

    fun signUp(email: String, password: String, name: String,
               phoneNumber: String, userType: UserTypeModel, completion: (Boolean) -> Unit){   // 정보 받기
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { signUpResult -> //createUserWithEmailAndPassword 함수:
            if(signUpResult.isSuccessful){                                                           //email과 password를 signUpResult로부터 받기
                db.collection("Users").document(auth.currentUser?.uid ?: "").set( //signUpResult 값을 받는데 성공:
                    hashMapOf(                                                                            //collection 함수: Users에 다음과 같은 set 정보 기록
                        "name" to name,
                        "email" to email,
                        "phoneNumber" to phoneNumber,
                        "userType" to userType.getString()
                    )
                ).addOnCompleteListener {                                                           //collection 함수에 completeListener 달아주기
                    if(it.isSuccessful){                                                            //collection 함수 성공: T
                        getUserInfo {                                                               //getUserInfo 반환
                            completion(it)
                            return@getUserInfo
                        }
                    } else{                                                                         //collection 함수 성공: F
                        auth.currentUser?.delete()                                                  //만든 유저 정보 지우기
                        completion(false)                                                           //
                        return@addOnCompleteListener
                    }
                }.addOnFailureListener {
                    it.printStackTrace()
                    completion(false)
                    return@addOnFailureListener
                }
            } else{
                completion(false)
                return@addOnCompleteListener
            }
        }.addOnFailureListener {
            it.printStackTrace()
            completion(false)
            return@addOnFailureListener
        }
    }

    fun sendPasswordResetMail(email: String, completion: (Boolean) -> Unit){
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                completion(it.isSuccessful)
            }.addOnFailureListener {
                it.printStackTrace()
                completion(false)
                return@addOnFailureListener
            }
    }

    private fun convertStringToUserType(userTypeAsString: String): UserTypeModel{
        return when(userTypeAsString){
            "Student" -> UserTypeModel.STUDENT
            "Professor" -> UserTypeModel.PROFESSOR
            "Employee" -> UserTypeModel.EMPLOYEE
            else -> UserTypeModel.STUDENT
        }
    }

    fun convertKoreanStringToUserType(userTypeAsKorean: String): UserTypeModel{
        return when(userTypeAsKorean){
            "학생" -> UserTypeModel.STUDENT
            "교수" -> UserTypeModel.PROFESSOR
            "교직원" -> UserTypeModel.EMPLOYEE
            else -> UserTypeModel.STUDENT
        }
    }

    private fun getUserInfo(completion: (Boolean) -> Unit){
        db.collection("Users").document(auth.currentUser?.uid ?: "").get().addOnCompleteListener {
            if(it.isSuccessful){
                val document = it.result

                if(document != null && document.exists()){
                    setUserInfo(
                        UserInfoModel(
                            uid = auth.currentUser?.uid ?: "",
                            email = auth.currentUser?.email ?: "",
                            name = document.get("name") as? String ?: "",
                            phoneNumber = document.get("phoneNumber") as? String ?: "",
                            userType = convertStringToUserType(document.get("userType") as? String ?: "Student")
                        )
                    )

                    completion(true)
                    return@addOnCompleteListener
                } else{
                    completion(false)
                    return@addOnCompleteListener
                }
            } else{
                completion(false)
                return@addOnCompleteListener
            }
        }.addOnFailureListener {
            it.printStackTrace()
            completion(false)
            return@addOnFailureListener
        }
    }

    private fun setUserInfo(userInfo: UserInfoModel?){
        UserManagement.userInfo = userInfo
    }

    fun getUserInfo(): UserInfoModel? {
        return UserManagement.userInfo
    }
}
