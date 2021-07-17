package com.yuyan.room

import org.yuyan.room.dao.*
import org.yuyan.room.entity.ColumnInfo


@Dao
interface UserDao {
    @Insert(entity = User::class)
    fun insert(user: User)

    @Query(statement = "select * from users where uid like :uid limit 1")
    fun getUserByUid(uid: Int): User

    @Query(statement = "select * from users order by uid desc limit 1")
    fun getLastUser(): User

    @Query(statement = "select * from experience where uid like :uid limit 1")
    fun getExperienceByUid(uid: Int): Experience

    @Update(entity = User::class)
    fun update(user: User)

    @Insert(entity = Login::class)
    fun login(login: Login)
}