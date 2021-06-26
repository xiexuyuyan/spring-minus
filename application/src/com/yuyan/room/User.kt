package com.yuyan.room

import org.yuyan.room.entity.ColumnInfo
import org.yuyan.room.entity.Entity
import org.yuyan.room.entity.PrimaryKey

@Entity
data class User (
        @PrimaryKey val uid: Int,
        @ColumnInfo(name = "user_name") val name: String,
        @ColumnInfo(name = "user_mail") val mail: String
)