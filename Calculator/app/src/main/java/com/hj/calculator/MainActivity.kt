package com.hj.calculator


import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hj.calculator.databinding.CalculatorMainViewDesignBinding

class MainActivity: AppCompatActivity() {
    private val operatorList = listOf('%', '/', '*', '-', '+')
    var result = MutableLiveData("0")
    var eqxn = MutableLiveData("")
}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding : CalculatorMainViewDesignBinding =
            DataBindingUtil.setContentView(this, R.layout.calculator_main_view_design)
        binding.screenDesign = this

        fun onClick(v: View){
            when(v){
                binding.btnAc -> {
                    eqxn.value = ""
                    result.value = "0"
                }

                }
                else -> {
                    if(eqxn.value!!.isNotEmpty()){
                        for(op in operatorList){
                            if(expression.value!![expression.value!!.length-1] == op){
                                result.value = "0"

                                //i want to show 7 in both TextView...T.T

                            }
                        }
                    }

            }
        }


    }
}