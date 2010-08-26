datasources = {
  datasource(name: 'ds2') {
    domainClasses([event])
    services(['hogeevent'])
    //readOnly(true)
    driverClassName('org.postgresql.Driver')
    url('jdbc:postgresql://localhost:5432/eventdb')
    username('postgres')
    password('postgres')
    //dbCreate('create-drop')
    logSql(false)
    dialect(org.hibernate.dialect.PostgreSQLDialect)
    pooled(true)
    //environments(['development'])
    hibernate {
      cache {
        provider_class('org.hibernate.cache.EhCacheProvider')
        use_second_level_cache(true)
        use_query_cache(true)
      }
    }
  }
}
