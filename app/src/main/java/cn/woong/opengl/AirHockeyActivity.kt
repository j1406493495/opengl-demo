package cn.woong.opengl

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils

class AirHockeyActivity : Activity() {
    private var glSurfaceView: GLSurfaceView? = null
    private var airHockeyRenderer = AirHockeyRenderer(this)
    private var mRender: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glSurfaceView = GLSurfaceView(this)

        // 检查设备是否支持 OpenGL2.0
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
            glSurfaceView!!.setEGLContextClientVersion(2)
            glSurfaceView!!.setRenderer(airHockeyRenderer)
            mRender = true
        } else {
            LogUtils.e("This device does not support OpenGL ES 2.0")
            ToastUtils.showShort("This device does not support OpenGL ES 2.0")
        }

        glSurfaceView!!.setOnTouchListener { v, event ->
            if (event != null) {
                val normalizedX = event.x / v.width.toFloat() * 2 - 1
                val normalizedY = -(event.y / v.height.toFloat() * 2 - 1)

                if (event.action == MotionEvent.ACTION_DOWN) {
                    glSurfaceView!!.queueEvent { airHockeyRenderer.handleTouchPress(normalizedX, normalizedY) }
                } else if (event.action == MotionEvent.ACTION_MOVE) {
                    glSurfaceView!!.queueEvent { airHockeyRenderer.handleTouchDrag(normalizedX, normalizedY) }
                }

                return@setOnTouchListener true
            } else {
                return@setOnTouchListener false
            }
        }

        setContentView(glSurfaceView)
    }

    override fun onPause() {
        super.onPause()

        if (mRender) {
            glSurfaceView!!.onPause()
        }
    }

    override fun onResume() {
        super.onResume()

        if (mRender) {
            glSurfaceView!!.onResume()
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
