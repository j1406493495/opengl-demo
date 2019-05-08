package cn.woong.opengl.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import com.blankj.utilcode.util.LogUtils

object TextureHelper {
    /**
     * 加载 Android 资源并转化为纹理资源
     */
    fun loadTexture(context: Context, resourceId: Int): Int {
        val textureObjectIds = IntArray(1)
        GLES20.glGenTextures(1, textureObjectIds, 0)

        if (textureObjectIds[0] == 0) {
            LogUtils.e("Could not generate a new OpenGL texture object")
            return 0
        }

        val options: BitmapFactory.Options = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options)
        options.inScaled = false

        if (bitmap == null) {
            LogUtils.e("ResourceId == $resourceId could not be decoded")
            GLES20.glDeleteTextures(1, textureObjectIds, 0)
            return 0
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0])
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap.recycle()
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

        return textureObjectIds[0]
    }
}