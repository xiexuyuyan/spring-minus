#include "droid_server_SystemServer.h"

#include <windows.h>
#include <stdio.h>

#define BUF_SIZE 1025


void handleExec(JNIEnv *env, jobject o, jobject obj_handler, jstring objString);
jstring charTojstring(JNIEnv* env, const char* str);
char* jstringToChar(JNIEnv *env, jstring jstr);


/*
 * Class:     droid_server_SystemServer
 * Method:    waitNativeMessage
 * Signature: (Ldroid/message/Handler;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_droid_server_SystemServer_waitNativeMessage
 (JNIEnv* env, jobject o, jobject obj_handler, jstring string_memory_name, jstring string_semaphore_name) {
    char* memory_name = jstringToChar(env, string_memory_name);
	// 创建共享文件句柄
	HANDLE hMapFile = CreateFileMapping(
	        INVALID_HANDLE_VALUE,	// 物理文件句柄
	        NULL,					// 默认安全级别
	        PAGE_READWRITE,			// 可读可写
	        0,						// 高位文件大小
	        BUF_SIZE,					// 地位文件大小
	        memory_name			// 共享内存名称
	);
	if(hMapFile == NULL){
	    printf("share memory create fail\n");
	    return;
	}
	
	// 映射缓存区视图 , 得到指向共享内存的指针
	char *pBuf = (char *)MapViewOfFile(
	        hMapFile,            // 共享内存的句柄
	        FILE_MAP_ALL_ACCESS, // 可读写许可
	        0,
	        0,
	        BUF_SIZE
	);
	char* semaphore_name = jstringToChar(env, string_semaphore_name);
	HANDLE sema = CreateSemaphore(NULL, 0, 1, semaphore_name); //创建信号量，初始资源为零，最大并发数为1，
	while(1)
	{
	    // printf("press any button to receive data..." );
	    // getchar();
		WaitForSingleObject(sema, INFINITE); //等待信号量资源数>0
	    printf("Java_com_yuyan_byjni_ContextThread_waitForHandler buff is %s\n",pBuf );
        handleExec(env, o, obj_handler, charTojstring(env, pBuf));
	    memset(pBuf, 0, sizeof(pBuf));
	}
	// 解除文件映射
	UnmapViewOfFile(pBuf);
	// 关闭内存映射文件对象句柄
	CloseHandle(hMapFile);
}

void handleExec(JNIEnv *env, jobject o, jobject obj_handler, jstring objString) {
    //1.获取jclass SystemServer
    jclass clz_systemserver = env->GetObjectClass(o);
    //2.获取system server声明的native message的what值
    jfieldID field_native_message_what = env->GetStaticFieldID(clz_systemserver, "NATIVE_MESSAGE", "I");
    //3.通过jfid获取age的属性值,注意用GetStaticIntField
    jint what_native_message = env->GetStaticIntField(clz_systemserver, field_native_message_what);

    // Message的类信息
    jclass clz_message= env->FindClass("droid/message/Message");
    // 找出Message的构造方法
    jmethodID init_message = env->GetMethodID(clz_message, "<init>", "()V");
    // jmethodID init_message = env->AllocObject(clz_message);
    // 构造要调用java方法对应的类对象
    jobject obj_message = env->NewObject(clz_message, init_message);
    // 找出Message的what字段
    jfieldID field_what = env->GetFieldID(clz_message, "what", "I");
    // 为what字段设置值
    env->SetIntField(obj_message, field_what, what_native_message);

    // 找到Message的obj字段
    jfieldID field_obj = env->GetFieldID(clz_message, "obj", "Ljava/lang/Object;");  
    // 为obj字段赋值
    // env->SetObjectField(obj_message, field_obj, (env)->NewStringUTF("mike")); 
    env->SetObjectField(obj_message, field_obj, objString); 


    // 找到Handler的类信息
    jclass clz_handler = env->GetObjectClass(obj_handler);
    // 找出Handler的sendMessage方法
    jmethodID method_sendMessage = env->GetMethodID(clz_handler, "sendMessage", "(Ldroid/message/Message;)Z");

    env->CallVoidMethod(obj_handler, method_sendMessage, obj_message);
}

// 由于jvm和c++对中文的编码不一样，因此需要转码。gb2312转换成utf8/16
jstring charTojstring(JNIEnv* env, const char* str) {
    jstring rtn = 0;
    int slen = strlen(str);
    unsigned short * buffer = 0;
    if (slen == 0)
        rtn = (env)->NewStringUTF(str);
    else {
        int length = MultiByteToWideChar( CP_ACP, 0, (LPCSTR) str, slen, NULL, 0);
        buffer = (unsigned short *) malloc(length * 2 + 1);
        if (MultiByteToWideChar( CP_ACP, 0, (LPCSTR) str, slen, (LPWSTR) buffer, length) > 0)
            rtn = (env)->NewString((jchar*) buffer, length);
        // 释放内存
        free(buffer);
    }
    return rtn;
}
char* jstringToChar(JNIEnv *env, jstring jstr) {
    int length = (env)->GetStringLength(jstr);
    const jchar* jcstr = (env)->GetStringChars(jstr, 0);
    char* rtn = (char*) malloc(length * 2 + 1);
    int size = 0;
    size = WideCharToMultiByte( CP_ACP, 0, (LPCWSTR) jcstr, length, rtn,
            (length * 2 + 1), NULL, NULL);
    if (size <= 0)
        return NULL;
    (env)->ReleaseStringChars(jstr, jcstr);
    rtn[size] = 0;
    return rtn;
}
