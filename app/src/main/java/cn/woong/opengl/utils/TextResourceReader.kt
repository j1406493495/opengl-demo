package cn.woong.opengl.utils

import android.content.Context
import android.content.res.Resources

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object TextResourceReader {
    fun readTextFileFromResource(context: Context, resourceId: Int): String {
        val builder = StringBuilder()

        try {
            val inputStream = context.resources.openRawResource(resourceId)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            var nextLine: String? = bufferedReader.readLine()
            while (nextLine != null) {
                nextLine = bufferedReader.readLine()
                builder.append(nextLine)
                builder.append('\n')
            }
        } catch (ioException: IOException) {
            throw RuntimeException("Can not open resource: $resourceId", ioException)
        } catch (nfe: Resources.NotFoundException) {
            throw RuntimeException("Can not find resource: $resourceId", nfe)
        }

        return builder.toString()
    }
}
