package cn.woong.opengl.programs

import android.content.Context
import android.opengl.GLES20
import cn.woong.opengl.utils.ShaderHelper
import cn.woong.opengl.utils.TextResourceReader

/**
 * @author Woong on 2019/6/8
 * @website http://woong.cn
 */
open class ShaderProgram(var context: Context, var vertexShaderResourceId: Int,
                         var fragmentShaderResourceId: Int) {
    /**
     *  Uniform constants
     */
    protected val U_COLOR = "u_Color"
    protected val U_MATRIX = "u_Matrix"
    protected val U_TEXTURE_UNIT = "u_TextureUnit"

    /**
     * Attribute constants
     */
    protected val A_POSITION = "a_Position"
    protected val A_COLOR = "a_Color"
    protected val A_TEXTURE_COORDINATES = "a_TextureCoordinates"

    /**
     * Shader program
     */
    protected var program: Int = 0

    init {
        program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId))
    }

    fun useProgram() {
        GLES20.glUseProgram(program)
    }
}