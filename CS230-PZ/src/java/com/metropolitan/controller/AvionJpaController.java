/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.controller;

import com.metropolitan.controller.exceptions.IllegalOrphanException;
import com.metropolitan.controller.exceptions.NonexistentEntityException;
import com.metropolitan.controller.exceptions.RollbackFailureException;
import com.metropolitan.entity.Avion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.metropolitan.entity.Kompanija;
import com.metropolitan.entity.Let;
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
public class AvionJpaController implements Serializable {

    public AvionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Avion avion) throws RollbackFailureException, Exception {
        if (avion.getLetCollection() == null) {
            avion.setLetCollection(new ArrayList<Let>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Kompanija idKompanije = avion.getIdKompanije();
            if (idKompanije != null) {
                idKompanije = em.getReference(idKompanije.getClass(), idKompanije.getId());
                avion.setIdKompanije(idKompanije);
            }
            Collection<Let> attachedLetCollection = new ArrayList<Let>();
            for (Let letCollectionLetToAttach : avion.getLetCollection()) {
                letCollectionLetToAttach = em.getReference(letCollectionLetToAttach.getClass(), letCollectionLetToAttach.getId());
                attachedLetCollection.add(letCollectionLetToAttach);
            }
            avion.setLetCollection(attachedLetCollection);
            em.persist(avion);
            if (idKompanije != null) {
                idKompanije.getAvionCollection().add(avion);
                idKompanije = em.merge(idKompanije);
            }
            for (Let letCollectionLet : avion.getLetCollection()) {
                Avion oldAvionIdOfLetCollectionLet = letCollectionLet.getAvionId();
                letCollectionLet.setAvionId(avion);
                letCollectionLet = em.merge(letCollectionLet);
                if (oldAvionIdOfLetCollectionLet != null) {
                    oldAvionIdOfLetCollectionLet.getLetCollection().remove(letCollectionLet);
                    oldAvionIdOfLetCollectionLet = em.merge(oldAvionIdOfLetCollectionLet);
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

    public void edit(Avion avion) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Avion persistentAvion = em.find(Avion.class, avion.getId());
            Kompanija idKompanijeOld = persistentAvion.getIdKompanije();
            Kompanija idKompanijeNew = avion.getIdKompanije();
            Collection<Let> letCollectionOld = persistentAvion.getLetCollection();
            Collection<Let> letCollectionNew = avion.getLetCollection();
            List<String> illegalOrphanMessages = null;
            for (Let letCollectionOldLet : letCollectionOld) {
                if (!letCollectionNew.contains(letCollectionOldLet)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Let " + letCollectionOldLet + " since its avionId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idKompanijeNew != null) {
                idKompanijeNew = em.getReference(idKompanijeNew.getClass(), idKompanijeNew.getId());
                avion.setIdKompanije(idKompanijeNew);
            }
            Collection<Let> attachedLetCollectionNew = new ArrayList<Let>();
            for (Let letCollectionNewLetToAttach : letCollectionNew) {
                letCollectionNewLetToAttach = em.getReference(letCollectionNewLetToAttach.getClass(), letCollectionNewLetToAttach.getId());
                attachedLetCollectionNew.add(letCollectionNewLetToAttach);
            }
            letCollectionNew = attachedLetCollectionNew;
            avion.setLetCollection(letCollectionNew);
            avion = em.merge(avion);
            if (idKompanijeOld != null && !idKompanijeOld.equals(idKompanijeNew)) {
                idKompanijeOld.getAvionCollection().remove(avion);
                idKompanijeOld = em.merge(idKompanijeOld);
            }
            if (idKompanijeNew != null && !idKompanijeNew.equals(idKompanijeOld)) {
                idKompanijeNew.getAvionCollection().add(avion);
                idKompanijeNew = em.merge(idKompanijeNew);
            }
            for (Let letCollectionNewLet : letCollectionNew) {
                if (!letCollectionOld.contains(letCollectionNewLet)) {
                    Avion oldAvionIdOfLetCollectionNewLet = letCollectionNewLet.getAvionId();
                    letCollectionNewLet.setAvionId(avion);
                    letCollectionNewLet = em.merge(letCollectionNewLet);
                    if (oldAvionIdOfLetCollectionNewLet != null && !oldAvionIdOfLetCollectionNewLet.equals(avion)) {
                        oldAvionIdOfLetCollectionNewLet.getLetCollection().remove(letCollectionNewLet);
                        oldAvionIdOfLetCollectionNewLet = em.merge(oldAvionIdOfLetCollectionNewLet);
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
                Integer id = avion.getId();
                if (findAvion(id) == null) {
                    throw new NonexistentEntityException("The avion with id " + id + " no longer exists.");
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
            Avion avion;
            try {
                avion = em.getReference(Avion.class, id);
                avion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The avion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Let> letCollectionOrphanCheck = avion.getLetCollection();
            for (Let letCollectionOrphanCheckLet : letCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Avion (" + avion + ") cannot be destroyed since the Let " + letCollectionOrphanCheckLet + " in its letCollection field has a non-nullable avionId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Kompanija idKompanije = avion.getIdKompanije();
            if (idKompanije != null) {
                idKompanije.getAvionCollection().remove(avion);
                idKompanije = em.merge(idKompanije);
            }
            em.remove(avion);
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

    public List<Avion> findAvionEntities() {
        return findAvionEntities(true, -1, -1);
    }

    public List<Avion> findAvionEntities(int maxResults, int firstResult) {
        return findAvionEntities(false, maxResults, firstResult);
    }

    private List<Avion> findAvionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Avion.class));
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

    public Avion findAvion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Avion.class, id);
        } finally {
            em.close();
        }
    }

    public int getAvionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Avion> rt = cq.from(Avion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
