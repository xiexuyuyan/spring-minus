package com.yuyan.web.share

import com.yuyan.data.Result
import com.yuyan.data.ResultInWeb
import com.yuyan.web.share.model.FileDescriptionInResult
import org.yuyan.springmvc.beans.AutoWired
import org.yuyan.springmvc.beans.Bean

@Bean
class ShareService {
    @AutoWired
    private var mDataSource: FileDataSource? = null

    @Suppress("UNCHECKED_CAST")
    fun loadAllFiles(uid: Int): ResultInWeb {
        val dataSource = mDataSource
                ?: return ResultInWeb.error()

        val loadResult = dataSource.loadAllFile(uid)

        if (loadResult is Result.Error) {
            return ResultInWeb.error().with("details", loadResult.error.message)
        }

        val success = loadResult as Result.Success<*>
        val data: List<FileDescriptionInResult> = success.data as List<FileDescriptionInResult>

        return ResultInWeb.success().apply {
            put("data", data.toTypedArray())
        }
    }
}