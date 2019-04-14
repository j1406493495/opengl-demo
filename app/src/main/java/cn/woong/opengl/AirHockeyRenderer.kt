package cn.woong.opengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import cn.woong.opengl.utils.ShaderHelper
import cn.woong.opengl.utils.TextResourceReader

class AirHockeyRenderer(private val mContext: Context) : GLSurfaceView.Renderer {
    private val mFloatBuffer: FloatBuffer

    init {

        // 两个三角形合成一个长方形
        val tableVertices = floatArrayOf(
                // 第一个三角形坐标
                0f, 0f, 9f, 14f, 0f, 14f,
                // 第二个三角形坐标
                0f, 0f, 9f, 0f, 9f, 14f,
                // middleLine
                0f, 7f, 9f, 7f,
                // mallets
                4.5f, 2f, 4.5f, 12f)

        // 复制 Java 堆到本地堆
        // 浮点数占4个字节
        mFloatBuffer = ByteBuffer.allocateDirect(tableVertices.size * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()

        mFloatBuffer.put(tableVertices)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.0f)

        val vertexShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader)
        val fragmentShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_fragment_shader)

        val vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource)
        val fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    }

    companion object {
        private val BYTES_PER_FLOAT = 4
        private val POSTION_COMPONENT_COUNT = 2
    }
}
