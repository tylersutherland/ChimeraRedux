#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float alpha_mod;

void main() {
    gl_FragColor = texture2D(u_texture, v_texCoords);
    //gl_FragColor.a += alpha_mod / 30;
}