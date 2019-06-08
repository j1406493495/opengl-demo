package cn.woong.opengl.programs

import android.content.Context
import android.opengl.GLES20
import cn.woong.opengl.R

/**
 * @author Woong on 2019/6/8
 * @website http://woong.cn
 */
class TextureShaderProgram(var textureContext: Context) : ShaderProgram(textureContext,
        R.raw.texture_vertex_shader, R.raw.texture_fragment_shader) {
    /**
     * Uniform locations
     */
    private var uMatrixLocation = 0
    private var uTextureUnitLocation = 0

    /**
     * Attribute locations
     */
    private var aPositionLocation = 0
    private var aTextureCoordinatesLocation = 0

    init {
        // Retrieve uniform locations for the shader program
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT)

        // Retrieve attribute locations for the shader program
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(program, A_TEXTURE_COORDINATES)
    }

    /**
     * 传递矩阵和纹理给他们的 Uniform
     */
    fun setUniform(matrix: FloatArray, textureId: Int) {
        // 将 matrix 矩阵传入 shaderProgram
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        // activeTexture(活动的纹理单元) 设为 TEXTURE0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        // 绑定 texture 和 unit
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        // 从 0 开始获取 texture uniform
        GLES20.glUniform1i(uTextureUnitLocation, 0)
    }

    /**
     * 获取属性位置
     */
    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

    fun getTextureCoordinatesAttributeLocation(): Int {
        return aTextureCoordinatesLocation
    }
}