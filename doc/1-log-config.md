# Dans une application spring boot, comment afficher les requêtes sql jouées par jooq?

Pour **afficher les requêtes SQL exécutées par jOOQ** dans une application **Spring Boot**, tu peux utiliser l’un ou plusieurs des mécanismes suivants :

---

### ✅ **Méthode recommandée : Configurer le `ExecuteListener` de jOOQ**

Ajoute un `ExecuteListener` dans ta configuration jOOQ pour logger chaque requête.

#### Exemple avec Spring Boot (Java config) :

```java
@Configuration
public class JooqConfig {

    @Bean
    public DefaultDSLContext dslContext(Configuration configuration) {
        return new DefaultDSLContext(configuration);
    }

    @Bean
    public Configuration jooqConfiguration(DataSource dataSource) {
        return new DefaultConfiguration()
                .derive(new DefaultConnectionProvider(dataSource))
                .derive(SQLDialect.POSTGRES) // ou autre dialecte
                .derive(new Settings().withExecuteLogging(true))
                .set(new JooqSqlLogger());
    }

    public static class JooqSqlLogger extends DefaultExecuteListener {
        private static final Logger logger = LoggerFactory.getLogger(JooqSqlLogger.class);

        @Override
        public void renderEnd(ExecuteContext ctx) {
            logger.info("jOOQ SQL: {}", ctx.sql());
        }
    }
}
```

---

### ✅ **Activer la journalisation jOOQ via SLF4J**

jOOQ utilise SLF4J pour logguer. Assure-toi que le niveau `DEBUG` est activé pour `org.jooq`.

#### Exemple dans `application.yml` :

```yaml
logging:
  level:
    org.jooq.tools.LoggerListener: DEBUG
    org.jooq: DEBUG
```

#### Et ajoute `LoggerListener` dans la config jOOQ :

```java
.configuration()
.set(new LoggerListener());
```

---

### ✅ Alternative simple (Spring Boot + jOOQ starter)

Si tu utilises `spring-boot-starter-jooq`, tu peux simplement ajouter le `LoggerListener` comme bean :

```java
@Bean
public ExecuteListenerProvider executeListenerProvider() {
    return new DefaultExecuteListenerProvider(new LoggerListener());
}
```

Et tu verras des logs comme :

```
[main] DEBUG org.jooq.tools.LoggerListener - Executing query          : insert into customer (first_name, ...) values (?, ?)
[main] DEBUG org.jooq.tools.LoggerListener - -> with bind values      : insert into customer (first_name, ...) values ('John', 'Doe')
```

---

### ✅ Résumé des options

| Méthode                    | Niveau de contrôle | Nécessite config ?                    | Affiche SQL avec valeurs ?    |
| -------------------------- | ------------------ | ------------------------------------- | ----------------------------- |
| `LoggerListener`           | Facile             | Peu                                   | Oui                           |
| Custom `ExecuteListener`   | Avancé             | Oui                                   | Oui                           |
| Spring Boot logging config | Simple             | Non (si `LoggerListener` est présent) | Non, sauf si `LoggerListener` |

---

Souhaites-tu une version Kotlin ou XML config de cette configuration ?
