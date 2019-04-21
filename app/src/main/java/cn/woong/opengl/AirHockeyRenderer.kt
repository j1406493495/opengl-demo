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

class AirHockeyRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private var vertexData: FloatBuffer
//    private var uColorLocation: Int = 0
    private var aColorLocation: Int = 0
    private var aPositionLocation: Int = 0
    private var program: Int = 0

    companion object {
        private const val U_COLOR: String = "u_Color"
        private const val A_COLOR: String = "a_Color"
        private const val A_POSITION: String = "a_Position"
        private const val BYTES_PER_FLOAT = 4
        private const val POSITION_COMPONENT_COUNT = 2
        private const val COLOR_COMPONENT_COUNT = 3
        private const val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT
    }

    init {

        // 两个三角形合成一个长方形
        val tableVertices = floatArrayOf(
                // 边框
//                -0.55f, -0.55f,
//                0.55f,  0.55f,
//                -0.55f,  0.55f,

//                -0.55f, -0.55f,
//                0.55f, -0.55f,
//                0.55f,  0.55f,

                // 桌板
                0f, 0f, 1f, 1f, 1f,
                -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
//                -0.5f, -0.5f,
//                0.5f,  0.5f,
//                -0.5f,  0.5f,
//
//                -0.5f, -0.5f,
//                0.5f, -0.5f,
//                0.5f,  0.5f,

                // Line 1
                -0.5f, 0f, 1f, 0f, 0f,
                0.5f, 0f, 1f, 0f, 0f,

                // Mallets
                0f, -0.25f, 0f, 0f, 1f,
                0f,  0.25f, 1f, 0f, 0f)
//                // 第一个三角形坐标
//                0f, 0f, 9f, 14f, 0f, 14f,
//                // 第二个三角形坐标
//                0f, 0f, 9f, 0f, 9f, 14f,
//                // middleLine
//                0f, 7f, 9f, 7f,
//                // mallets
//                4.5f, 2f, 4.5f, 12f)


        // 复制 Java 堆到本地堆
        // 浮点数占4个字节
        vertexData = ByteBuffer.allocateDirect(tableVertices.size * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()

        vertexData.put(tableVertices)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        /**
         * 加载着色器
         */
        val vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader)
        val fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader)

        /**
         * 编译着色器
         */
        val vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource)
        val fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource)

        /**
         * 链接着色器
         */
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader)

        /**
         * program 是否有效
         */
        ShaderHelper.validateProgram(program)

        /**
         * 使用 program
         */
        GLES20.glUseProgram(program)

        /**
         * 获取属性和 uniform 位置
         */
//        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR)
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)

        /**
         * 关联属性与顶点数据
         */
        vertexData.position(0)
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, STRIDE, vertexData)
        GLES20.glEnableVertexAttribArray(aPositionLocation)

        vertexData.position(POSITION_COMPONENT_COUNT)
        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, STRIDE, vertexData)
        GLES20.glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        /**
         * 画边框
         */
//        GLES20.glUniform4f(uColorLocation, 0.5f, 0.5f, 0.5f, 1.0f)
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6)

        /**
         * 画桌子
         */
//        GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)

        /**
         * 画分割线
         */
//        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2)

        /**
         * 画木棰
         */
//        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1)
//        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1)
    }

}
