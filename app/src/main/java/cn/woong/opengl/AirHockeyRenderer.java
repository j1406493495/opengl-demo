package cn.woong.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.woong.opengl.utils.ShaderHelper;
import cn.woong.opengl.utils.TextResourceReader;

public class AirHockeyRenderer implements GLSurfaceView.Renderer  {
    private static final int POSTION_COMPONENT_COUNT = 2;
    private final FloatBuffer mFloatBuffer;
    private final Context mContext;

    public AirHockeyRenderer(Context context) {
        mContext = context;

        // 两个三角形合成一个长方形
        float[] tableVertices = {
                // 第一个三角形坐标
                0f, 0f, 9f, 14f, 0f, 14f,
                // 第二个三角形坐标
                0f, 0f, 9f, 0f, 9f, 14f,
                // middleLine
                0f, 7f, 9f, 7f,
                // mallets
                4.5f, 2f, 4.5f, 12f};

        // 复制 Java 堆到本地堆
        // 浮点数占4个字节
        mFloatBuffer = ByteBuffer.allocateDirect(tableVertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        mFloatBuffer.put(tableVertices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        String vertexShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }
}
