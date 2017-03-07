/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Quartier;
import bean.Ville;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author walid
 */
@Stateless
public class QuartierFacade extends AbstractFacade<Quartier> {

    @PersistenceContext(unitName = "com.mycompany_FoodOnline_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public List<Quartier> findQuartierByVille(Ville ville) {
        return em.createQuery("select q from Quartier q where q.ville.id='" + ville.getId() + "'").getResultList();

    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public QuartierFacade() {
        super(Quartier.class);
    }
    
    private void clone(Quartier quartierSource, Quartier quartierDestination){
        quartierDestination.setId(quartierSource.getId());
    }
    
    public Quartier clone(Quartier quartier){
        Quartier cloned = new Quartier();
        clone(quartier, cloned);
        return cloned;
    }

}
