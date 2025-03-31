package com.traderbird.ksp

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import java.io.File
import java.io.OutputStream

@AutoService(SymbolProcessor::class)
class CollectFunctionProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(CollectFunction::class.qualifiedName!!)
        val collectedFunctions = mutableMapOf<String, MutableList<String>>()

        symbols.filterIsInstance<KSFunctionDeclaration>().forEach { function ->
            val annotation = function.annotations.find {
                it.shortName.asString() == "CollectFunction"
            }

            val targetClass = annotation?.arguments?.find { it.name?.asString() == "targetClass" }?.value as? KSType
            val targetField = annotation?.arguments?.find { it.name?.asString() == "targetField" }?.value as? String

            if (targetClass != null && targetField != null) {
                val parentClass = function.parentDeclaration as? KSClassDeclaration
                val functionQualifiedName =
                    "${parentClass?.qualifiedName?.asString()}.${function.simpleName.asString()}"

                val fieldKey = "${targetClass.declaration.qualifiedName?.asString()}.$targetField"
                collectedFunctions.getOrPut(fieldKey) { mutableListOf() }.add(functionQualifiedName)
            }
        }

        generateCode(collectedFunctions)
        return emptyList()
    }

    private fun generateCode(collectedFunctions: Map<String, List<String>>) {
        val packageName = "generated"
        val fileName = "CollectedFunctionRegistry"

        val file: OutputStream?

        try {
            file = codeGenerator.createNewFile(
                Dependencies.ALL_FILES,
                packageName,
                fileName
            )
        } catch (e: Exception) {
            // logger.error("Error creating file: $e")
            return
        }

        file.use { output ->
            output.write("package $packageName\n\n".toByteArray())
            output.write("object $fileName {\n".toByteArray())
            output.write("    @JvmStatic\n".toByteArray())
            output.write("    fun processCollectFunction() {\n".toByteArray())

            collectedFunctions.forEach { (field, functions) ->
                output.write("        $field.apply {\n".toByteArray())
                functions.forEach { function ->
                    output.write("            add { arg -> $function(arg) }\n".toByteArray())
                }
                output.write("        }\n".toByteArray())
            }

            output.write("    }\n".toByteArray())
            output.write("}\n".toByteArray())
        }
    }
}