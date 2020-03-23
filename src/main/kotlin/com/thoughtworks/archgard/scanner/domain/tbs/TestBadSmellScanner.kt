package com.thoughtworks.archgard.scanner.domain.tbs

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.Scanner
import com.thoughtworks.archgard.scanner.domain.toolscanners.CocaScanner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TestBadSmellScanner(@Autowired val testBadSmellRepo: TestBadSmellRepo) : Scanner {

    private val mapper = jacksonObjectMapper()

    override fun scan(context: ScanContext) {

        val cocaScanner = CocaScanner(context.projectRoot)
        val report = cocaScanner.getTestBadSmellReport()
        val testBadSmells = mapper.readValue<List<CocaTestBadSmellModel>>(report)
                .map { m -> TestBadSmell(UUID.randomUUID().toString(), m.Line, m.FileName, m.Description, m.Type) }
        testBadSmellRepo.save(testBadSmells)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CocaTestBadSmellModel(val FileName: String, val Type: String, val Description: String, val Line: Int)

}