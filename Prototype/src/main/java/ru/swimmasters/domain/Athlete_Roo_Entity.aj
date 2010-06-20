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
import ru.swimmasters.domain.Athlete;

privileged aspect Athlete_Roo_Entity {
    
    @PersistenceContext
    transient EntityManager Athlete.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Athlete.id;
    
    @Version
    @Column(name = "version")
    private Integer Athlete.version;
    
    public Long Athlete.getId() {
        return this.id;
    }
    
    public void Athlete.setId(Long id) {
        this.id = id;
    }
    
    public Integer Athlete.getVersion() {
        return this.version;
    }
    
    public void Athlete.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Athlete.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Athlete.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Athlete attached = this.entityManager.find(this.getClass(), this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Athlete.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public Athlete Athlete.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Athlete merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager Athlete.entityManager() {
        EntityManager em = new Athlete().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Athlete.countAthletes() {
        return ((Number) entityManager().createQuery("select count(o) from Athlete o").getSingleResult()).longValue();
    }
    
    @SuppressWarnings("unchecked")
    public static List<Athlete> Athlete.findAllAthletes() {
        return entityManager().createQuery("select o from Athlete o").getResultList();
    }
    
    public static Athlete Athlete.findAthlete(Long id) {
        if (id == null) return null;
        return entityManager().find(Athlete.class, id);
    }
    
    @SuppressWarnings("unchecked")
    public static List<Athlete> Athlete.findAthleteEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Athlete o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
