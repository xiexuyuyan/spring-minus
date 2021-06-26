package org.yuyan.room.annotation.processor

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@SupportedAnnotationTypes("org.yuyan.room.database.Database")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class DatabaseAnnotationProcessor: AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>?
                         , roundEnv: RoundEnvironment?): Boolean {
        roundEnv?.rootElements.let { it ->
            it?.forEach {
                println("input file: $it")
            }
        }
        return false
    }
}