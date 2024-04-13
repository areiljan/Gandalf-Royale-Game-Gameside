#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 center;
uniform float radius;
uniform sampler2D u_texture; // Declare u_texture as a uniform texture sampler
uniform mat4 u_inverseProjection; // Inverse of the projection matrix to convert from screen space to world space

void main() {
    // Convert screen space coordinates to world coordinates
    vec4 worldPos = u_inverseProjection * vec4(gl_FragCoord.xy, 0.0, 1.0);
    vec2 worldCoord = worldPos.xy / worldPos.w; // Normalize by the homogeneous coordinate

    // Calculate distance from the center of the circle
    float dist = length(worldCoord - center);

    // If the distance is greater than the radius, sample color from texture
    if (dist > radius) {
        // Convert world coordinates to texture coordinates (assuming texture size is 1.0x1.0)
        vec2 texCoord = worldCoord;

        // Sample color from the texture using the calculated texture coordinates
        vec4 texColor = texture2D(u_texture, texCoord);

        // Output the sampled color
        gl_FragColor = texColor;
    } else {
        // Inside the circle, set color to red
        gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0); // Red color with full alpha
    }
}
