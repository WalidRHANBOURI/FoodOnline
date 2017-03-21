/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Cuisine;
import bean.Menu;
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
public class MenuFacade extends AbstractFacade<Menu> {

    @PersistenceContext(unitName = "com.mycompany_FoodOnline_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    public void remplirListCuisine(Menu menu,List<Cuisine> cuisines){
        System.out.println(cuisines);
        if(cuisines != null){
        for (Cuisine cuisine : cuisines) {
          menu.getCuisines().add(cuisine);
            System.out.println(menu.getCuisines());
        }
        edit(menu);
        }
    }
    public MenuFacade() {
        super(Menu.class);
    }

    private void clone(Menu menuSource, Menu menuDestination){
        menuDestination.setId(menuSource.getId());
        menuDestination.setRestaurant(menuSource.getRestaurant());
    }
    
    public Menu clone(Menu menu){
        Menu cloned = new Menu();
        clone(menu, cloned);
        return cloned;
    }
    
    
}
