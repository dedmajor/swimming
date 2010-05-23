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
import org.olympic.swimming.domain.Swimmer;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Swimmer_Roo_Entity {
    
    @PersistenceContext
    transient EntityManager Swimmer.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Swimmer.id;
    
    @Version
    @Column(name = "version")
    private Integer Swimmer.version;
    
    public Long Swimmer.getId() {
        return this.id;
    }
    
    public void Swimmer.setId(Long id) {
        this.id = id;
    }
    
    public Integer Swimmer.getVersion() {
        return this.version;
    }
    
    public void Swimmer.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Swimmer.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Swimmer.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Swimmer attached = this.entityManager.find(this.getClass(), this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Swimmer.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public Swimmer Swimmer.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Swimmer merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager Swimmer.entityManager() {
        EntityManager em = new Swimmer().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Swimmer.countSwimmers() {
        return ((Number) entityManager().createQuery("select count(o) from Swimmer o").getSingleResult()).longValue();
    }
    
    @SuppressWarnings("unchecked")
    public static List<Swimmer> Swimmer.findAllSwimmers() {
        return entityManager().createQuery("select o from Swimmer o").getResultList();
    }
    
    public static Swimmer Swimmer.findSwimmer(Long id) {
        if (id == null) return null;
        return entityManager().find(Swimmer.class, id);
    }
    
    @SuppressWarnings("unchecked")
    public static List<Swimmer> Swimmer.findSwimmerEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Swimmer o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
