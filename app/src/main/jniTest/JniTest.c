
#include "com_testdemo_jnitest_JniTestNative.h"
#include <jni.h>
#include <string.h>


JNIEXPORT jstring JNICALL Java_com_testdemo_jnitest_JniTestNative_getString (JNIEnv * env, jobject obj ){
    jstring jstr;
    char cstr[]="hello jni!";
    jstr= (*env)->NewStringUTF(env, cstr);
    return jstr;
}