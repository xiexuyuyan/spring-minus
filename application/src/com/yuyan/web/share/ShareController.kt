package com.yuyan.web.share

import com.yuyan.data.ResultInWeb
import com.yuyan.room.UserDatabaseHelper
import com.yuyan.room.WebSession
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

    @RequestMapping("/web/get_all_file.jsp")
    fun getAllFiles(): String{
        val sessionKey = (request as HttpServletRequest).getHeader("session_key")
        val webSession: WebSession = database.webSessionDao().getSessionBySessionKey(sessionKey.toInt())
        if (webSession.uid == 0) {
            val resultInWeb = ResultInWeb.error("10002", "null of login")
            return resultInWeb.jsonString()
        }

        val shareService = mShareService
                ?: return ResultInWeb.error().jsonString()

        val resultString = shareService.loadAllFiles(webSession.uid).jsonString()
        println("resultString = $resultString")
        return resultString
    }
}

















