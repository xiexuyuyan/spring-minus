package com.yuyan.harp.room

import org.yuyan.room.dao.*


@Dao
interface WebSessionDao {
    @Insert(entity = Login::class)
    fun login(login: Login)

    @Insert(entity = WebSession::class)
    fun insert(webSession: WebSession)

    @Update(entity = WebSession::class)
    fun update(webSession: WebSession)

    @Query(statement = "select * from web_session where uid like :uid limit 1")
    fun getSessionByUid(uid: Int): WebSession

    @Query(statement = "select * from web_session where session_key like :sessionKey limit 1")
    fun getSessionBySessionKey(sessionKey: Int): WebSession
}