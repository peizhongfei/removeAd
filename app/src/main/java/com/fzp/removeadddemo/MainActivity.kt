package com.fzp.removeadddemo

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(),OnClickListener {
    private lateinit var openService:Button
    private lateinit var tvStatus:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        openService=findViewById(R.id.open_service)
        tvStatus=findViewById(R.id.tv_status)

        openService.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.open_service -> {
                jumpAccessbilityService()
            }
            else -> {}
        }
    }

    /**
     * 启动 AccessibilityService服务
     */
    private fun jumpAccessbilityService() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }

    override fun onResume() {
        super.onResume()
        refreshStatus()
    }

    private fun refreshStatus() {
        if (AdGoService.isServiceAvaible) {
            tvStatus.text = "跳过广告服务状态：已开启"
        }else{
            tvStatus.text = "跳过广告服务状态：未开启"
        }
    }
}