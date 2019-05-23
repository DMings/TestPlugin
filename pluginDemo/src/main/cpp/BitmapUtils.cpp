//
// Copyright (c) 2019 Meitu Inc. All rights reserved.
//

#ifndef XXXXXXXXXXXXXXXX
#define XXXXXXXXXXXXXXXX

#include <jni.h>
#include <string>
#include <android/bitmap.h>
#include <stdlib.h>
#include <algorithm>

static const std::string CLASS_NAME = "com/dming/testndk/NDKActivity";

using namespace std;

static int bitmapUtils_native_blur(JNIEnv *env, jclass thiz, jobject inBitmap, int jRadius) {
    if (inBitmap == NULL) {
        return JNI_ERR;
    }
    unsigned char* data = nullptr;
    int ret = AndroidBitmap_lockPixels(env, inBitmap, (void **) &data);
    if (ret != 0) {
        return JNI_ERR;
    }
    auto *info = new AndroidBitmapInfo();
    int getInfoRet = AndroidBitmap_getInfo(env, inBitmap, info);
    if (getInfoRet != ANDROID_BITMAP_RESULT_SUCCESS) {
        return JNI_ERR;
    }
    if (info->format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        return JNI_ERR;
    }

//    size_t size = info->width * info->height * 4;
//    unsigned char *cpyDat = new unsigned char[size];
//    memcpy(cpyDat, data, size);


    int w = info->width;
    int h = info->height;
    int radius = jRadius;
    int* pix = reinterpret_cast<int *>(data);

    int wm = w - 1;
    int hm = h - 1;
    int wh = w * h;
    int div = radius + radius + 1;

    int *r = (int *)malloc(wh * sizeof(int));
    int *g = (int *)malloc(wh * sizeof(int));
    int *b = (int *)malloc(wh * sizeof(int));
    int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;

    int *vmin = (int *)malloc(max(w,h) * sizeof(int));

    int divsum = (div + 1) >> 1;
    divsum *= divsum;
    int *dv = (int *)malloc(256 * divsum * sizeof(int));
    for (i = 0; i < 256 * divsum; i++) {
        dv[i] = (i / divsum);
    }

    yw = yi = 0;

    int(*stack)[3] = (int(*)[3])malloc(div * 3 * sizeof(int));
    int stackpointer;
    int stackstart;
    int *sir;
    int rbs;
    int r1 = radius + 1;
    int routsum, goutsum, boutsum;
    int rinsum, ginsum, binsum;

    for (y = 0; y < h; y++) {
        rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
        for (i = -radius; i <= radius; i++) {
            p = pix[yi + (min(wm, max(i, 0)))];
            sir = stack[i + radius];
            sir[0] = (p & 0xff0000) >> 16;
            sir[1] = (p & 0x00ff00) >> 8;
            sir[2] = (p & 0x0000ff);

            rbs = r1 - abs(i);
            rsum += sir[0] * rbs;
            gsum += sir[1] * rbs;
            bsum += sir[2] * rbs;
            if (i > 0) {
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
            }
            else {
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
            }
        }
        stackpointer = radius;

        for (x = 0; x < w; x++) {

            r[yi] = dv[rsum];
            g[yi] = dv[gsum];
            b[yi] = dv[bsum];

            rsum -= routsum;
            gsum -= goutsum;
            bsum -= boutsum;

            stackstart = stackpointer - radius + div;
            sir = stack[stackstart % div];

            routsum -= sir[0];
            goutsum -= sir[1];
            boutsum -= sir[2];

            if (y == 0) {
                vmin[x] = min(x + radius + 1, wm);
            }
            p = pix[yw + vmin[x]];

            sir[0] = (p & 0xff0000) >> 16;
            sir[1] = (p & 0x00ff00) >> 8;
            sir[2] = (p & 0x0000ff);

            rinsum += sir[0];
            ginsum += sir[1];
            binsum += sir[2];

            rsum += rinsum;
            gsum += ginsum;
            bsum += binsum;

            stackpointer = (stackpointer + 1) % div;
            sir = stack[(stackpointer) % div];

            routsum += sir[0];
            goutsum += sir[1];
            boutsum += sir[2];

            rinsum -= sir[0];
            ginsum -= sir[1];
            binsum -= sir[2];

            yi++;
        }
        yw += w;
    }
    for (x = 0; x < w; x++) {
        rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
        yp = -radius * w;
        for (i = -radius; i <= radius; i++) {
            yi = max(0, yp) + x;

            sir = stack[i + radius];

            sir[0] = r[yi];
            sir[1] = g[yi];
            sir[2] = b[yi];

            rbs = r1 - abs(i);

            rsum += r[yi] * rbs;
            gsum += g[yi] * rbs;
            bsum += b[yi] * rbs;

            if (i > 0) {
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
            }
            else {
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
            }

            if (i < hm) {
                yp += w;
            }
        }
        yi = x;
        stackpointer = radius;
        for (y = 0; y < h; y++) {
            // Preserve alpha channel: ( 0xff000000 & pix[yi] )
            pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

            rsum -= routsum;
            gsum -= goutsum;
            bsum -= boutsum;

            stackstart = stackpointer - radius + div;
            sir = stack[stackstart % div];

            routsum -= sir[0];
            goutsum -= sir[1];
            boutsum -= sir[2];

            if (x == 0) {
                vmin[y] = min(y + r1, hm) * w;
            }
            p = x + vmin[y];

            sir[0] = r[p];
            sir[1] = g[p];
            sir[2] = b[p];

            rinsum += sir[0];
            ginsum += sir[1];
            binsum += sir[2];

            rsum += rinsum;
            gsum += ginsum;
            bsum += binsum;

            stackpointer = (stackpointer + 1) % div;
            sir = stack[stackpointer];

            routsum += sir[0];
            goutsum += sir[1];
            boutsum += sir[2];

            rinsum -= sir[0];
            ginsum -= sir[1];
            binsum -= sir[2];

            yi += w;
        }
    }

    free(r);
    free(g);
    free(b);
    free(vmin);
    free(dv);
    free(stack);
    AndroidBitmap_unlockPixels(env, inBitmap);
    return JNI_OK;
}

static const JNINativeMethod bitmaputilsMethod[] = {
    {"native_blur", "(Landroid/graphics/Bitmap;I)I", (void *) bitmapUtils_native_blur}
};

int register_nativelib_BitmapUtils(JNIEnv *env) {
    jclass cls = env->FindClass(CLASS_NAME.c_str());
    if (cls == NULL) {
        return JNI_ERR;
    }
    // 注册方法
    jint nRes = env->RegisterNatives(cls, bitmaputilsMethod, sizeof(bitmaputilsMethod)/sizeof(bitmaputilsMethod[0]));
    if (nRes < 0) {
        return JNI_ERR;
    }

    return JNI_OK;
}

#endif
