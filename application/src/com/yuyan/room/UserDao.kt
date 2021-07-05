package com.yuyan.room

import org.yuyan.room.dao.*
import org.yuyan.room.entity.ColumnInfo


@Dao
interface UserDao {

    @Insert(entity = User::class)
    fun insert(apple: User)

    @Query(statement = "select * from users where uid like :uid limit 1")
    fun getUserNameByUid(uid: Int): User

    @Query(statement = "select * from experience where uid like :uid limit 1")
    fun getExperienceByUid(uid: Int): Experience
}