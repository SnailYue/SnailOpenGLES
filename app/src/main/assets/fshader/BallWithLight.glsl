precision mediump float;
varying vec4 vDiffuse;

void main() {
    vec4 finalColor = vec4(1.0);

    gl_FragColor = finalColor * vDiffuse + finalColor * vec4(0.15, 0.15, 0.15, 1.0);
}
