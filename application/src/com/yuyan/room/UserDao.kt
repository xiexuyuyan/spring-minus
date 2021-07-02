package com.yuyan.room

import org.yuyan.room.dao.*


@Dao
interface UserDao {

    @Insert(entity = User::class)
    fun insert(user: User)

}