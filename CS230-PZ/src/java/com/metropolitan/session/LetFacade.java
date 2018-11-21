/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.session;

import com.metropolitan.entity.Let;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Milos
 */
@Stateless
public class LetFacade extends AbstractFacade<Let> {

    @PersistenceContext(unitName = "CS230-PZPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LetFacade() {
        super(Let.class);
    }
    
}
