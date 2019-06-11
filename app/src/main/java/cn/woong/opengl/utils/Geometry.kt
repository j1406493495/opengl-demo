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

        fun translate(vector: Vector): Point {
            return Point(x + vector.x, y + vector.y, z + vector.z)
        }
    }

    class Circle(var center: Point, var radius: Float) {
        fun scale(scale: Float): Circle {
            return Circle(center, radius * scale)
        }
    }

    class Cylinder(var center: Point, var radius: Float, var height: Float)

    class Ray(var point: Point, var vector: Vector)

    class Vector(var x: Float, var y: Float, var z: Float) {
        fun length(): Float {
            return Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        }

        fun crossProduct(other: Vector): Vector {
            return Vector(y * other.z - z * other.y, z * other.x - x * other.z,
                    x * other.y - y * other.x)
        }

        fun dotProduct(other: Vector): Float {
            return x * other.x + y * other.y + z * other.z
        }

        fun scale(f: Float): Vector {
            return Vector(x * f, y * f, z * f)
        }
    }

    class Sphere(var center: Point, var radius: Float)

    class Plane(var point: Point, var normal: Vector)

    companion object {
        fun vectorBetween(from: Point, to: Point): Vector {
            return Vector(to.x - from.x, to.y - from.y, to.z - from.z)
        }

        fun intersects(sphere: Sphere, ray: Ray): Boolean {
            return distanceBetween(sphere.center, ray) < sphere.radius
        }

        fun distanceBetween(point: Point, ray: Ray): Float {
            val p1ToPoint = vectorBetween(ray.point, point)
            val p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point)
            val areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length()
            val lengthOfBase = ray.vector.length()

            return areaOfTriangleTimesTwo / lengthOfBase
        }

        fun intersectionPoint(ray: Ray, plane: Plane): Point {
            val rayToPlaneVector = vectorBetween(ray.point, plane.point)
            val scaleFactor = rayToPlaneVector.dotProduct(plane.normal) / ray.vector.dotProduct(plane.normal)

            return ray.point.translate(ray.vector.scale(scaleFactor))
        }
    }
}