package com.curated.app.core.ui.components.liquid

import android.graphics.RuntimeShader
import androidx.annotation.RequiresApi

/*
 * AGSL port of the liquid-glass fragment shaders in Pixelux-Glass
 * (https://github.com/CruzCortes/Pixelux-Glass, src/Shader/Shaders.metal).
 *
 * Kept from the original: the rounded-rectangle signed distance field, the lens profile
 * sin(pow(t, 0.25) * PI / 2) that bends the backdrop hardest right at the rim, the idea
 * of merging neighbouring glasses by smoothly combining their distance fields, and the
 * lighter lower half of the body.
 *
 * Changed for an app UI instead of an AR camera feed:
 *  - The backdrop is the recorded Compose content behind the glass, not a camera texture,
 *    so the YCbCr conversion and display transform are gone.
 *  - The lens bends along the surface normal of the glass. The original scales UVs around
 *    the centre of the screen, which only reads as a lens over a full-screen camera image.
 *  - Blur comes from a platform RenderEffect chained in front of this shader instead of
 *    the 9x9 sampling loop.
 *  - Merging uses an order-independent exponential smooth minimum so every member of a
 *    group computes the same field, and each pixel is drawn by the nearest member only.
 *  - Rim highlights, a little chromatic dispersion and the drop shadow are new.
 *
 * Pixelux-Glass is MIT licensed:
 *
 * Copyright (c) 2025 CruzCortes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/** Most neighbours one glass can merge with; matches the array sizes in the shader. */
internal const val MAX_MERGE_NEIGHBOURS = 8

@RequiresApi(33)
internal object LiquidGlassShader {

    /**
     * One shared instance is enough: a RenderEffect copies the uniforms when it is
     * created, and all drawing happens on the main thread.
     */
    val instance: RuntimeShader by lazy { RuntimeShader(SOURCE) }

    private const val SOURCE = """
uniform shader content;      // backdrop recorded behind the glass
uniform float2 origin;       // top-left of the glass inside this layer, px
uniform float2 glassSize;    // px
uniform float4 radii;        // corner radii px: bottom-right, top-right, bottom-left, top-left
uniform float px;            // pixels per dp
uniform float lensBand;      // width of the bending band inside the rim, px
uniform float lensDepth;     // how far the rim pulls the backdrop inwards, px
uniform float dispersion;    // red/blue split as a fraction of the bend
uniform float4 tint;         // straight rgb, a = strength
uniform float rim;           // specular rim strength
uniform float glow;          // body and lower-edge light strength
uniform float4 shadowColor;  // straight rgb, a = strength
uniform float2 shadowGeom;   // x = blur px, y = offset y px
uniform float mergeK;        // smooth-union softness, px
uniform float count;         // neighbours in use
uniform float4 nRect[8];     // neighbour centre relative to this glass centre, half size
uniform float nRadius[8];    // neighbour corner radius

float sdBox(float2 p, float2 b, float4 r) {
    float2 rr = (p.x > 0.0) ? r.xy : r.zw;
    float rad = (p.y > 0.0) ? rr.x : rr.y;
    float2 q = abs(p) - b + rad;
    return min(max(q.x, q.y), 0.0) + length(max(q, float2(0.0))) - rad;
}

// x: distance to the (merged) glass surface, negative inside.
// y: >= 0 when this glass is the closest member, so it owns the pixel.
float2 field(float2 p) {
    float own = sdBox(p, glassSize * 0.5, radii);
    if (count < 0.5) {
        return float2(own, 1.0);
    }
    float acc = exp(clamp(-own / mergeK, -30.0, 30.0));
    float nearest = 1.0e6;
    for (int i = 0; i < 8; i++) {
        if (float(i) < count) {
            float o = sdBox(p - nRect[i].xy, nRect[i].zw, float4(nRadius[i]));
            acc += exp(clamp(-o / mergeK, -30.0, 30.0));
            nearest = min(nearest, o);
        }
    }
    return float2(-mergeK * log(acc), nearest - own);
}

half4 main(float2 xy) {
    float2 p = xy - (origin + glassSize * 0.5);
    float2 f = field(p);
    if (f.y < 0.0) {
        return half4(0.0);
    }
    float d = f.x;

    float sh = 0.0;
    if (shadowColor.a > 0.0 && d > -1.0) {
        float ds = field(p - float2(0.0, shadowGeom.y)).x;
        float s = 1.0 - smoothstep(0.0, max(shadowGeom.x, 1.0), ds);
        sh = shadowColor.a * s * s;
    }
    half4 shadow = half4(shadowColor.rgb * sh, sh);

    float cover = clamp(0.5 - d, 0.0, 1.0);
    if (cover <= 0.0) {
        return shadow;
    }

    float2 ex = float2(1.0, 0.0);
    float2 g = float2(
        field(p + ex.xy).x - field(p - ex.xy).x,
        field(p + ex.yx).x - field(p - ex.yx).x
    );
    float2 n = g / max(length(g), 1.0e-4);

    // Pixelux lens profile: strongest at the rim, gone by the end of the band.
    float t = clamp(-d / lensBand, 0.0, 1.0);
    float bend = 1.0 - sin(pow(t, 0.25) * 1.5707963);
    float2 shift = -n * bend * lensDepth;

    half4 c = content.eval(xy + shift);
    if (dispersion > 0.0 && bend > 0.002) {
        c.r = content.eval(xy + shift * (1.0 + dispersion)).r;
        c.b = content.eval(xy + shift * (1.0 - dispersion)).b;
    }

    float3 col = c.rgb * (1.0 - tint.a) + tint.rgb * tint.a;
    float a = c.a + tint.a * (1.0 - c.a);

    float2 m = p / glassSize;
    float lift = 0.05 + 0.10 * smoothstep(-0.1, 0.3, m.y);
    float edge = 1.0 - smoothstep(0.0, 7.0 * px, -d);
    float lower = edge * clamp(m.y * 2.0 + 0.2, 0.0, 1.0) * 0.30;

    // Light from the top-left: a bright rim facing it and a fainter one opposite.
    float facing = dot(n, float2(-0.7071, -0.7071));
    float spec = pow(clamp(facing, 0.0, 1.0), 2.0) + 0.55 * pow(clamp(-facing, 0.0, 1.0), 3.0);
    float line = 1.0 - smoothstep(0.0, 1.6 * px, -d);

    float light = glow * (lift + lower) + rim * line * (0.18 + 0.82 * spec);
    a = clamp(a + light, 0.0, 1.0);
    col = min(col + float3(light), float3(a));

    return half4(col, a) * cover + shadow * (1.0 - cover);
}
"""
}
