package org.yuyan.room.annotation.processor

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import org.yuyan.room.base.RoomDatabase
import org.yuyan.room.database.Database
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.util.ElementFilter

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("org.yuyan.room.database.Database")
class DatabaseAnnotationProcessor: AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>?
                         , roundEnv: RoundEnvironment?): Boolean {
        if (!ifProcess(annotations, roundEnv)) {
            return false
        }

        val targetAnnotationName: String = supportedAnnotationTypes.elementAt(0)
        println(targetAnnotationName)
        val annotatedTypeElements = findAnnotatedTypeElementByName(targetAnnotationName
            , roundEnv?.rootElements)
        annotatedTypeElements.forEach {element ->

            val clsBuilder = formClassBuilder(element)
            println("class: ${clsBuilder.build().name}")
            ElementFilter.methodsIn(element.enclosedElements).forEach {
                println("method:${it.modifiers} ${it.simpleName}")
            }
        }
        return false
    }














    /*roundEnv?.rootElements.let { inputElements ->// class list in env to apply processor
        inputElements?.forEach {inputElement ->// class loaded in env to apply processor
            // traverse all input elements and find the target annotation
            val databaseAnnotationTypeElement = findTypeElementByAnnotationMirrors("", inputElement)
            databaseAnnotationTypeElement?.let {dbElement ->
                val pkgName: String = dbElement.enclosingElement.toString()
                val clsName: String = (dbElement.qualifiedName.toString()).split("$pkgName.")[1]
                // println(pkgName + "." + clsName)

                val dbSuperElement: TypeElement = processingEnv.typeUtils.asElement(dbElement.superclass) as TypeElement
                val superPkgName: String = dbSuperElement.enclosingElement.toString()
                val superClsName: String = (dbSuperElement.qualifiedName.toString()).split("$superPkgName.")[1]
                // println(superPkgName + "." + superClsName)

                val databaseClass = ClassName(pkgName, clsName)

                DatabasePoet().generatedDatabaseImpl(pkgName, clsName, superPkgName, superClsName, filer)
            }
        }
    }*/

/*    private fun findAnnotatedTypeElementByName(clsName: String, inputElements: MutableSet<out Element>?): HashSet<TypeElement>{
        val annotatedTypeElements = HashSet<TypeElement>()
        inputElements?.forEach { element ->
            val mirrors = element.annotationMirrors
            mirrors.forEach { annotationMirror ->
                when (annotationMirror.annotationType.toString()) {
                    clsName -> {
                        annotatedTypeElements.add(element = element as TypeElement)
                    }
                }
            }
        }
        return annotatedTypeElements
    }*/
}
