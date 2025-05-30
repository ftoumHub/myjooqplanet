package com.mydeveloperplanet.myjooqplanet.config;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JooqConfig {

    @Bean
    public DSLContext dslContext(DataSource ds, Settings settings) {
        return DSL.using(ds, SQLDialect.POSTGRES, settings);
    }

    @Bean
    public Settings jooqSettings() {
        return new Settings()
                .withRenderFormatted(true);  // Affiche les requêtes formatées
    }
}
