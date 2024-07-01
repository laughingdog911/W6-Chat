package com.sbl.homework.week_06.frameworks.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sbl.homework.week_06.R
import com.sbl.homework.week_06.databinding.LayoutStartBinding
import com.sbl.homework.week_06.userManagement.ui.LoginFragment

class StartActivity: AppCompatActivity() {
    private lateinit var view: LayoutStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = DataBindingUtil.setContentView(this, R.layout.layout_start)
        view.view = this
        view.lifecycleOwner = this

        val transactionManager = supportFragmentManager.beginTransaction()
        transactionManager.replace(R.id.start, LoginFragment())
        transactionManager.commit()
    }
}