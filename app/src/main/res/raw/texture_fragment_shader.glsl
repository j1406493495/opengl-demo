precision mediump float;
uniform sampler2D u_TextureUnit;    // sampler2D 二维纹理数据的数组
varying vec2 v_TextureCoordinates;

void main() {
    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);
}