// these settings can be overriden in ~/.grails/transmartConfig/DataSource-rest-api.groovy

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
//    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
}

environments {
    development {
        driverClassName = 'org.postgresql.Driver'
        url             = 'jdbc:postgresql://localhost:5432/transmart'
        dialect         = 'org.hibernate.dialect.PostgreSQLDialect'

    //    driverClassName = 'oracle.jdbc.driver.OracleDriver'
    //    url             = 'jdbc:oracle:thin:@localhost:11521:CI'
    //    dialect         = 'org.hibernate.dialect.Oracle10gDialect'

        username        = 'biomart_user'
        password        = 'biomart_user'
        dbCreate        = 'none'
    }
}

environments {
    development {
        dataSource {
            logSql    = true
            formatSql = true

            properties {
                maxActive   = 10
                maxIdle     = 5
                minIdle     = 2
                initialSize = 2
            }
        }
    }

    test {
        dataSource {
            driverClassName = 'org.h2.Driver'
            url             = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;INIT=RUNSCRIPT FROM './h2_init.sql'"
            username        = 'sa'
            password        = ''
            dbCreate        = 'update'
            logSql          = true
            formatSql       = true
        }
    }
}

// vim: set ts=4 sw=4 et:
