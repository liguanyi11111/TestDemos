LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := JniTest.c
LOCAL_MODULE := JniTest

include $(BUILD_SHARED_LIBRARY)