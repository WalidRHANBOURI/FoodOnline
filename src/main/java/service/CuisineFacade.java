/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Cuisine;
import bean.Menu;
import bean.Plat;
import bean.Restaurant;
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
public class CuisineFacade extends AbstractFacade<Cuisine> {

    @PersistenceContext(unitName = "com.mycompany_FoodOnline_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public void findCuisineByRestaurant(Restaurant restaurant){
        em.createQuery("select csn from Cuisine csn where csn.menus.restaurant.id='"+restaurant.getId()+"'").getResultList();
    }
       public List<Plat> platCuisine(Cuisine cuisine){
       String requette = "select p from Plat p where p.cuisine.id='"+cuisine.getId()+"'";
       return em.createQuery(requette).getResultList();
   } 
   public List<Cuisine> cuisineByMenu(Menu menu){
       List<Cuisine> cuisines = new ArrayList<>();
       String requette="";
       if(menu!=null){
           requette ="select mc.cuisines_ID from menu_cuisine mc where mc.menu_ID='"+menu.getId()+"'";
       }
       List<String> listes=em.createNativeQuery(requette).getResultList();
       System.out.println(listes);
       for (String liste : listes) {
           cuisines.add(find(liste)); 
       }
      return cuisines;
   }
    public CuisineFacade() {
        super(Cuisine.class);
    }
    
    private void clone(Cuisine cuisineSource, Cuisine cuisineDestiantion){
        cuisineDestiantion.setId(cuisineSource.getId());
    }
    
    public Cuisine clone(Cuisine cuisine){
        Cuisine cloned = new Cuisine();
        clone(cuisine, cloned);
        return cloned;
    }
    
}
