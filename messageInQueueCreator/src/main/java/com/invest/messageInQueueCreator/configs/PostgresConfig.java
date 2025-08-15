package com.invest.messageInQueueCreator.configs;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@Profile("!test")
@EnableJpaRepositories(basePackages = "com.invest.messageInQueueCreator.repositories",
        transactionManagerRef="postgresTransactionManager",
        entityManagerFactoryRef = "postgresEntityManagerFactory")
public class PostgresConfig {

    @Value("${spring.datasource.url}")
    private String urlDatabase;

    @Value("${spring.datasource.username}")
    private String usernameDatabase;

    @Value("${spring.datasource.password}")
    private String passwordDatabase;

    private static final String PERSISTENCE_UNIT = "postgres";
    private static final String DRIVER_POSTGRESQL = "org.postgresql.Driver";

    @Primary
    @Bean(name = "postgresDataSource")
    public DataSource postgresDataSource() {
        return DataSourceBuilder.create()
                .url(urlDatabase)
                .username(usernameDatabase)
                .password(passwordDatabase)
                .driverClassName(DRIVER_POSTGRESQL)
                .build();
    }

    @Primary
    @Bean(name = "postgresEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean postEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(postgresDataSource())
                .packages("com.invest.messageInQueueCreator.entities")
                .persistenceUnit(PERSISTENCE_UNIT)
                .build();
    }

    @Primary
    @Bean(name = "postgresTransactionManager")
    public PlatformTransactionManager postgresTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
