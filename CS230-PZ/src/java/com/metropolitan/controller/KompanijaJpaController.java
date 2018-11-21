/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.controller;

import com.metropolitan.controller.exceptions.IllegalOrphanException;
import com.metropolitan.controller.exceptions.NonexistentEntityException;
import com.metropolitan.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.metropolitan.entity.Avion;
import com.metropolitan.entity.Kompanija;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Milos
 */
public class KompanijaJpaController implements Serializable {

    public KompanijaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Kompanija kompanija) throws RollbackFailureException, Exception {
        if (kompanija.getAvionCollection() == null) {
            kompanija.setAvionCollection(new ArrayList<Avion>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Avion> attachedAvionCollection = new ArrayList<Avion>();
            for (Avion avionCollectionAvionToAttach : kompanija.getAvionCollection()) {
                avionCollectionAvionToAttach = em.getReference(avionCollectionAvionToAttach.getClass(), avionCollectionAvionToAttach.getId());
                attachedAvionCollection.add(avionCollectionAvionToAttach);
            }
            kompanija.setAvionCollection(attachedAvionCollection);
            em.persist(kompanija);
            for (Avion avionCollectionAvion : kompanija.getAvionCollection()) {
                Kompanija oldIdKompanijeOfAvionCollectionAvion = avionCollectionAvion.getIdKompanije();
                avionCollectionAvion.setIdKompanije(kompanija);
                avionCollectionAvion = em.merge(avionCollectionAvion);
                if (oldIdKompanijeOfAvionCollectionAvion != null) {
                    oldIdKompanijeOfAvionCollectionAvion.getAvionCollection().remove(avionCollectionAvion);
                    oldIdKompanijeOfAvionCollectionAvion = em.merge(oldIdKompanijeOfAvionCollectionAvion);
                }
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

    public void edit(Kompanija kompanija) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Kompanija persistentKompanija = em.find(Kompanija.class, kompanija.getId());
            Collection<Avion> avionCollectionOld = persistentKompanija.getAvionCollection();
            Collection<Avion> avionCollectionNew = kompanija.getAvionCollection();
            List<String> illegalOrphanMessages = null;
            for (Avion avionCollectionOldAvion : avionCollectionOld) {
                if (!avionCollectionNew.contains(avionCollectionOldAvion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Avion " + avionCollectionOldAvion + " since its idKompanije field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Avion> attachedAvionCollectionNew = new ArrayList<Avion>();
            for (Avion avionCollectionNewAvionToAttach : avionCollectionNew) {
                avionCollectionNewAvionToAttach = em.getReference(avionCollectionNewAvionToAttach.getClass(), avionCollectionNewAvionToAttach.getId());
                attachedAvionCollectionNew.add(avionCollectionNewAvionToAttach);
            }
            avionCollectionNew = attachedAvionCollectionNew;
            kompanija.setAvionCollection(avionCollectionNew);
            kompanija = em.merge(kompanija);
            for (Avion avionCollectionNewAvion : avionCollectionNew) {
                if (!avionCollectionOld.contains(avionCollectionNewAvion)) {
                    Kompanija oldIdKompanijeOfAvionCollectionNewAvion = avionCollectionNewAvion.getIdKompanije();
                    avionCollectionNewAvion.setIdKompanije(kompanija);
                    avionCollectionNewAvion = em.merge(avionCollectionNewAvion);
                    if (oldIdKompanijeOfAvionCollectionNewAvion != null && !oldIdKompanijeOfAvionCollectionNewAvion.equals(kompanija)) {
                        oldIdKompanijeOfAvionCollectionNewAvion.getAvionCollection().remove(avionCollectionNewAvion);
                        oldIdKompanijeOfAvionCollectionNewAvion = em.merge(oldIdKompanijeOfAvionCollectionNewAvion);
                    }
                }
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
                Integer id = kompanija.getId();
                if (findKompanija(id) == null) {
                    throw new NonexistentEntityException("The kompanija with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Kompanija kompanija;
            try {
                kompanija = em.getReference(Kompanija.class, id);
                kompanija.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The kompanija with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Avion> avionCollectionOrphanCheck = kompanija.getAvionCollection();
            for (Avion avionCollectionOrphanCheckAvion : avionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Kompanija (" + kompanija + ") cannot be destroyed since the Avion " + avionCollectionOrphanCheckAvion + " in its avionCollection field has a non-nullable idKompanije field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(kompanija);
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

    public List<Kompanija> findKompanijaEntities() {
        return findKompanijaEntities(true, -1, -1);
    }

    public List<Kompanija> findKompanijaEntities(int maxResults, int firstResult) {
        return findKompanijaEntities(false, maxResults, firstResult);
    }

    private List<Kompanija> findKompanijaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Kompanija.class));
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

    public Kompanija findKompanija(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Kompanija.class, id);
        } finally {
            em.close();
        }
    }

    public int getKompanijaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Kompanija> rt = cq.from(Kompanija.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
