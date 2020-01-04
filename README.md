# Persia

This project consists of a few subprojects.

## CDC

Small library to achieve the same functionality as Hibernate Interceptors, but in more portable way. 
It is not provider dependent, so you can use it with any JPA provider.

### Usage example

If you have instance of EntityManagerFactory, then you can use CDC feature like this:
```java
class Test {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = EntityManagerFactoryProxy.around(delegate)
                .addPersistentListenerInitializer(MyPersistenceEventListener::new)
                .build();
    }
}
```
where MyPersistenceEventListener implements PersistenceEventListener.

Every time EntityManagerFactory call "createEntityManager" methods,
PersistenceEventListener implementation will be initialized
and initialized instance will receive persistence events like "onPersist", "onMerge", "onRemove".

If you have instance of EntityManager, then you can use CDC feature like this:
```java
class Test {
    public static void main(String[] args) {
        EntityManager entityManager = EntityManagerProxy.around(delegate)
                .addPersistenceListener(new MyPersistenceEventListener())
                .build();
    }
}
```

## Ext

Extensions for standard JPA API such as simple implementation of EntityManagerFactory.
