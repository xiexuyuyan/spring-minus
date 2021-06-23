package room.applying

import room.framework.dao.Dao
import room.framework.dao.Delete
import room.framework.dao.Insert
import room.framework.dao.Query


/**
 * Created by: As10970 2021/6/23 9:53.
 * Project: Fraud.
 */

@Dao
interface UserDao {
    @Query("")
    fun getAll(): List<User>

    @Insert
    fun insert(vararg user: User)

    @Delete
    fun delete(user: User)
}