package com.yuyan.room

import org.yuyan.room.dao.Dao
import org.yuyan.room.dao.Delete
import org.yuyan.room.dao.Insert
import org.yuyan.room.dao.Query


@Dao
interface UserDao {
    @Query("sentence")
    fun getAll(): List<User>

    @Insert
    fun insert(vararg users: User)

    @Delete
    fun delete(user: User)
}