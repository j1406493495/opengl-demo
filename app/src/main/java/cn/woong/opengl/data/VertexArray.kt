package cn.woong.opengl.data

import android.opengl.GLES20
import cn.woong.opengl.constants.Constants
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * @author Woong on 2019/6/3
 * @website http://woong.cn
 * 顶点数据操作类，负责传输数据到本地内存
 */
class VertexArray(var vertexData: FloatArray) {
    private var floatBuffer: FloatBuffer

    init {
        // 把顶点数据从 Java 内存复制到本地内存, 使其不被 Java 内存回收控制
        floatBuffer = ByteBuffer.allocateDirect(vertexData.size * Constants.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData)
    }

    /**
     * 关联 glsl 中的 attribute 变量(例如 a_Position) 和 floatBuffer 中的实际数据位置
     */
    fun setVertexAttribPointer(dataOffset: Int, attributeLocation: Int, componentCount: Int, stride: Int) {
        // 指针偏移，读取数据
        floatBuffer.position(dataOffset)

        // opengl 从缓冲区中查看 attributeLocation 对应的数据
        // attributeLocation: 属性位置
        // componentCount: 每个属性数据的计数
        // GL_FLOAT: 数据类型
        // normalized: 整型数据时有用
        // stride: 一个数组存储多个属性时，间隔长度
        // floatBuffer: 数据源
        GLES20.glVertexAttribPointer(attributeLocation, componentCount, GLES20.GL_FLOAT,
                false, stride, floatBuffer)

        // 使能顶点数据
        GLES20.glEnableVertexAttribArray(attributeLocation)

        // 指针归零，防止后续指针偏移出错
        floatBuffer.position(0)
    }
}