#include <windows.h>
#include <stdio.h>

#define BUF_SIZE 1025
char szName[] = "NameOfMappingObject";    // 共享内存的名字

int main()
{
	    // 创建共享文件句柄
	    HANDLE hMapFile = CreateFileMapping(
	            INVALID_HANDLE_VALUE,	// 物理文件句柄
	            NULL,					// 默认安全级别
	            PAGE_READWRITE,			// 可读可写
	            0,						// 高位文件大小
	            BUF_SIZE,					// 地位文件大小
	            szName			// 共享内存名称
	    );
	    if(hMapFile == NULL){
	        printf("share memory create fail\n");
	        return -1;
	    }
	
	    // 映射缓存区视图 , 得到指向共享内存的指针
	    char *pBuf = (char *)MapViewOfFile(
	            hMapFile,            // 共享内存的句柄
	            FILE_MAP_ALL_ACCESS, // 可读写许可
	            0,
	            0,
	            BUF_SIZE
	    );
	
		HANDLE sema = CreateSemaphore(NULL, 0, 1, "com.yuyan.harp"); //创建信号量，初始资源为零，最大并发数为1，
	    while(1)
	    {
	        // printf("press any button to receive data..." );
	        // getchar();
			WaitForSingleObject(sema, INFINITE); //等待信号量资源数>0
	        printf("buff is %s\n",pBuf );
	        memset(pBuf,0,sizeof(pBuf));
	    }
	    // 解除文件映射
	    UnmapViewOfFile(pBuf);
	    // 关闭内存映射文件对象句柄
	    CloseHandle(hMapFile);
	    return 0;
}