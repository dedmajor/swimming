package ru.swimmasters.domain;

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
import org.springframework.transaction.annotation.Transactional;
import ru.swimmasters.domain.Meet;

privileged aspect Meet_Roo_Entity {
    
    @PersistenceContext
    transient EntityManager Meet.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Meet.id;
    
    @Version
    @Column(name = "version")
    private Integer Meet.version;
    
    public Long Meet.getId() {
        return this.id;
    }
    
    public void Meet.setId(Long id) {
        this.id = id;
    }
    
    public Integer Meet.getVersion() {
        return this.version;
    }
    
    public void Meet.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Meet.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Meet.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Meet attached = this.entityManager.find(this.getClass(), this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Meet.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public Meet Meet.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Meet merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager Meet.entityManager() {
        EntityManager em = new Meet().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Meet.countMeets() {
        return ((Number) entityManager().createQuery("select count(o) from Meet o").getSingleResult()).longValue();
    }
    
    @SuppressWarnings("unchecked")
    public static List<Meet> Meet.findAllMeets() {
        return entityManager().createQuery("select o from Meet o").getResultList();
    }
    
    public static Meet Meet.findMeet(Long id) {
        if (id == null) return null;
        return entityManager().find(Meet.class, id);
    }
    
    @SuppressWarnings("unchecked")
    public static List<Meet> Meet.findMeetEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Meet o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
