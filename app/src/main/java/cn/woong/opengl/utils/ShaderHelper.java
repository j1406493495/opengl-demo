package cn.woong.opengl.utils;

import android.opengl.GLES20;

import com.blankj.utilcode.util.LogUtils;

public class ShaderHelper {

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = GLES20.glCreateShader(type);

        if (shaderObjectId == 0) {
            LogUtils.e("Can not create new shader === ");
            return 0;
        }

        GLES20.glShaderSource(shaderObjectId, shaderCode);
        GLES20.glCompileShader(shaderObjectId);

        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        LogUtils.i("Results of compiling source: \n" + shaderCode + "\n" + GLES20.glGetShaderInfoLog(shaderObjectId));

        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderObjectId);
            LogUtils.e("Compilation of shader failed === ");
            return 0;
        }

        return shaderObjectId;
    }
}
