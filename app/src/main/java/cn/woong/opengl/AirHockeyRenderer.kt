package cn.woong.opengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import cn.woong.opengl.constants.Constants.BYTES_PER_FLOAT
import cn.woong.opengl.objects.Mallet
import cn.woong.opengl.objects.Puck
import cn.woong.opengl.objects.Table
import cn.woong.opengl.programs.ColorShaderProgram
import cn.woong.opengl.programs.TextureShaderProgram
import cn.woong.opengl.utils.*

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private var projectionMatrix: FloatArray = FloatArray(16)
    private var modelMatrix: FloatArray = FloatArray(16)
    private var viewMatrix = FloatArray(16)
    private var viewProjectionMatrix = FloatArray(16)
    private var modelViewProjectionMatrix = FloatArray(16)
    private var invertedViewProjectionMatrix = FloatArray(16)
    private lateinit var puck: Puck
    private lateinit var table: Table
    private lateinit var mallet: Mallet
    private lateinit var textureProgram: TextureShaderProgram
    private lateinit var colorProgram: ColorShaderProgram
    private var texture = 0
    private var malletPressed = false
    private lateinit var blueMalletPosition: Geometry.Point

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0)

        positionTableInScene()
        textureProgram.useProgram()
        textureProgram.setUniform(modelViewProjectionMatrix, texture)
        table.bindData(textureProgram)
        table.draw()

        positionObjectInScene(0f, mallet.height / 2f, -0.4f)
        colorProgram.useProgram()
        colorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f)
        mallet.bindData(colorProgram)
        mallet.draw()

//        positionObjectInScene(0f, mallet.height / 2f, 0.4f)
        positionObjectInScene(blueMalletPosition.x, blueMalletPosition.y, blueMalletPosition.z)
        colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f)
        mallet.draw()

        positionObjectInScene(0f, puck.height / 2f, 0f)
        colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f)
        puck.bindData(colorProgram)
        puck.draw()

        /**
         *
        // 告诉 opengl 使用这个 program
        textureProgram.useProgram()
        // 传入 uniform
        textureProgram.setUniform(projectionMatrix, texture)
        // 绑定顶点数组数据和着色器程序
        table.bindData(textureProgram)
        // 绘制桌子
        table.draw()

        colorProgram.useProgram()
        colorProgram.setUniforms(projectionMatrix)
        mallet.bindData(colorProgram)
        mallet.draw()
         */
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        MatrixHelper.perspectiveM(projectionMatrix, 45f,
                width.toFloat() / height.toFloat(), 1f, 10f)

        Matrix.setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f,
                0f, 0f, 0f, 1f, 0f)

        /**
         *
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2.5f)
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f)

        val temp = FloatArray(16)
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0)
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.size)
         */
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        puck = Puck(0.06f, 0.02f, 32)
        mallet = Mallet(0.08f, 0.15f, 32)
        table = Table()
        blueMalletPosition = Geometry.Point(0f, mallet.height / 2f, 0.4f)

        textureProgram = TextureShaderProgram(context)
        colorProgram = ColorShaderProgram(context)

        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)
    }

    private fun positionTableInScene() {
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f)
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }

    private fun positionObjectInScene(x: Float, y: Float, z: Float) {
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, x, y, z)
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }

    fun handleTouchPress(normalizedX: Float, normalizedY: Float) {
        val ray = convertNormalized2DPointToRay(normalizedX, normalizedY)
        val malletBoundingSphere = Geometry.Sphere(Geometry.Point(blueMalletPosition.x,
                blueMalletPosition.y, blueMalletPosition.z), mallet.height / 2f)
        malletPressed = Geometry.intersects(malletBoundingSphere, ray)
    }

    fun handleTouchDrag(normalizedX: Float, normalizedY: Float) {
        if (malletPressed) {
            val ray = convertNormalized2DPointToRay(normalizedX, normalizedY)
            val plane = Geometry.Plane(Geometry.Point(0f, 0f, 0f), Geometry.Vector(0f, 1f, 0f))
            val touchedPoint = Geometry.intersectionPoint(ray, plane)
            blueMalletPosition = Geometry.Point(touchedPoint.x, mallet.height / 2f, touchedPoint.z)
        }
    }

    private fun convertNormalized2DPointToRay(normalizedX: Float, normalizedY: Float): Geometry.Ray {
        val nearPointNdc = floatArrayOf(normalizedX, normalizedY, -1f, 1f)
        val farPointNdc = floatArrayOf(normalizedX, normalizedY, 1f, 1f)
        val nearPointWorld = FloatArray(4)
        val farPointWorld = FloatArray(4)

        Matrix.multiplyMV(nearPointWorld, 0, invertedViewProjectionMatrix,
                0, nearPointNdc, 0)
        Matrix.multiplyMV(farPointWorld, 0, invertedViewProjectionMatrix,
                0, farPointNdc, 0)

        divideByW(nearPointWorld)
        divideByW(farPointWorld)

        val nearPointRay = Geometry.Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2])
        val farPointRay = Geometry.Point(farPointWorld[0], farPointWorld[1], farPointWorld[2])

        return Geometry.Ray(nearPointRay, Geometry.vectorBetween(nearPointRay, farPointRay))
    }

    private fun divideByW(vector: FloatArray) {
        vector[0] /= vector[3]
        vector[1] /= vector[3]
        vector[2] /= vector[3]
    }

    /*********************************** 第7章纹理前代码 *******************************
     *
     *
    private var vertexData: FloatBuffer
    private var projectionMatrix: FloatArray = FloatArray(16)
    private var modelMatrix: FloatArray = FloatArray(16)
//    private var uColorLocation: Int = 0
    private var uMatrixLocation: Int = 0
    private var aColorLocation: Int = 0
    private var aPositionLocation: Int = 0
    private var program: Int = 0

    companion object {
        private const val U_COLOR: String = "u_Color"
        private const val A_COLOR: String = "a_Color"
        private const val A_POSITION: String = "a_Position"
        private const val U_MATRIX: String = "u_Matrix"
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
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
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
                0f, -0.4f, 0f, 0f, 1f,
                0f,  0.4f, 1f, 0f, 0f)
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
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)

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

        MatrixHelper.perspectiveM(projectionMatrix, 50f,
                width.toFloat() / height.toFloat(), 1f, 10f)

        Matrix.setIdentityM(modelMatrix, 0)
//        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2f)
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2.5f)
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f)

        val temp = FloatArray(16)
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0)
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.size)


//        val aspectRatio = if (width > height) {
//            width.toFloat() / height.toFloat()
//        } else {
//            height.toFloat() / width.toFloat()
//        }
//
//        if (width > height) {
//            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
//        } else {
//            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
//        }
    }

    override fun onDrawFrame(gl: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0)

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
     *
     *
    **/

}
