package org.yuyan.room.annotation.processor

import org.yuyan.room.base.RoomDatabase
import org.yuyan.room.database.Database
import org.yuyan.room.database.DatabasePoet
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.ElementFilter
import kotlin.reflect.KClass

@SupportedSourceVersion(SourceVersion.RELEASE_8)
class DatabaseAnnotationProcessor: AbstractProcessor() {

    private lateinit var filer: Filer
    private val databaseAnnotationKClass: KClass<*> = Database::class
    private val databaseAnnotationJClass: Class<Database> = Database::class.java
    private val databaseAnnotationCanonicalName: String = databaseAnnotationJClass.canonicalName

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        filer = processingEnv!!.filer
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return arrayOf(databaseAnnotationCanonicalName).toHashSet()
    }

    override fun process(annotations: MutableSet<out TypeElement>?
                         , roundEnv: RoundEnvironment?): Boolean {

        when (roundEnv?.processingOver()) {
            true -> return false
        }


        roundEnv?.rootElements.let { inputElements ->// class list in env to apply processor
            inputElements?.forEach {inputElement ->// class loaded in env to apply processor
                // traverse all input elements and find the target annotation
                val databaseAnnotationTypeElement = findTypeElementByAnnotationMirrors(databaseAnnotationCanonicalName, inputElement)
                databaseAnnotationTypeElement?.let {dbElement ->
                    val pkgName: String = dbElement.enclosingElement.toString()
                    val clsName: String = (dbElement.qualifiedName.toString()).split("$pkgName.")[1]
                    // println(pkgName + "." + clsName)

                    val dbSuperElement: TypeElement = processingEnv.typeUtils.asElement(dbElement.superclass) as TypeElement
                    val superPkgName: String = dbSuperElement.enclosingElement.toString()
                    val superClsName: String = (dbSuperElement.qualifiedName.toString()).split("$superPkgName.")[1]
                    // println(superPkgName + "." + superClsName)

                    DatabasePoet().generatedDatabaseImpl(pkgName, clsName, superPkgName, superClsName, filer)
                }
            }
        }

        return false
    }



    private fun findTypeElementByAnnotationMirrors(clsName: String, element: Element): TypeElement?{
        val mirrors = element.annotationMirrors
        mirrors.forEach { annotationMirror ->
            when (annotationMirror.annotationType.toString()) {
                clsName -> {
                    return element as TypeElement
                }
            }
        }
        return null
    }

    private fun findTypeElementByTypeMirrors(clsName: String, mirror: TypeMirror){
        val mirrors = mirror.annotationMirrors
        mirrors.forEach { annotationMirror ->
            println(annotationMirror.annotationType.toString())
            when (annotationMirror.annotationType.toString()) {
                clsName -> {
                    // println(clsName)
                }
            }
        }
    }
}
