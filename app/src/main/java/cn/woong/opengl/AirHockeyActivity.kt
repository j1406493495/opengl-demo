package cn.woong.opengl

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle

class AirHockeyActivity : Activity() {
    private var mGLSurfaceView: GLSurfaceView? = null
    private var mRender: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        setContentView(R.layout.activity_main);

        mGLSurfaceView = GLSurfaceView(this)
        mGLSurfaceView!!.setEGLContextClientVersion(2)

        //        mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mGLSurfaceView!!.setRenderer(AirHockeyRenderer(this))
        mRender = true

        setContentView(mGLSurfaceView)

        // Example of a call to a native method
        //        TextView tv = (TextView) findViewById(R.id.sample_text);
        //        tv.setText(stringFromJNI());
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

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
