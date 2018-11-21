/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.controller;

import com.metropolitan.controller.exceptions.NonexistentEntityException;
import com.metropolitan.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.metropolitan.entity.Aerodrom;
import com.metropolitan.entity.Avion;
import com.metropolitan.entity.Let;
import com.metropolitan.entity.Users;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Milos
 */
public class LetJpaController implements Serializable {

    public LetJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Let let) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Aerodrom aerodromIdDolazak = let.getAerodromIdDolazak();
            if (aerodromIdDolazak != null) {
                aerodromIdDolazak = em.getReference(aerodromIdDolazak.getClass(), aerodromIdDolazak.getId());
                let.setAerodromIdDolazak(aerodromIdDolazak);
            }
            Aerodrom aerodromIdPolazak = let.getAerodromIdPolazak();
            if (aerodromIdPolazak != null) {
                aerodromIdPolazak = em.getReference(aerodromIdPolazak.getClass(), aerodromIdPolazak.getId());
                let.setAerodromIdPolazak(aerodromIdPolazak);
            }
            Avion avionId = let.getAvionId();
            if (avionId != null) {
                avionId = em.getReference(avionId.getClass(), avionId.getId());
                let.setAvionId(avionId);
            }
            Users pilot = let.getPilot();
            if (pilot != null) {
                pilot = em.getReference(pilot.getClass(), pilot.getUsername());
                let.setPilot(pilot);
            }
            em.persist(let);
            if (aerodromIdDolazak != null) {
                aerodromIdDolazak.getLetCollection().add(let);
                aerodromIdDolazak = em.merge(aerodromIdDolazak);
            }
            if (aerodromIdPolazak != null) {
                aerodromIdPolazak.getLetCollection().add(let);
                aerodromIdPolazak = em.merge(aerodromIdPolazak);
            }
            if (avionId != null) {
                avionId.getLetCollection().add(let);
                avionId = em.merge(avionId);
            }
            if (pilot != null) {
                pilot.getLetCollection().add(let);
                pilot = em.merge(pilot);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Let let) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Let persistentLet = em.find(Let.class, let.getId());
            Aerodrom aerodromIdDolazakOld = persistentLet.getAerodromIdDolazak();
            Aerodrom aerodromIdDolazakNew = let.getAerodromIdDolazak();
            Aerodrom aerodromIdPolazakOld = persistentLet.getAerodromIdPolazak();
            Aerodrom aerodromIdPolazakNew = let.getAerodromIdPolazak();
            Avion avionIdOld = persistentLet.getAvionId();
            Avion avionIdNew = let.getAvionId();
            Users pilotOld = persistentLet.getPilot();
            Users pilotNew = let.getPilot();
            if (aerodromIdDolazakNew != null) {
                aerodromIdDolazakNew = em.getReference(aerodromIdDolazakNew.getClass(), aerodromIdDolazakNew.getId());
                let.setAerodromIdDolazak(aerodromIdDolazakNew);
            }
            if (aerodromIdPolazakNew != null) {
                aerodromIdPolazakNew = em.getReference(aerodromIdPolazakNew.getClass(), aerodromIdPolazakNew.getId());
                let.setAerodromIdPolazak(aerodromIdPolazakNew);
            }
            if (avionIdNew != null) {
                avionIdNew = em.getReference(avionIdNew.getClass(), avionIdNew.getId());
                let.setAvionId(avionIdNew);
            }
            if (pilotNew != null) {
                pilotNew = em.getReference(pilotNew.getClass(), pilotNew.getUsername());
                let.setPilot(pilotNew);
            }
            let = em.merge(let);
            if (aerodromIdDolazakOld != null && !aerodromIdDolazakOld.equals(aerodromIdDolazakNew)) {
                aerodromIdDolazakOld.getLetCollection().remove(let);
                aerodromIdDolazakOld = em.merge(aerodromIdDolazakOld);
            }
            if (aerodromIdDolazakNew != null && !aerodromIdDolazakNew.equals(aerodromIdDolazakOld)) {
                aerodromIdDolazakNew.getLetCollection().add(let);
                aerodromIdDolazakNew = em.merge(aerodromIdDolazakNew);
            }
            if (aerodromIdPolazakOld != null && !aerodromIdPolazakOld.equals(aerodromIdPolazakNew)) {
                aerodromIdPolazakOld.getLetCollection().remove(let);
                aerodromIdPolazakOld = em.merge(aerodromIdPolazakOld);
            }
            if (aerodromIdPolazakNew != null && !aerodromIdPolazakNew.equals(aerodromIdPolazakOld)) {
                aerodromIdPolazakNew.getLetCollection().add(let);
                aerodromIdPolazakNew = em.merge(aerodromIdPolazakNew);
            }
            if (avionIdOld != null && !avionIdOld.equals(avionIdNew)) {
                avionIdOld.getLetCollection().remove(let);
                avionIdOld = em.merge(avionIdOld);
            }
            if (avionIdNew != null && !avionIdNew.equals(avionIdOld)) {
                avionIdNew.getLetCollection().add(let);
                avionIdNew = em.merge(avionIdNew);
            }
            if (pilotOld != null && !pilotOld.equals(pilotNew)) {
                pilotOld.getLetCollection().remove(let);
                pilotOld = em.merge(pilotOld);
            }
            if (pilotNew != null && !pilotNew.equals(pilotOld)) {
                pilotNew.getLetCollection().add(let);
                pilotNew = em.merge(pilotNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = let.getId();
                if (findLet(id) == null) {
                    throw new NonexistentEntityException("The let with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Let let;
            try {
                let = em.getReference(Let.class, id);
                let.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The let with id " + id + " no longer exists.", enfe);
            }
            Aerodrom aerodromIdDolazak = let.getAerodromIdDolazak();
            if (aerodromIdDolazak != null) {
                aerodromIdDolazak.getLetCollection().remove(let);
                aerodromIdDolazak = em.merge(aerodromIdDolazak);
            }
            Aerodrom aerodromIdPolazak = let.getAerodromIdPolazak();
            if (aerodromIdPolazak != null) {
                aerodromIdPolazak.getLetCollection().remove(let);
                aerodromIdPolazak = em.merge(aerodromIdPolazak);
            }
            Avion avionId = let.getAvionId();
            if (avionId != null) {
                avionId.getLetCollection().remove(let);
                avionId = em.merge(avionId);
            }
            Users pilot = let.getPilot();
            if (pilot != null) {
                pilot.getLetCollection().remove(let);
                pilot = em.merge(pilot);
            }
            em.remove(let);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Let> findLetEntities() {
        return findLetEntities(true, -1, -1);
    }

    public List<Let> findLetEntities(int maxResults, int firstResult) {
        return findLetEntities(false, maxResults, firstResult);
    }

    private List<Let> findLetEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Let.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Let findLet(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Let.class, id);
        } finally {
            em.close();
        }
    }

    public int getLetCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Let> rt = cq.from(Let.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
