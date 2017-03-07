/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Plat;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author walid
 */
@Stateless
public class PlatFacade extends AbstractFacade<Plat> {

    @PersistenceContext(unitName = "com.mycompany_FoodOnline_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PlatFacade() {
        super(Plat.class);
    }
    
    private void clone(Plat platSource, Plat platDestination){
        platDestination.setId(platSource.getId());
        platDestination.setPrix(platSource.getPrix());
        platDestination.setType(platSource.getType());
    }

    public Plat clone(Plat plat){
        Plat cloned = new Plat();
        clone(plat, cloned);
        return cloned;
    }
}
