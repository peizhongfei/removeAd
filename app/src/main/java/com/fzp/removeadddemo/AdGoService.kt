package com.fzp.removeadddemo

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 *@author zhongfeiPei
 *@date on 2024/11/12 9:26
 *@email zhongfei.p@verifone.cn
 *@describe 无障碍辅助服务
 *
 */
 class AdGoService: AccessibilityService() {
    private val TAG: String = "AdGoService"
    //创建线程池
    private var excutor:ExecutorService = Executors.newFixedThreadPool(5)

    companion object {
        var instance:AdGoService?=null //单例
        val isServiceAvaible:Boolean get() = instance!=null //判断无障碍服务是否可用
    }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
//        event?.let {
//            //写跳过广告的逻辑
//            Log.d(TAG, "onAccessibilityEvent: $it")
//            getCurrentRootNode()?.findAccessibilityNodeInfosByText("跳过").takeUnless { it.isNullOrEmpty() }?.get(0)
//                ?.let { Log.d(TAG, "onAccessibilityEvent: 检测到跳过广告节点：$it")
//                    performGlobalAction(AccessibilityNodeInfo.ACTION_CLICK)
//                }
//        }

        //过滤
        event?.let {
            excutor.execute {
                searchNode("跳过")?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        }
    }

    /**
     * 递归遍历查找匹配文本或id结点
     * 结点id的构造规则：包名:id/具体id
     * */
    private fun searchNode(filter: String): AccessibilityNodeInfo? {
        val rootNode = getCurrentRootNode()
        if (rootNode != null) {
            rootNode.findAccessibilityNodeInfosByText(filter).takeUnless { it.isNullOrEmpty() }?.let { return it[0] }
            if (!rootNode.packageName.isNullOrBlank()) {
                rootNode.findAccessibilityNodeInfosByViewId("${rootNode.packageName}:id/$filter")
                    .takeUnless { it.isNullOrEmpty() }?.let { return it[0] }
            }
        }
        return null
    }

    override fun onInterrupt() {
        // 服务中断时执行清理或记录日志操作
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "onServiceConnected: 服务已连接")
        instance = this

//        excutor.execute {
//            //可执行一些耗时操作
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    /**
     * 获取当前视图根节点
     */
    private fun getCurrentRootNode() =try{
        rootInActiveWindow
    }catch (e:Exception){
        e.message?.let { Log.e(TAG, "getCurrentRootNode: $it") }
        null
    }
}