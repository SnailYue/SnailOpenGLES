attribute vec4 vPosition;
attribute vec2 vCoordinate;
uniform mat4 vMatrix;

//用于传递参数给fragmentshader
varying vec2 aCoordinate;
varying vec4 aPos;
varying vec4 gPosition;

void main() {
    gl_Position = vMatrix * vPosition;
    aPos = vPosition;
    aCoordinate = vCoordinate;
    gPosition = vMatrix * vPosition;
}