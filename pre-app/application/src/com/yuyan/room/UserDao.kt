package com.yuyan.room

import org.yuyan.room.dao.*
import org.yuyan.room.entity.ColumnInfo


@Dao
interface UserDao {
    @Insert(entity = User::class)
    fun insert(user: User)

    @Query(statement = "select * from users where uid like :uid limit 1")
    fun getUserByUid(uid: Int): User

    @Query(statement = "select * from users where user_name like :name and user_mail like :mail limit 1")
    fun getUserByNameAndMail(name: String, mail: String): User

    @Query(statement = "select * from users order by uid desc limit 1")
    fun getLastUser(): User

    @Update(entity = User::class)
    fun update(user: User)

    @Insert(entity = Login::class)
    fun login(login: Login)
}