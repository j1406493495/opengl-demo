package cn.woong.opengl.objects

import android.opengl.GLES20
import cn.woong.opengl.utils.Geometry

/**
 * @author Woong on 2019/6/8
 * @website http://woong.cn
 */
class ObjectBuilder(var sizeInVertices: Int) {
    companion object {
        private val FLOATS_PER_VERTEX = 3

        /**
         * 圆柱体顶部顶点数量
         */
        fun sizeOfCircleInVertices(numPoints: Int): Int {
            return 1 + (numPoints + 1)
        }

        /**
         * 圆柱体侧面顶点数量
         */
        fun sizeOfOpenCylinderInVertices(numPoints: Int): Int {
            return (numPoints + 1) * 2
        }

        fun creataPuck(puck: Geometry.Cylinder, numPoints: Int): GeneratedData {
            val size = sizeOfCircleInVertices(numPoints) + sizeOfOpenCylinderInVertices(numPoints)
            val puckTop = Geometry.Circle(puck.center.translateY(puck.height / 2), puck.radius)

            val builder: ObjectBuilder = ObjectBuilder(size)
            builder.appendCircle(puckTop, numPoints)
            builder.appendOpenCylinder(puck, numPoints)

            return builder.build()
        }

        fun createMallet(center: Geometry.Point, radius: Float, height: Float, numPoints: Int): GeneratedData {
            val size = sizeOfCircleInVertices(numPoints) * 2 + sizeOfOpenCylinderInVertices(numPoints) * 2
            val baseHeight = height * 0.25f
            val baseCircle = Geometry.Circle(center.translateY(-baseHeight), radius)
            val baseCylinder = Geometry.Cylinder(baseCircle.center.translateY(-baseHeight / 2f),
                    radius, baseHeight)

            val handleHeight = height * 0.75f
            val handleRadius = radius / 3f
            val handleCircle = Geometry.Circle(center.translateY(height * 0.5f), handleRadius)
            val handleCylinder = Geometry.Cylinder(handleCircle.center.translateY(-handleHeight / 2f),
                    handleRadius, handleHeight)

            val builder = ObjectBuilder(size)
            builder.appendCircle(baseCircle, numPoints)
            builder.appendOpenCylinder(baseCylinder, numPoints)
            builder.appendCircle(handleCircle, numPoints)
            builder.appendOpenCylinder(handleCylinder, numPoints)

            return builder.build()
        }

    }

    private lateinit var vertexData: FloatArray
    private var offset = 0
    private val drawList = arrayListOf<DrawCommand>()

    init {
        vertexData = FloatArray(sizeInVertices * FLOATS_PER_VERTEX)
    }

    private fun appendCircle(circle: Geometry.Circle, numPoints: Int) {
        val startVertex = offset / FLOATS_PER_VERTEX
        val numVertices = sizeOfCircleInVertices(numPoints)

        vertexData[offset++] = circle.center.x
        vertexData[offset++] = circle.center.y
        vertexData[offset++] = circle.center.z

        for (i in 0..numPoints) {
            val angleInRadians = i.toFloat() / numPoints.toFloat() * (Math.PI * 2f)

            vertexData[offset++] = circle.center.x + circle.radius * Math.cos(angleInRadians).toFloat()
            vertexData[offset++] = circle.center.y
            vertexData[offset++] = circle.center.z + circle.radius * Math.sin(angleInRadians).toFloat()
        }

        drawList.add(object : DrawCommand {
            override fun draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, startVertex, numVertices)
            }
        })
    }

    private fun appendOpenCylinder(cylinder: Geometry.Cylinder, numPoints: Int) {
        val startVertex = offset / FLOATS_PER_VERTEX
        val numVertices = sizeOfOpenCylinderInVertices(numPoints)
        val yStart = cylinder.center.y - cylinder.height / 2f
        val yEnd = cylinder.center.y + cylinder.height / 2f

        for (i in 0..numPoints) {
            val angleInRadians = i.toFloat() / numPoints.toFloat() * Math.PI * 2f
            val xPosition = cylinder.center.x + cylinder.radius * Math.cos(angleInRadians).toFloat()
            val zPosition = cylinder.center.z + cylinder.radius * Math.sin(angleInRadians).toFloat()

            vertexData[offset++] = xPosition
            vertexData[offset++] = yStart
            vertexData[offset++] = zPosition
            vertexData[offset++] = xPosition
            vertexData[offset++] = yEnd
            vertexData[offset++] = zPosition
        }

        drawList.add(object : DrawCommand {
            override fun draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, startVertex, numVertices)
            }
        })
    }

    private fun build(): GeneratedData {
        return GeneratedData(vertexData, drawList)
    }

    interface DrawCommand {
        fun draw()
    }

    class GeneratedData(var vertexData: FloatArray, var drawList: ArrayList<DrawCommand>)
}