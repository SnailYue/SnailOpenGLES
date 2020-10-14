uniform mat4 vMatrix;
uniform mat4 uMMatrix;
uniform vec3 uLightLocation;
uniform vec3 uCamera;
attribute vec3 vPosition;
attribute vec3 vNormal;
varying vec4 vDiffuse;

vec4 pointLight(vec3 normal, vec3 lightLocation, vec4 lightDiffuse) {
    vec3 newTarget = normalize((vMatrix * vec4(normal + vPosition, 1)).xyz - (vMatrix * vec4(vPosition, 1)).xyz);
    vec3 vp = normalize(lightLocation - (vMatrix * vec4(vPosition, 1)).xyz);
    return lightDiffuse * max(0.0, dot(newTarget, vp));
}

void main() {
    gl_Position = vMatrix * vec4(vPosition, 1);
    vec4 at = vec4(1.0, 1.0, 1.0, 1.0);
    vec3 pos = vec3(100.0, 80.0, 80.0);
    vDiffuse =  pointLight(normalize(vPosition), pos, at);
}
