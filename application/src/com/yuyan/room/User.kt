package com.yuyan.room

import org.yuyan.room.entity.ColumnInfo
import org.yuyan.room.entity.Entity
import org.yuyan.room.entity.PrimaryKey

@Entity(tableName = "users")
data class User (
        @ColumnInfo(name = "uid")
        @PrimaryKey val uid: Int,
        @ColumnInfo(name = "user_name") val name: String,
        @ColumnInfo(name = "user_mail") val mail: String
)