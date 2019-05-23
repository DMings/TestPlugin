#include <jni.h>
#include <string>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <list>
#include <assert.h>
#include "log.h"
#include "BitmapUtils.cpp"
//#include "MTFeatureDetector.h"

//#define TEST_ONE "ONEEEEEEE*"
//#define TEST_TWO "TWOOOOOOO*"
//
//int i = 5;
//
//extern "C" JNIEXPORT jstring
//
//JNICALL
//Java_com_dming_testndk_MainActivity_stringFromJNI(
//        JNIEnv *env,
//        jobject /* this */) {
////    const char *one = TEST_ONE;
////    const char *two = TEST_TWO;
////    size_t oneLen = strlen(one);
////    size_t twoLen = strlen(two);
////    char *newStr = (char *) malloc(oneLen + twoLen);
////    memset(newStr, 0, oneLen + twoLen);
////    char *nOne = strcat(newStr, TEST_ONE);
////    char *nTwo = strcat(nOne, TEST_TWO);
//
//    LOGI("i---%d",i);
//    for (int j = 0;j < 20;j++){
//        int bytes[i];
////        int *bytes = new int[i];
//        bytes[5] = 668;
//        uint8_t bytess[i];
//        for (int k = 0;k < i;k++){
//            bytess[k] = 77;
//        }
//        LOGI("bytes[5]: %d",bytes[5]);
//        LOGI("bytess---%ld", sizeof(bytess));
////        delete bytes;
//    }
////    LOGI("bytes---%ld", sizeof(bytes));
////    int bytes[i];
//
//
//    std::string nStr = TEST_ONE;
//    nStr += TEST_TWO;
//    nStr += "666->";
//
////    MTFeatureDetector mTFeatureDetector;
////    MTFeatureDetector *mTFeatureDetectorList = new MTFeatureDetector[i];
////    mTFeatureDetectorList[0] = mTFeatureDetector;
////
////    MTFeatureDetector *mTFeatureDetectorP = new MTFeatureDetector;
////    MTFeatureDetector **mTFeatureDetectorPList = new MTFeatureDetector*[5];
////    mTFeatureDetectorPList[0] = mTFeatureDetectorP;
//
//
//    return env->NewStringUTF(nStr.c_str());
//}
//
//JNIEXPORT jstring JNICALL native_test2(JNIEnv *env,jobject){
//    return env->NewStringUTF("native_test2");
//}
//JNIEXPORT jstring JNICALL native_test3(JNIEnv *env,jclass){
//    return env->NewStringUTF("native_test3native_test3");
//}

////注册Java端的方法  以及本地相对应的方法
//JNINativeMethod method[]={{"test2", "()Ljava/lang/String;", (void*)(native_test2)}, {"test3", "()Ljava/lang/String;", (void*)(native_test3)}};
//
////注册相应的类以及方法
//jint registerNativeMeth(JNIEnv *env){
//    jclass cl=env->FindClass("com/dming/testndk/NDKActivity");
//    if((env->RegisterNatives(cl,method,sizeof(method)/sizeof(method[0])))<0){
//        return -1;
//    }
//    return 0;
//}

int aa = 899;
int bb = 899;
int cc = 899;
void test1(int & b){
    b = 799;
    LOGI("aa=%d aa=%lld b=%lld",aa,&aa,&b);
}

void test2(int * b){
    *b = 799;
    LOGI("bb=%d bb=%lld b=%lld",bb,&bb,&b);
}

void test3(int * b){
    int c = 233;
    b = &c;
    LOGI("cc=%d b=%d",cc,*b);
}

extern "C" JNIEXPORT void
JNICALL
Java_com_dming_testndk_NDKActivity_testCpp(
        JNIEnv *env,
        jobject /* this */) {
    test1(aa);
    test2(&bb);
    test3(&cc);
//    exit(0);
//    int str = 0;
//    assert(str > 0);
}

//实现jni_onload 动态注册方法
jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env = NULL;
    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }
//    if(registerNativeMeth(env)!=JNI_OK){//注册方法
//        return -1;
//    }
//    register_nativelib_BitmapUtils(env);
    return JNI_VERSION_1_4;//必须返回这个值
}