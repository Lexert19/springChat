package com.example.springChat.config;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
@EnableR2dbcRepositories
//@EnableR2dbcRepositories(basePackages = {"com.example.springChat.repository.UserRepository"}, basePackageClasses = {})
public class R2DBCConfig{
   /* @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));

        return initializer;
    }*/

    @Bean
    public ConnectionFactoryInitializer testProfileInitializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        initializer.setDatabasePopulator(populator);

        return initializer;
    }


    @Autowired
    private Environment env;

    /*@Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
                .option("username", env.getProperty("spring.r2dbc.username"))
                .option()

        return ConnectionFactories..builder()
                        .url(env.getProperty("spring.r2dbc.url"))
                        .username(env.getProperty("spring.r2dbc.username"))
                        .password(env.getProperty("spring.r2dbc.password"))
                        .build();

    }*/

    /*@Bean
    public H2ConnectionFactory connectionFactory() {
        return new H2ConnectionFactory(
                H2ConnectionConfiguration.builder()
                        .url(env.getProperty("spring.r2dbc.url"))
                        .username(env.getProperty("spring.r2dbc.username"))
                        .password(env.getProperty("spring.r2dbc.password"))
                        .build()
        );

    }*/

   /* @Bean
    public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.create(connectionFactory);
    }*/
}
