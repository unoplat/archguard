package org.archguard.scanner.analyser

import org.archguard.scanner.core.document.DocumentContext
import org.archguard.scanner.core.document.DocumentContent

class DocumentAnalyser(override val context: DocumentContext) : org.archguard.scanner.core.document.DocumentAnalyser {
    override fun analyse(): List<DocumentContent> {
        return listOf()
    }

}