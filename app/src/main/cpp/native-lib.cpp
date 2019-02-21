#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_cn_woong_opengl_AirHockeyActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
