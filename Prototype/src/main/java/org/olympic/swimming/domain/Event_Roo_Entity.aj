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
import org.olympic.swimming.domain.Event;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Event_Roo_Entity {
    
    @PersistenceContext
    transient EntityManager Event.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Event.id;
    
    @Version
    @Column(name = "version")
    private Integer Event.version;
    
    public Long Event.getId() {
        return this.id;
    }
    
    public void Event.setId(Long id) {
        this.id = id;
    }
    
    public Integer Event.getVersion() {
        return this.version;
    }
    
    public void Event.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Event.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Event.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Event attached = this.entityManager.find(this.getClass(), this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Event.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public Event Event.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Event merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager Event.entityManager() {
        EntityManager em = new Event().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Event.countEvents() {
        return ((Number) entityManager().createQuery("select count(o) from Event o").getSingleResult()).longValue();
    }
    
    @SuppressWarnings("unchecked")
    public static List<Event> Event.findAllEvents() {
        return entityManager().createQuery("select o from Event o").getResultList();
    }
    
    public static Event Event.findEvent(Long id) {
        if (id == null) return null;
        return entityManager().find(Event.class, id);
    }
    
    @SuppressWarnings("unchecked")
    public static List<Event> Event.findEventEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Event o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
