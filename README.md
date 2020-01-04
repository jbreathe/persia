# Persia

This project consists of a few subprojects.

## CDC

Small library to achieve the same functionality as Hibernate Interceptors, but in more portable way. 
It is not provider dependent, so you can use it with any JPA provider.

### Usage example

```java
class Test {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = EntityManagerFactoryProxy.around(delegate)
                .addListenerInitializer(MyPersistenceEventListener::new)
                .build();
    }
}
```
where MyPersistenceEventListener implements PersistenceEventListener.

## Ext

Extensions for standard JPA API such as simple implementation of EntityManagerFactory.
