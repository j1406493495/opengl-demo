package cn.woong.opengl.utils

import android.content.Context
import android.content.res.Resources

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object TextResourceReader {
    /**
     * 从 raw 文件中加载着色器资源
     */
    fun readTextFileFromResource(context: Context, resourceId: Int): String {
        val builder = StringBuilder()

        try {
            val inputStream = context.resources.openRawResource(resourceId)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            // 逐行读取文件内容
            var nextLine: String? = bufferedReader.readLine()
            while (nextLine != null) {
                builder.append(nextLine)
                builder.append("\n")
                nextLine = bufferedReader.readLine()
            }
        } catch (ioException: IOException) {
            throw RuntimeException("Can not open resource: $resourceId", ioException)
        } catch (nfe: Resources.NotFoundException) {
            throw RuntimeException("Can not find resource: $resourceId", nfe)
        }

        return builder.toString()
    }
}
