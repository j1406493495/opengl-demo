package cn.woong.opengl

import android.app.Application

import com.blankj.utilcode.util.Utils

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }
}
