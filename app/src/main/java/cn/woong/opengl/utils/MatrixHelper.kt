package cn.woong.opengl.utils

object MatrixHelper {
    /**
     * 透视投影转换矩阵
     * @param m 原始工程矩阵
     * @param yFovInDegrees 视角大小
     * @param aspect 屏幕的宽高比
     * @param n 到近处平面的距离
     * @param f 到远处平面的距离
     *
     * [
     *   a/aspect   0         0               0
     *   0          a         0               0
     *   0          0         (f+n)/(f-n)     2fn/(f-n)
     *   0          0         -1              0
     * ]
     */
    fun perspectiveM(m: FloatArray, yFovInDegrees: Float, aspect: Float, n: Float, f: Float) {
        val angleInRadians: Float = ((yFovInDegrees * Math.PI) / 180).toFloat()
        val a: Float = (1.0 / Math.tan(angleInRadians / 2.0)).toFloat()

        m[0] = a / aspect
        m[1] = 0f
        m[2] = 0f
        m[3] = 0f

        m[4] = 0f
        m[5] = a
        m[6] = 0f
        m[7] = 0f

        m[8] = 0f
        m[9] = 0f
        m[10] = -((f + n) / (f - n))
        m[11] = -1f

        m[12] = 0f
        m[13] = 0f
        m[14] = -((2f * f * n) / (f - n))
        m[15] = 0f
    }
}