package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.JClass
import com.thoughtworks.archguard.module.domain.JClassRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class JClassRepositoryImpl : JClassRepository {
    @Autowired
    lateinit var jdbi: Jdbi

    override fun getJClassByName(name: String): JClass {
        val sql = "select id, name, module from JClass where name='$name'"
        return jdbi.withHandle<JClass, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClass::class.java))
            it.createQuery(sql)
                    .mapTo(JClass::class.java)
                    .one()
        }
    }
}