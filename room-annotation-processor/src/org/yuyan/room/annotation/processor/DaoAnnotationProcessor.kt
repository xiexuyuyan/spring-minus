package org.yuyan.room.annotation.processor

import com.squareup.kotlinpoet.*
import java.lang.System.out
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.type.DeclaredType
import javax.lang.model.util.ElementFilter
import kotlin.reflect.KClass

@SupportedAnnotationTypes("org.yuyan.room.dao.Dao")
@SupportedSourceVersion(SourceVersion.RELEASE_14)
class DaoAnnotationProcessor: AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        if (!ifProcess(annotations, roundEnv)) {
            return false
        }

        val targetAnnotationName: String = supportedAnnotationTypes.elementAt(0)
        val annotatedTypeElements = findAnnotatedTypeElementByName(targetAnnotationName
            , roundEnv?.rootElements)
        println(targetAnnotationName)

        annotatedTypeElements.forEach {element ->
            val clsBuilder: TypeSpec.Builder = formClassBuilder(element)
            println("class: ${clsBuilder.build().name}----------------")

            ElementFilter.methodsIn(element.enclosedElements).forEach {
                println("method:${it.modifiers} ${it.simpleName}")
                clsBuilder.addFunction(formMethodBuilder(element = it).build())
            }

            val pkgName: String = element.enclosingElement.toString()
            val clsName: String = element.simpleName.toString()
            val kotlinFile = FileSpec.builder(pkgName, clsName)
                .addType(clsBuilder.build())
                .build()
            kotlinFile.writeTo(out)
            // kotlinFile.writeTo(filer = processingEnv.filer)
        }
        return false
    }

}