attribute vec4 a_Position;
//attribute vec4 a_Color;
//varying vec4 v_Color; varying 属性用来混合颜色
uniform mat4 u_Matrix;

void main()
{
//    v_Color = a_Color;

    gl_Position = u_Matrix * a_Position;
//    gl_PointSize = 10.0;
}