package cn.woong.opengl.programs

import android.content.Context
import android.opengl.GLES20
import cn.woong.opengl.R
import kotlin.math.acos

/**
 * @author Woong on 2019/6/8
 * @website http://woong.cn
 */
class ColorShaderProgram(var colorContext: Context) : ShaderProgram(colorContext,
        R.raw.simple_vertex_shader, R.raw.simple_fragment_shader) {
    /**
     * uniform locations
     */
    private var uMatrixLocation = 0
    private var uColorLocation = 0

    /**
     * Attribute locations
     */
    private var aPositionLocation = 0
//    private var aColorLocation = 0

    init {
        // 获取 uniform 在 shaderProgram 中的位置
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR)

        // 获取 attribute 在 shaderProgram 中的位置
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
//        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)
    }

    fun setUniforms(matrix: FloatArray, r: Float, g: Float, b: Float) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        GLES20.glUniform4f(uColorLocation, r, g, b, 1f)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

//    fun getColorAttributeLocation(): Int {
//        return aColorLocation
//    }
}