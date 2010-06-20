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
import ru.swimmasters.domain.Discipline;

privileged aspect Discipline_Roo_Entity {
    
    @PersistenceContext
    transient EntityManager Discipline.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Discipline.id;
    
    @Version
    @Column(name = "version")
    private Integer Discipline.version;
    
    public Long Discipline.getId() {
        return this.id;
    }
    
    public void Discipline.setId(Long id) {
        this.id = id;
    }
    
    public Integer Discipline.getVersion() {
        return this.version;
    }
    
    public void Discipline.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Discipline.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Discipline.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Discipline attached = this.entityManager.find(this.getClass(), this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Discipline.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public Discipline Discipline.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Discipline merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager Discipline.entityManager() {
        EntityManager em = new Discipline().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Discipline.countDisciplines() {
        return ((Number) entityManager().createQuery("select count(o) from Discipline o").getSingleResult()).longValue();
    }
    
    @SuppressWarnings("unchecked")
    public static List<Discipline> Discipline.findAllDisciplines() {
        return entityManager().createQuery("select o from Discipline o").getResultList();
    }
    
    public static Discipline Discipline.findDiscipline(Long id) {
        if (id == null) return null;
        return entityManager().find(Discipline.class, id);
    }
    
    @SuppressWarnings("unchecked")
    public static List<Discipline> Discipline.findDisciplineEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Discipline o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
