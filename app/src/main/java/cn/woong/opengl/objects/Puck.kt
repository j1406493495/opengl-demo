package cn.woong.opengl.objects

import cn.woong.opengl.data.VertexArray
import cn.woong.opengl.programs.ColorShaderProgram
import cn.woong.opengl.utils.Geometry

/**
 * @author Woong on 2019/6/8
 * @website http://woong.cn
 */
class Puck(var radius: Float, var height: Float, var numPointsAroundPuck: Int) {
    companion object {
        /**
         * 每次读取顶点的数量
         */
        private val POSITION_COMPONENT_COUNT = 3
    }

    private lateinit var vertexArray: VertexArray
    private lateinit var drawlist: ArrayList<ObjectBuilder.DrawCommand>

    init {
        val generatedData = ObjectBuilder.creataPuck(Geometry.Cylinder(Geometry.Point(0f, 0f, 0f),
                radius, height), numPointsAroundPuck)
        vertexArray = VertexArray(generatedData.vertexData)
        drawlist = generatedData.drawList
    }

    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0)
    }

    fun draw() {
        drawlist.forEach { drawCommand: ObjectBuilder.DrawCommand -> drawCommand.draw() }
    }
}