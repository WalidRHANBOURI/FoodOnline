/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Cuisine;
import bean.Menu;
import bean.Plat;
import bean.PlatMenu;
import bean.Restaurant;
import bean.User;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
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
    @EJB
    private RestaurantFacade restoFacade;
    @EJB
    private MenuFacade menuFacade;
    @EJB
    private PlatMenuFacade platMenuFacade;

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
        public int creeMenu(User user, Menu menu, List<Plat> plats) {
        List<Restaurant> restos = em.createQuery("select r from Restaurant r where r.user.login='" + user.getLogin() + "'").getResultList();
        Restaurant restaurant = restos.get(0);
        if (restaurant != null) {
            menu.setId(menuFacade.generateId("Menu", "id"));
            menu.setRestaurant(restaurant);
            System.out.println(menu.getRestaurant());
            menuFacade.create(menu);
            for (Plat plat : plats) {
                PlatMenu platMenu = new PlatMenu();
                platMenu.setPlat(plat);
                platMenu.setCuisine(plat.getCuisine());
                platMenu.setMenu(menu);
                platMenuFacade.create(platMenu);

            }
            return 1;
        }

        return -1;
    }
        
    public List<Plat> findAllPlatsByCuisine(List<Cuisine> cuisines) {
        List<Plat> plats = new ArrayList<>();
        for (Cuisine cuisine : cuisines) {
            plats.addAll(em.createQuery("select p from Plat p where p.cuisine.id='" + cuisine.getId() + "'").getResultList());
        }
        return plats;
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
