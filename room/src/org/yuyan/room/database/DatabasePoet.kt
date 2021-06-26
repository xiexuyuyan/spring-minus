package org.yuyan.room.database

import com.squareup.kotlinpoet.*
import org.yuyan.room.base.RoomDatabase
import java.lang.Exception
import java.lang.System.out
import java.lang.reflect.Modifier
import javax.annotation.processing.Filer
import kotlin.reflect.KClass


/**
 * Created by: As10970 2021/6/26 15:07.
 * Project: Vicar.
 */

class DatabasePoet {

    /**
     * @param proxy the class be proxy-ed
     * */
    fun generatedDatabaseImpl(pkgName: String, _clsName: String, superPkgName: String, superClsName: String, filer: Filer) {
        val clsName: String = _clsName + RoomDatabase.DB_IMPL_SUFFIX
        println("generate cls: $pkgName.$clsName")
        val fullName = pkgName + clsName
        val databaseClass = ClassName(pkgName, clsName)
        println(pkgName + "." + clsName)
        val databaseSuperClass = ClassName(pkgName, _clsName)
        println(superPkgName + "." + superClsName)
        val clsType: TypeSpec = TypeSpec.classBuilder(databaseClass)
                .superclass(databaseSuperClass)
                .addFunction(FunSpec.builder("userDao")
                    .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
                    .addStatement("println(\"hello kotlin poet\")")
                    .build())
                .build()
        val file = FileSpec.builder(packageName = pkgName, clsName)
                .addType(clsType)
                .build()
        file.writeTo(filer)
        file.writeTo(out)
    }
}