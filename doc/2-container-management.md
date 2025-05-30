# Comment éviter d'arrêter/redémarrer les conteneurs à chaque arrêt/relance du serveur spring boot

Pour éviter que les conteneurs **Testcontainers** ou **Spring Boot Docker Compose** se redémarrent à chaque fois que tu redémarres ton application Spring Boot (typiquement pendant le développement), tu peux **garder les conteneurs "chauds"** entre les cycles de lancement **en suivant les stratégies suivantes :**

---

## ✅ **1. Utiliser des conteneurs réutilisables avec Testcontainers**

Depuis Testcontainers v1.17, tu peux marquer les conteneurs comme **réutilisables**, pour éviter leur arrêt/démarrage à chaque test ou lancement.

### 🔧 Étapes :

#### a. Active la réutilisation dans `~/.testcontainers.properties` :

Crée (ou édite) ce fichier :

```
~/.testcontainers.properties
```

Et ajoute :

```properties
testcontainers.reuse.enable=true
```

#### b. Marque tes conteneurs comme réutilisables :

```java
PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
    .withDatabaseName("test")
    .withUsername("test")
    .withPassword("test")
    .withReuse(true);
```

> ✅ Résultat : le conteneur reste actif après la fin du test ou du contexte Spring, et sera réutilisé aux lancements suivants (tant que Docker tourne).

---

## ✅ **2. Utiliser `spring-boot-docker-compose` uniquement pour init / dev**

Si tu utilises `spring-boot-docker-compose` (Spring Boot 3+), **les conteneurs démarrent avec l'application** et s'arrêtent à la fin **par défaut**.

### 🔄 Solution : Détacher leur cycle de vie de Spring Boot

**Option 1 : Lancer `docker-compose` manuellement (hors Spring Boot)**

Au lieu de laisser Spring Boot lancer/arrêter les conteneurs à chaque run, **lance-les une fois manuellement** :

```bash
docker compose -f src/main/docker/docker-compose.yml up -d
```

Et désactive `spring-boot-docker-compose` dans l’environnement :

```yaml
spring:
  docker:
    compose:
      enabled: false
```

➡️ Tu gardes tes conteneurs démarrés en tâche de fond, et Spring Boot les utilise sans gérer leur cycle de vie.

**Option 2 : Activer `lifecycleManagement: start-only` (Spring Boot 3.3+)**

Avec Spring Boot 3.3+, tu peux faire :

```yaml
spring:
  docker:
    compose:
      lifecycle-management: start-only
```

> 🚀 Cela signifie que Spring Boot **ne les arrêtera plus automatiquement** à la fin.

---

## ✅ Résumé des meilleures pratiques

| Outil                          | Solution recommandée                                                              |
| ------------------------------ | --------------------------------------------------------------------------------- |
| **Testcontainers**             | `.withReuse(true)` + `testcontainers.reuse.enable=true`                           |
| **Spring Boot Docker Compose** | `lifecycle-management: start-only` ou `enabled: false` + docker-compose à la main |
| **Développement local**        | Utiliser `docker-compose up -d` + profils de config                               |

---

Souhaites-tu un exemple combiné `Testcontainers + Spring Boot Docker Compose` optimisé pour le dev ?
