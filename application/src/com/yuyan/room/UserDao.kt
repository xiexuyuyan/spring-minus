package com.yuyan.room

import org.yuyan.room.dao.Dao
import org.yuyan.room.dao.Delete
import org.yuyan.room.dao.Insert
import org.yuyan.room.dao.Query


@Dao
interface UserDao {
    @Query("sentence")
    fun getAll(): MutableList<User>

    @Insert
    fun insert(userList: MutableList<User>)

    @Delete
    fun delete(user: User)
}