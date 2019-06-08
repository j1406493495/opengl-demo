package cn.woong.opengl.objects

import android.opengl.GLES20
import cn.woong.opengl.constants.Constants
import cn.woong.opengl.data.VertexArray
import cn.woong.opengl.programs.ColorShaderProgram
import cn.woong.opengl.utils.Geometry

/**
 * @author Woong on 2019/6/3
 * @website http://woong.cn
 */
class Mallet(var radius: Float, var height: Float, var numPointsAroundMallet: Int) {
    companion object {
        private val POSITION_COMPONENT_COUNT = 2
    }

    private lateinit var vertexArray: VertexArray
    private lateinit var drawList: ArrayList<ObjectBuilder.DrawCommand>

    init {
        val generatedData = ObjectBuilder.createMallet(Geometry.Point(0f, 0f, 0f),
                radius, height, numPointsAroundMallet)
        vertexArray = VertexArray(generatedData.vertexData)
        drawList = generatedData.drawList
    }

    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0)
    }

    fun draw() {
        drawList.forEach { drawCommand: ObjectBuilder.DrawCommand -> drawCommand.draw() }
    }

    /****
     * Mallet 是一个点的情况
     *
    private var vertexArray: VertexArray

    companion object {
        private val POSITION_COMPONENT_COUNT = 2
        private val COLOR_COMPONENT_COUNT = 3
        private val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT
    }

    init {
        val VERTEX_DATA = floatArrayOf(
                // order of coordinates: X Y R G B
                0f, -0.4f, 0f, 0f, 1f,
                0f, 0.4f, 1f, 0f, 0f
        )

        vertexArray = VertexArray(VERTEX_DATA)
    }

    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE)
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, colorProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT, STRIDE)
    }

    fun draw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 2)
    }
    ****/
}