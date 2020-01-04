package jbreathe.persia.ext;

import javax.persistence.Cache;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolver;
import javax.persistence.spi.PersistenceProviderResolverHolder;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

// "local" or "resource-local" (or so-called "non-JTA") and "container" (or "managed"),
// because it can be used in container environment with @PersistenceContext annotation.
public class ManagedNonJtaEntityManagerFactory implements EntityManagerFactory {
    private final EntityManagerFactory delegate;

    /*
      emf.setDataSource(dataSource); // required
      emf.setPersistenceUnitName("myUnit"); // required or optional with default value = "entityManagerFactory"?
      emf.setPackagesToScan(new String[] { "my.model" }); // optional
      emf.setJpaProperties(additionalProperties); // optional
     */

    public ManagedNonJtaEntityManagerFactory(DataSource dataSource) {
        this("entityManagerFactory", dataSource);
    }

    public ManagedNonJtaEntityManagerFactory(String persistenceUnitName, DataSource dataSource) {
        assert persistenceUnitName != null;
        assert dataSource != null;
        PersistenceProviderResolver resolver = PersistenceProviderResolverHolder.getPersistenceProviderResolver();
        List<PersistenceProvider> providers = resolver.getPersistenceProviders();
        EntityManagerFactory emf = null;
        for (PersistenceProvider provider : providers) {
            SimplePersistenceUnitInfo persistenceUnitInfo = new SimplePersistenceUnitInfo();
            persistenceUnitInfo.setPersistenceUnitName(persistenceUnitName);
            persistenceUnitInfo.setNonJtaDataSource(dataSource);
            emf = provider.createContainerEntityManagerFactory(persistenceUnitInfo, null);
            if (emf != null) {
                break;
            }
        }
        if (emf == null) {
            throw new PersistenceException("No Persistence provider for EntityManager named " + persistenceUnitName);
        }
        this.delegate = emf;
    }

    @Override
    public EntityManager createEntityManager() {
        return delegate.createEntityManager();
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        return delegate.createEntityManager(map);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        return delegate.createEntityManager(synchronizationType);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
        return delegate.createEntityManager(synchronizationType, map);
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
}
