package cn.woong.opengl.data

import android.opengl.GLES20
import cn.woong.opengl.constants.Constants
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * @author Woong on 2019/6/3
 * @website http://woong.cn
 */
class VertexArray(var vertexData: FloatArray) {
    private var floatBuffer: FloatBuffer

    init {
        floatBuffer = ByteBuffer.allocateDirect(vertexData.size * Constants.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData)
    }

    fun setVertexAttribPointer(dataOffset: Int, attributeLocation: Int, componentCount: Int, stride: Int) {
        floatBuffer.position(dataOffset)
        GLES20.glVertexAttribPointer(attributeLocation, componentCount, GLES20.GL_FLOAT,
                false, stride, floatBuffer)
        GLES20.glEnableVertexAttribArray(attributeLocation)

        floatBuffer.position(0)
    }
}