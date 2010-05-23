package org.olympic.swimming.domain;

import java.lang.Integer;
import java.lang.Long;
import java.lang.SuppressWarnings;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.olympic.swimming.domain.Pool;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Pool_Roo_Entity {
    
    @PersistenceContext
    transient EntityManager Pool.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Pool.id;
    
    @Version
    @Column(name = "version")
    private Integer Pool.version;
    
    public Long Pool.getId() {
        return this.id;
    }
    
    public void Pool.setId(Long id) {
        this.id = id;
    }
    
    public Integer Pool.getVersion() {
        return this.version;
    }
    
    public void Pool.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Pool.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Pool.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Pool attached = this.entityManager.find(this.getClass(), this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Pool.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public Pool Pool.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Pool merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager Pool.entityManager() {
        EntityManager em = new Pool().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Pool.countPools() {
        return ((Number) entityManager().createQuery("select count(o) from Pool o").getSingleResult()).longValue();
    }
    
    @SuppressWarnings("unchecked")
    public static List<Pool> Pool.findAllPools() {
        return entityManager().createQuery("select o from Pool o").getResultList();
    }
    
    public static Pool Pool.findPool(Long id) {
        if (id == null) return null;
        return entityManager().find(Pool.class, id);
    }
    
    @SuppressWarnings("unchecked")
    public static List<Pool> Pool.findPoolEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Pool o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
