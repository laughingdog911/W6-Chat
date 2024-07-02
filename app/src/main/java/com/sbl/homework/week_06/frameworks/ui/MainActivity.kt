package com.sbl.homework.week_06.frameworks.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sbl.homework.week_06.home.ui.HomeFragment
import com.sbl.homework.week_06.R
import com.sbl.homework.week_06.databinding.LayoutMainBinding

class MainActivity: AppCompatActivity() {

    private lateinit var view: LayoutMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = DataBindingUtil.setContentView(this, R.layout.layout_main)
        view.view = this
        view.lifecycleOwner = this

        val transactionManager = supportFragmentManager.beginTransaction()
        transactionManager.replace(R.id.main, HomeFragment())
        transactionManager.commit()
    }
}