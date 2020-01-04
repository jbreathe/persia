package jbreathe.persia.cdc;

import javax.annotation.concurrent.NotThreadSafe;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@NotThreadSafe
public final class EntityManagerProxy {
    private final EntityManager delegate;
    private final List<PersistenceEventListener> listeners = new ArrayList<>();

    private EntityManagerProxy(EntityManager delegate) {
        this.delegate = delegate;
    }

    public static EntityManagerProxy around(EntityManager delegate) {
        return new EntityManagerProxy(delegate);
    }

    public EntityManagerProxy addListener(PersistenceEventListener listener) {
        this.listeners.add(listener);
        return this;
    }

    public EntityManager build() {
        return new EventableEntityManagerProxy(delegate, listeners);
    }
}
