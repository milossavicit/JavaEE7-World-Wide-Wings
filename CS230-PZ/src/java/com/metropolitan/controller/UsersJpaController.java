/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.controller;

import com.metropolitan.controller.exceptions.NonexistentEntityException;
import com.metropolitan.controller.exceptions.PreexistingEntityException;
import com.metropolitan.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.metropolitan.entity.Let;
import com.metropolitan.entity.Users;
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
public class UsersJpaController implements Serializable {

    public UsersJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (users.getLetCollection() == null) {
            users.setLetCollection(new ArrayList<Let>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Let> attachedLetCollection = new ArrayList<Let>();
            for (Let letCollectionLetToAttach : users.getLetCollection()) {
                letCollectionLetToAttach = em.getReference(letCollectionLetToAttach.getClass(), letCollectionLetToAttach.getId());
                attachedLetCollection.add(letCollectionLetToAttach);
            }
            users.setLetCollection(attachedLetCollection);
            em.persist(users);
            for (Let letCollectionLet : users.getLetCollection()) {
                Users oldPilotOfLetCollectionLet = letCollectionLet.getPilot();
                letCollectionLet.setPilot(users);
                letCollectionLet = em.merge(letCollectionLet);
                if (oldPilotOfLetCollectionLet != null) {
                    oldPilotOfLetCollectionLet.getLetCollection().remove(letCollectionLet);
                    oldPilotOfLetCollectionLet = em.merge(oldPilotOfLetCollectionLet);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUsers(users.getUsername()) != null) {
                throw new PreexistingEntityException("Users " + users + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Users persistentUsers = em.find(Users.class, users.getUsername());
            Collection<Let> letCollectionOld = persistentUsers.getLetCollection();
            Collection<Let> letCollectionNew = users.getLetCollection();
            Collection<Let> attachedLetCollectionNew = new ArrayList<Let>();
            for (Let letCollectionNewLetToAttach : letCollectionNew) {
                letCollectionNewLetToAttach = em.getReference(letCollectionNewLetToAttach.getClass(), letCollectionNewLetToAttach.getId());
                attachedLetCollectionNew.add(letCollectionNewLetToAttach);
            }
            letCollectionNew = attachedLetCollectionNew;
            users.setLetCollection(letCollectionNew);
            users = em.merge(users);
            for (Let letCollectionOldLet : letCollectionOld) {
                if (!letCollectionNew.contains(letCollectionOldLet)) {
                    letCollectionOldLet.setPilot(null);
                    letCollectionOldLet = em.merge(letCollectionOldLet);
                }
            }
            for (Let letCollectionNewLet : letCollectionNew) {
                if (!letCollectionOld.contains(letCollectionNewLet)) {
                    Users oldPilotOfLetCollectionNewLet = letCollectionNewLet.getPilot();
                    letCollectionNewLet.setPilot(users);
                    letCollectionNewLet = em.merge(letCollectionNewLet);
                    if (oldPilotOfLetCollectionNewLet != null && !oldPilotOfLetCollectionNewLet.equals(users)) {
                        oldPilotOfLetCollectionNewLet.getLetCollection().remove(letCollectionNewLet);
                        oldPilotOfLetCollectionNewLet = em.merge(oldPilotOfLetCollectionNewLet);
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
                String id = users.getUsername();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getUsername();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            Collection<Let> letCollection = users.getLetCollection();
            for (Let letCollectionLet : letCollection) {
                letCollectionLet.setPilot(null);
                letCollectionLet = em.merge(letCollectionLet);
            }
            em.remove(users);
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

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
