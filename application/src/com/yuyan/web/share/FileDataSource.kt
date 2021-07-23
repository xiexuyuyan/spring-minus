package com.yuyan.web.share

import com.yuyan.data.Result
import com.yuyan.web.share.model.FileDescriptionInResult
import org.yuyan.springmvc.beans.Bean
import java.io.File
import java.lang.Exception

@Bean

class FileDataSource {
    fun loadAllFile(uid: Int): Result<*> {
        val rootDir = ShareFileManager.root()
        val rootFiles: Array<File> = rootDir.listFiles()
                ?: return Result.Error(Exception("null of the root directory"))

        val files = ArrayList<FileDescriptionInResult>()

        for (file in rootFiles) {
            files.add(FileDescriptionInResult(file.name, file.lastModified(), file.canonicalPath, file.isFile))
        }

        return Result.Success(files)
    }
}