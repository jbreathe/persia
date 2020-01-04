package jbreathe.persia.cdc;

import javax.persistence.Cache;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class EntityManagerFactoryProxy implements EntityManagerFactory {
    private final EntityManagerFactory delegate;
    private final List<Supplier<PersistenceEventListener>> listenerInitializers;

    public EntityManagerFactoryProxy(EntityManagerFactory delegate,
                                     List<Supplier<PersistenceEventListener>> listenerInitializers) {
        this.delegate = delegate;
        this.listenerInitializers = listenerInitializers;
    }

    @Override
    public EntityManager createEntityManager() {
        EntityManager entityManager = delegate.createEntityManager();
        return createProxy(entityManager);
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        EntityManager entityManager = delegate.createEntityManager(map);
        return createProxy(entityManager);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        EntityManager entityManager = delegate.createEntityManager(synchronizationType);
        return createProxy(entityManager);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
        EntityManager entityManager = delegate.createEntityManager(synchronizationType, map);
        return createProxy(entityManager);
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return delegate.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return delegate.getMetamodel();
    }

    @Override
    public boolean isOpen() {
        return delegate.isOpen();
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public Map<String, Object> getProperties() {
        return delegate.getProperties();
    }

    @Override
    public Cache getCache() {
        return delegate.getCache();
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        return delegate.getPersistenceUnitUtil();
    }

    @Override
    public void addNamedQuery(String name, Query query) {
        delegate.addNamedQuery(name, query);
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        return delegate.unwrap(cls);
    }

    @Override
    public <T> void addNamedEntityGraph(String graphName, EntityGraph<T> entityGraph) {
        delegate.addNamedEntityGraph(graphName, entityGraph);
    }

    private EntityManager createProxy(EntityManager entityManager) {
        List<PersistenceEventListener> eventListeners = initEventListeners();
        return new EntityManagerProxy(entityManager, eventListeners);
    }

    // lazy initialization of listeners
    private List<PersistenceEventListener> initEventListeners() {
        return listenerInitializers.stream()
                .map(Supplier::get)
                .collect(Collectors.toList());
    }
}
