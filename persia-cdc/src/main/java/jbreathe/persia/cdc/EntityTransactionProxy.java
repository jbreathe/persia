package jbreathe.persia.cdc;

import javax.persistence.EntityTransaction;
import java.util.List;

public final class EntityTransactionProxy implements EntityTransaction {
    private final EntityTransaction delegate;
    private final List<PersistenceEventListener> eventListeners;

    public EntityTransactionProxy(EntityTransaction delegate, List<PersistenceEventListener> eventListeners) {
        this.delegate = delegate;
        this.eventListeners = eventListeners;
    }

    @Override
    public void begin() {
        eventListeners.forEach(PersistenceEventListener::onBegin);
        delegate.begin();
    }

    @Override
    public void commit() {
        eventListeners.forEach(PersistenceEventListener::onCommit);
        delegate.commit();
    }

    @Override
    public void rollback() {
        eventListeners.forEach(PersistenceEventListener::onRollback);
        delegate.rollback();
    }

    @Override
    public void setRollbackOnly() {
        delegate.setRollbackOnly();
    }

    @Override
    public boolean getRollbackOnly() {
        return delegate.getRollbackOnly();
    }

    @Override
    public boolean isActive() {
        return delegate.isActive();
    }
}
