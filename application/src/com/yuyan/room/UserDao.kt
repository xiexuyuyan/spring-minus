package com.yuyan.room

import org.yuyan.room.dao.Dao
import org.yuyan.room.dao.Delete
import org.yuyan.room.dao.Insert
import org.yuyan.room.dao.Query


@Dao
interface UserDao {
    @Query("")
    fun getAll(): List<User>

    @Insert
    fun insert(vararg user: User)

    @Delete
    fun delete(user: User)
}