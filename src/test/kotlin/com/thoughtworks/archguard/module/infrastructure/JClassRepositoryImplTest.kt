package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.model.ClazzType
import com.thoughtworks.archguard.module.domain.model.JClass
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.web.WebAppConfiguration

@SpringBootTest
@WebAppConfiguration
internal class JClassRepositoryImplTest {
    @Autowired
    lateinit var jClassRepository: JClassRepository

    @Test
    @Sql("classpath:sqls/insert_jclass.sql")
    internal fun should_get_jclass_by_name() {
        val jClass = jClassRepository.getJClassBy("org.apache.dubbo.demo.GreetingService", "dubbo-demo-interface")
        val expectedClass = JClass("c1983476-7bd8-4e52-a523-71c4f3f5098e", "org.apache.dubbo.demo.GreetingService", "dubbo-demo-interface")
        expectedClass.classType = ClazzType.INTERFACE
        assertThat(jClass).isEqualToComparingFieldByField(expectedClass)

        val jClass2 = jClassRepository.getJClassBy("org.apache.dubbo.demo.DemoService", "dubbo-demo-interface")
        val expectedClass2 = JClass("c65ee9c2-dab5-4ebb-8a0f-b8682eddd9d8", "org.apache.dubbo.demo.DemoService", "dubbo-demo-interface")
        expectedClass2.classType = ClazzType.NOT_DEFINED
        assertThat(jClass2).isEqualToComparingFieldByField(expectedClass2)
    }
}