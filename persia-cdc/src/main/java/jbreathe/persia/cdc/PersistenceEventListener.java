package jbreathe.persia.cdc;

public interface PersistenceEventListener {
    // tx
    void onBegin();

    void onCommit();

    void onRollback();

    // em
    void onPersist(Object entity); // before persist

    void onMerge(Object before, Object after);

    void onRemove(Object entity); // before remove
}
