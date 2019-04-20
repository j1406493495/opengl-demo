package cn.woong.opengl

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils

class AirHockeyActivity : Activity() {
    private var mGLSurfaceView: GLSurfaceView? = null
    private var mRender: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGLSurfaceView = GLSurfaceView(this)

        val activityManager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")))

        if (supportsEs2) {
            mGLSurfaceView!!.setEGLContextClientVersion(2)
            mGLSurfaceView!!.setRenderer(AirHockeyRenderer(this))
            mRender = true
        } else {
            LogUtils.e("This device does not support OpenGL ES 2.0")
            ToastUtils.showShort("This device does not support OpenGL ES 2.0")
        }

        setContentView(mGLSurfaceView)
    }

    override fun onPause() {
        super.onPause()

        if (mRender) {
            mGLSurfaceView!!.onPause()
        }
    }

    override fun onResume() {
        super.onResume()

        if (mRender) {
            mGLSurfaceView!!.onResume()
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }
}
