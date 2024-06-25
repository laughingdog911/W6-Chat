package com.hj.myreel

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.hj.myreel.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var likedNo = MutableLiveData("0")
    
    val imageDrawableArray = this.resources.obtainTypedArray(R.array.picList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        fun onClick (like: View){
            when(like){
                binding.likeBtn -> {
                    likedNo.value += 1

                }
            }
        }


        fun onLongClick (next: View){
            when(next){
                binding.picDp -> {
                    //for(i in arrayListOf(binding.picLis??t))
                    //if i != last.index
                    //array[] += 1
                    //else i == last.index
                    //setimageResource.index[0]
                }
            }
        }
        }
    }
