package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.sourcecode.LanguageSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import java.io.File

class CppAnalyser(override val context: SourceCodeContext) : LanguageSourceCodeAnalyser {
    private val impl = chapi.ast.cppast.CPPAnalyser()
    private val logger = org.slf4j.LoggerFactory.getLogger(this.javaClass)

    override fun analyse(): List<CodeDataStruct> = runBlocking {
        getFilesByPath(context.path) {
            it.absolutePath.endsWith(".hpp") || it.absolutePath.endsWith(".h")
        }.map {
            async { impl.addSource(it.readContent()) }
        }

        getFilesByPath(context.path)
            .filter { it.absolutePath.endsWith(".c") }

        getFilesByPath(context.path) {
            it.absolutePath.endsWith(".c") || it.absolutePath.endsWith(".cpp")
        }
            .map { async { analysisByFile(it) } }.awaitAll()
            .flatten()
    }

    private fun analysisByFile(file: File): List<CodeDataStruct> {
        logger.info("analysis file: ${file.absolutePath}")
        val content = file.readContent()
        val lines = content.lines()
        val codeContainer = impl.analysis(content, file.name)

        return codeContainer.DataStructures.map { ds ->
            ds.apply {
                ds.Imports = codeContainer.Imports
                ds.FilePath = file.absolutePath

                if (context.withFunctionCode) {
                    ds.Content = contentByPosition(lines, ds.Position)
                    ds.Functions.map { it.apply { it.Content = contentByPosition(lines, it.Position) } }
                }
            }
        }
    }
}
