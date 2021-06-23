package room.applying

import room.framework.enty.ColumnInfo
import room.framework.enty.Entity
import room.framework.enty.PrimaryKey

@Entity
data class User (
        @PrimaryKey val uid: Int,
        @ColumnInfo(name = "user_name") val name: String,
        @ColumnInfo(name = "user_mail") val mail: String
)