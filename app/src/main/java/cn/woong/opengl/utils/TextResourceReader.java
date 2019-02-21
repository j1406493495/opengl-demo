package cn.woong.opengl.utils;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextResourceReader {
    public static String readTextFileFromResource(Context context, int resourceId) {
        StringBuilder builder = new StringBuilder();

        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                builder.append(nextLine);
                builder.append('\n');
            }
        } catch (IOException ioException) {
            throw new RuntimeException("Can not open resource: " + resourceId, ioException);
        } catch (Resources.NotFoundException nfe) {
            throw new RuntimeException("Can not find resource: " + resourceId, nfe);
        }

        return builder.toString();
    }
}
