package cn.woong.opengl.utils

/**
 * @author Woong on 2019/6/8
 * @website http://woong.cn
 */
class Geometry {
    class Point(var x: Float, var y: Float, var z: Float) {
        fun translateY(distance: Float): Point {
            return Point(x, y + distance, z)
        }
    }

    class Circle(var center: Point, var radius: Float) {
        fun scale(scale: Float): Circle {
            return Circle(center, radius * scale)
        }
    }

    class Cylinder(var center: Point, var radius: Float, var height: Float)
}