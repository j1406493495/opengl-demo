attribute vec4 a_Position;
//attribute vec4 a_Color;
//varying vec4 v_Color; varying 属性用来混合颜色
// 利用该矩阵将虚拟坐标空间转换为归一化坐标
uniform mat4 u_Matrix;

void main()
{
//    v_Color = a_Color;

    // 利用该矩阵将 a_Position 转换为归一化坐标
    gl_Position = u_Matrix * a_Position;
//    gl_PointSize = 10.0;
}