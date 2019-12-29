attribute vec4 a_Position; // attribute 修饰符只能在顶点着色器使用，一般用来表示点数据
//attribute vec4 a_Color;
//varying vec4 v_Color; varying 修饰符表示值可以传递到片段着色器
// 利用该矩阵将虚拟坐标空间转换为归一化坐标
uniform mat4 u_Matrix;  // uniform 修饰符类似 const，表示不会被改变的数据

void main()
{
//    v_Color = a_Color;

    // 利用该矩阵将 a_Position 转换为归一化坐标
    gl_Position = u_Matrix * a_Position;
//    gl_PointSize = 10.0;
}