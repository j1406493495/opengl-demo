package cn.woong.opengl.utils

import android.opengl.GLES20

import com.blankj.utilcode.util.LogUtils

object ShaderHelper {

    fun compileVertexShader(shaderCode: String): Int {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode)
    }

    fun compileFragmentShader(shaderCode: String): Int {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode)
    }

    /**
     * 编译着色器
     */
    private fun compileShader(type: Int, shaderCode: String): Int {
        val shaderObjectId = GLES20.glCreateShader(type)

        if (shaderObjectId == 0) {
            LogUtils.e("Can not create new shader === ")
            return 0
        }

        GLES20.glShaderSource(shaderObjectId, shaderCode)
        GLES20.glCompileShader(shaderObjectId)

        val compileStatus = IntArray(1)
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

        LogUtils.i("Results of compiling source: \n" + shaderCode + "\n" + GLES20.glGetShaderInfoLog(shaderObjectId))

        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderObjectId)
            LogUtils.e("Compilation of shader failed === ")
            return 0
        }

        return shaderObjectId
    }

    /**
     * 链接着色器
     */
    fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
        val programObjectId: Int = GLES20.glCreateProgram()

        if (programObjectId == 0) {
            LogUtils.e("Could not create new program")
            return 0
        }

        GLES20.glAttachShader(programObjectId, vertexShaderId)
        GLES20.glAttachShader(programObjectId, fragmentShaderId)

        GLES20.glLinkProgram(programObjectId)

        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0)
        LogUtils.i("Results of linking program ${GLES20.glGetProgramInfoLog(programObjectId)}")

        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programObjectId)
            LogUtils.e("Linking of program failed")
            return 0
        }

        return programObjectId
    }

    /**
     * 检测 program 是否有效
     */
    fun validateProgram(programObjectId: Int): Boolean {
        GLES20.glValidateProgram(programObjectId)

        val validateState = IntArray(1)
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateState, 0)
        LogUtils.i("Results of validating program ${GLES20.glGetProgramInfoLog(programObjectId)}")

        return validateState[0] != 0
    }
}
