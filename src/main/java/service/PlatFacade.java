/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Cuisine;
import bean.Plat;
import bean.PlatMenu;
import bean.Restaurant;
import controler.util.SearchUtil;
import java.util.ArrayList;
import java.util.List;
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
    public void triPlatAsc(List<Plat> plats){
        List<Plat> tri = new ArrayList<>();
        Plat min = plats.get(0);
        
        
    }
    public Restaurant findRestoByPlat(Plat plat){
        String requette ="select p from PlatMenu p where 1=1";
        requette += SearchUtil.addConstraint("p", "plat.id", "=", plat.getId());
        List<PlatMenu> res = em.createQuery(requette).getResultList();
        return res.get(0).getMenu().getRestaurant();
        
    }
     public List<Plat> findPlatByCuisine(Cuisine cuisine){
        String requette="";
        if(cuisine !=null){
           requette +="select p from Plat p where p.cuisine.id='"+cuisine.getId()+"'";
        }
       return em.createQuery(requette).getResultList();
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
