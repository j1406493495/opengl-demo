package cn.woong.opengl.objects

import android.opengl.GLES20
import cn.woong.opengl.constants.Constants
import cn.woong.opengl.data.VertexArray
import cn.woong.opengl.programs.TextureShaderProgram

/**
 * @author Woong on 2019/6/3
 * @website http://woong.cn
 */
class Table {
    private var vertexArray: VertexArray

    companion object {
        /**
         * 每次读取顶点的数量
         */
        private val POSITION_COMPONENT_COUNT = 2
        private val TEXTURE_COORDINATES_COMPONENT_COUNT = 2
        private val STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT
    }

    init {
        val VERTEX_DATA: FloatArray = floatArrayOf(
                // Order of coordinates: X,Y,S,T
                // Triangle fan
                0f, 0f, 0.5f, 0.5f,
                -0.5f, -0.8f, 0f, 0.9f,
                0.5f, -0.8f, 1f, 0.9f,
                0.5f, 0.8f, 1f, 0.1f,
                -0.5f, 0.8f, 0f, 0.1f,
                -0.5f, -0.8f, 0f, 0.9f)

        vertexArray = VertexArray(VERTEX_DATA)
    }

    fun bindData(textureProgram: TextureShaderProgram) {
        vertexArray.setVertexAttribPointer(0, textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE)
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE)
    }

    fun draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)
    }
}