//
// Created by Administrator on 11/17/2016.
//

#ifndef FACERECOGNITION_LOG_H
#define FACERECOGNITION_LOG_H
#define TAG "DMUI"
#include <android/log.h>

#ifdef DEBUG_MODE
#define LOGI(...) \
        __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__); \
        __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)
#else
#define LOGI(...) \
        __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)
#endif

//#define LOGI(...) \
//        __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)

#define LOGD(...) \
        __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)

#define LOGW(...) \
        __android_log_print(ANDROID_LOG_WARN,TAG,__VA_ARGS__)


#endif //FACERECOGNITION_LOG_H
