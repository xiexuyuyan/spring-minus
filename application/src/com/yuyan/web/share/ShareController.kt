package com.yuyan.web.share

import com.yuyan.data.ResultInWeb
import com.yuyan.room.UserDatabaseHelper
import com.yuyan.room.WebSession
import com.yuyan.web.session.getUidFromSession
import org.yuyan.springmvc.beans.AutoWired
import org.yuyan.springmvc.beans.TypeServletRequest
import org.yuyan.springmvc.beans.TypeServletResponse
import org.yuyan.springmvc.web.mvc.Controller
import org.yuyan.springmvc.web.mvc.RequestMapping
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Controller
class ShareController {
    @AutoWired
    private val mShareService: ShareService? = null

    @TypeServletRequest
    private val request: ServletRequest? = null

    @TypeServletResponse
    private val response: ServletResponse? = null

    private val database = UserDatabaseHelper.get()

    @RequestMapping("/web/get_all_files.jsp")
    fun getAllFiles(): String{
        val uid = getUidFromSession(request = request as HttpServletRequest, database = database).run {
            if (first == 0) {
                return second.jsonString()
            }
            first
        }

        val shareService = mShareService
                ?: return ResultInWeb.create(10004).with("details", "it performance that [mShareService] are not init").jsonString()

        val resultString = shareService.loadAllFiles(uid).jsonString()
        println("resultString = $resultString")
        return resultString
    }

    @RequestMapping("/web/get_user_root.jsp")
    fun getUserRoot(): String {
        val uid = getUidFromSession(request = request as HttpServletRequest, database = database).run {
            if (first == 0) {
                return second.jsonString()
            }
            first
        }
        (ShareFileManager.root(uid) ?: return ResultInWeb.error().with("details", "open user root dir failed").jsonString()).let {
            return ResultInWeb.success().with("data", it.canonicalPath).jsonString()
        }
    }
}

















