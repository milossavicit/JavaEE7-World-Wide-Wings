/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.session;

import com.metropolitan.entity.Avion;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Milos
 */
@Stateless
public class AvionFacade extends AbstractFacade<Avion> {

    @PersistenceContext(unitName = "CS230-PZPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AvionFacade() {
        super(Avion.class);
    }
    
}
