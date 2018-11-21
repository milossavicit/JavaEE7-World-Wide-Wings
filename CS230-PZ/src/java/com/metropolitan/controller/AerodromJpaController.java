/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.controller;

import com.metropolitan.controller.exceptions.IllegalOrphanException;
import com.metropolitan.controller.exceptions.NonexistentEntityException;
import com.metropolitan.controller.exceptions.RollbackFailureException;
import com.metropolitan.entity.Aerodrom;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
public class AerodromJpaController implements Serializable {

    public AerodromJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Aerodrom aerodrom) throws RollbackFailureException, Exception {
        if (aerodrom.getLetCollection() == null) {
            aerodrom.setLetCollection(new ArrayList<Let>());
        }
        if (aerodrom.getLetCollection1() == null) {
            aerodrom.setLetCollection1(new ArrayList<Let>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Let> attachedLetCollection = new ArrayList<Let>();
            for (Let letCollectionLetToAttach : aerodrom.getLetCollection()) {
                letCollectionLetToAttach = em.getReference(letCollectionLetToAttach.getClass(), letCollectionLetToAttach.getId());
                attachedLetCollection.add(letCollectionLetToAttach);
            }
            aerodrom.setLetCollection(attachedLetCollection);
            Collection<Let> attachedLetCollection1 = new ArrayList<Let>();
            for (Let letCollection1LetToAttach : aerodrom.getLetCollection1()) {
                letCollection1LetToAttach = em.getReference(letCollection1LetToAttach.getClass(), letCollection1LetToAttach.getId());
                attachedLetCollection1.add(letCollection1LetToAttach);
            }
            aerodrom.setLetCollection1(attachedLetCollection1);
            em.persist(aerodrom);
            for (Let letCollectionLet : aerodrom.getLetCollection()) {
                Aerodrom oldAerodromIdDolazakOfLetCollectionLet = letCollectionLet.getAerodromIdDolazak();
                letCollectionLet.setAerodromIdDolazak(aerodrom);
                letCollectionLet = em.merge(letCollectionLet);
                if (oldAerodromIdDolazakOfLetCollectionLet != null) {
                    oldAerodromIdDolazakOfLetCollectionLet.getLetCollection().remove(letCollectionLet);
                    oldAerodromIdDolazakOfLetCollectionLet = em.merge(oldAerodromIdDolazakOfLetCollectionLet);
                }
            }
            for (Let letCollection1Let : aerodrom.getLetCollection1()) {
                Aerodrom oldAerodromIdPolazakOfLetCollection1Let = letCollection1Let.getAerodromIdPolazak();
                letCollection1Let.setAerodromIdPolazak(aerodrom);
                letCollection1Let = em.merge(letCollection1Let);
                if (oldAerodromIdPolazakOfLetCollection1Let != null) {
                    oldAerodromIdPolazakOfLetCollection1Let.getLetCollection1().remove(letCollection1Let);
                    oldAerodromIdPolazakOfLetCollection1Let = em.merge(oldAerodromIdPolazakOfLetCollection1Let);
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

    public void edit(Aerodrom aerodrom) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Aerodrom persistentAerodrom = em.find(Aerodrom.class, aerodrom.getId());
            Collection<Let> letCollectionOld = persistentAerodrom.getLetCollection();
            Collection<Let> letCollectionNew = aerodrom.getLetCollection();
            Collection<Let> letCollection1Old = persistentAerodrom.getLetCollection1();
            Collection<Let> letCollection1New = aerodrom.getLetCollection1();
            List<String> illegalOrphanMessages = null;
            for (Let letCollectionOldLet : letCollectionOld) {
                if (!letCollectionNew.contains(letCollectionOldLet)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Let " + letCollectionOldLet + " since its aerodromIdDolazak field is not nullable.");
                }
            }
            for (Let letCollection1OldLet : letCollection1Old) {
                if (!letCollection1New.contains(letCollection1OldLet)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Let " + letCollection1OldLet + " since its aerodromIdPolazak field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Let> attachedLetCollectionNew = new ArrayList<Let>();
            for (Let letCollectionNewLetToAttach : letCollectionNew) {
                letCollectionNewLetToAttach = em.getReference(letCollectionNewLetToAttach.getClass(), letCollectionNewLetToAttach.getId());
                attachedLetCollectionNew.add(letCollectionNewLetToAttach);
            }
            letCollectionNew = attachedLetCollectionNew;
            aerodrom.setLetCollection(letCollectionNew);
            Collection<Let> attachedLetCollection1New = new ArrayList<Let>();
            for (Let letCollection1NewLetToAttach : letCollection1New) {
                letCollection1NewLetToAttach = em.getReference(letCollection1NewLetToAttach.getClass(), letCollection1NewLetToAttach.getId());
                attachedLetCollection1New.add(letCollection1NewLetToAttach);
            }
            letCollection1New = attachedLetCollection1New;
            aerodrom.setLetCollection1(letCollection1New);
            aerodrom = em.merge(aerodrom);
            for (Let letCollectionNewLet : letCollectionNew) {
                if (!letCollectionOld.contains(letCollectionNewLet)) {
                    Aerodrom oldAerodromIdDolazakOfLetCollectionNewLet = letCollectionNewLet.getAerodromIdDolazak();
                    letCollectionNewLet.setAerodromIdDolazak(aerodrom);
                    letCollectionNewLet = em.merge(letCollectionNewLet);
                    if (oldAerodromIdDolazakOfLetCollectionNewLet != null && !oldAerodromIdDolazakOfLetCollectionNewLet.equals(aerodrom)) {
                        oldAerodromIdDolazakOfLetCollectionNewLet.getLetCollection().remove(letCollectionNewLet);
                        oldAerodromIdDolazakOfLetCollectionNewLet = em.merge(oldAerodromIdDolazakOfLetCollectionNewLet);
                    }
                }
            }
            for (Let letCollection1NewLet : letCollection1New) {
                if (!letCollection1Old.contains(letCollection1NewLet)) {
                    Aerodrom oldAerodromIdPolazakOfLetCollection1NewLet = letCollection1NewLet.getAerodromIdPolazak();
                    letCollection1NewLet.setAerodromIdPolazak(aerodrom);
                    letCollection1NewLet = em.merge(letCollection1NewLet);
                    if (oldAerodromIdPolazakOfLetCollection1NewLet != null && !oldAerodromIdPolazakOfLetCollection1NewLet.equals(aerodrom)) {
                        oldAerodromIdPolazakOfLetCollection1NewLet.getLetCollection1().remove(letCollection1NewLet);
                        oldAerodromIdPolazakOfLetCollection1NewLet = em.merge(oldAerodromIdPolazakOfLetCollection1NewLet);
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
                Integer id = aerodrom.getId();
                if (findAerodrom(id) == null) {
                    throw new NonexistentEntityException("The aerodrom with id " + id + " no longer exists.");
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
            Aerodrom aerodrom;
            try {
                aerodrom = em.getReference(Aerodrom.class, id);
                aerodrom.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The aerodrom with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Let> letCollectionOrphanCheck = aerodrom.getLetCollection();
            for (Let letCollectionOrphanCheckLet : letCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Aerodrom (" + aerodrom + ") cannot be destroyed since the Let " + letCollectionOrphanCheckLet + " in its letCollection field has a non-nullable aerodromIdDolazak field.");
            }
            Collection<Let> letCollection1OrphanCheck = aerodrom.getLetCollection1();
            for (Let letCollection1OrphanCheckLet : letCollection1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Aerodrom (" + aerodrom + ") cannot be destroyed since the Let " + letCollection1OrphanCheckLet + " in its letCollection1 field has a non-nullable aerodromIdPolazak field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(aerodrom);
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

    public List<Aerodrom> findAerodromEntities() {
        return findAerodromEntities(true, -1, -1);
    }

    public List<Aerodrom> findAerodromEntities(int maxResults, int firstResult) {
        return findAerodromEntities(false, maxResults, firstResult);
    }

    private List<Aerodrom> findAerodromEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Aerodrom.class));
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

    public Aerodrom findAerodrom(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Aerodrom.class, id);
        } finally {
            em.close();
        }
    }

    public int getAerodromCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Aerodrom> rt = cq.from(Aerodrom.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
