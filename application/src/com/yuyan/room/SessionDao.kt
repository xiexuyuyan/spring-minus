package com.yuyan.room

import org.yuyan.room.dao.*
import org.yuyan.room.entity.ColumnInfo


@Dao
interface SessionDao {
    @Insert(entity = Login::class)
    fun login(login: Login)

    @Insert(entity = Session::class)
    fun insert(session: Session)

    @Update(entity = Session::class)
    fun update(session: Session)

    @Query(statement = "select * from session where uid like :uid limit 1")
    fun getSessionByUid(uid: Int): Session

    @Query(statement = "select * from session where session_user like :sessionUser limit 1")
    fun getSessionBySessionUser(sessionUser: Int): Session
}