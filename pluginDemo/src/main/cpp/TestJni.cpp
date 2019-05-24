#include <jni.h>
#include <string.h>

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }
    return JNI_VERSION_1_4;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_dming_testndk_TestNDKActivity_getMsgFromNDK(JNIEnv *env, jobject instance) {

    const char *msg = "Test Ndk Success!";
    return env->NewStringUTF(msg);
}