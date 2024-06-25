package com.example.mycalculator

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mycalculator.databinding.CalculatorMainViewDesignBinding
import com.example.mycalculator.ui.theme.MyCalculatorTheme

class MainActivity : Activity() {
    //number, operator button 눌렀을 때 입력값 저장
    var input = 0
    //number, operator button 입력값 조합으로 나온 수식의 결과값
    var result = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            MyCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    Button(onClick = { /*TODO*/ }) {
        Text(text = "A")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyCalculatorTheme {
        Greeting("Android")
    }
}