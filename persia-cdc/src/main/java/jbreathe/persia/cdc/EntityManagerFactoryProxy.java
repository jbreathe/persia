package jbreathe.persia.cdc;

import javax.annotation.concurrent.NotThreadSafe;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@NotThreadSafe
public final class EntityManagerFactoryProxy {
    private final EntityManagerFactory delegate;
    private final List<Supplier<PersistenceEventListener>> listenerInitializers = new ArrayList<>();

    private EntityManagerFactoryProxy(EntityManagerFactory delegate) {
        this.delegate = delegate;
    }

    public static EntityManagerFactoryProxy around(EntityManagerFactory delegate) {
        return new EntityManagerFactoryProxy(delegate);
    }

    public EntityManagerFactoryProxy addListenerInitializer(Supplier<PersistenceEventListener> listenerInitializer) {
        this.listenerInitializers.add(listenerInitializer);
        return this;
    }

    public EntityManagerFactory build() {
        return new EventableEntityManagerFactoryProxy(delegate, listenerInitializers);
    }
}
