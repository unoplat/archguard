package com.thoughtworks.archgard.scanner2.appl

import com.thoughtworks.archgard.scanner2.domain.model.ClassMetric
import com.thoughtworks.archgard.scanner2.domain.repository.CircularDependencyMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.ClassMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archgard.scanner2.domain.service.AbcService
import com.thoughtworks.archgard.scanner2.domain.service.CircularDependencyService
import com.thoughtworks.archgard.scanner2.domain.service.DitService
import com.thoughtworks.archgard.scanner2.domain.service.LCOM4Service
import com.thoughtworks.archgard.scanner2.domain.service.NocService
import com.thoughtworks.archgard.scanner2.infrastructure.influx.ClassMetricsDtoListForWriteInfluxDB
import com.thoughtworks.archgard.scanner2.infrastructure.influx.InfluxDBClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MetricPersistApplService(val abcService: AbcService,
                               val ditService: DitService,
                               val lcoM4Service: LCOM4Service,
                               val nocService: NocService,
                               val jClassRepository: JClassRepository,
                               val circularDependencyService: CircularDependencyService,
                               val classMetricRepository: ClassMetricRepository,
                               val circularDependencyMetricRepository: CircularDependencyMetricRepository,
                               val influxDBClient: InfluxDBClient) {

    @Transactional
    fun persistLevel2Metrics(systemId: Long) {
        val jClasses = jClassRepository.getJClassesHasModules(systemId)

        val abcMap = abcService.calculate(systemId, jClasses)
        val ditMap = ditService.calculate(systemId, jClasses)
        val nocMap = nocService.calculate(systemId, jClasses)
        val lcom4Map = lcoM4Service.calculate(systemId, jClasses)

        val classMetrics = mutableListOf<ClassMetric>()
        jClasses.forEach {
            classMetrics.add(ClassMetric(systemId, it.toVO(),
                    abcMap[it.id], ditMap[it.id], nocMap[it.id], lcom4Map[it.id]))
        }

        classMetricRepository.insertOrUpdateClassMetric(systemId, classMetrics)
        influxDBClient.save(ClassMetricsDtoListForWriteInfluxDB(classMetrics).toRequestBody())
    }

    @Transactional
    fun persistCircularDependencyMetrics(systemId: Long) {
        val classCircularDependency = circularDependencyService.getClassCircularDependency(systemId)
        val methodCircularDependency = circularDependencyService.getMethodCircularDependency(systemId)
        circularDependencyMetricRepository.saveClassCircularDependency(classCircularDependency)
        circularDependencyMetricRepository.saveMethodCircularDependency(methodCircularDependency)
    }
}

