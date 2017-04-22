/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Cuisine;
import bean.Menu;
import bean.Quartier;
import bean.Restaurant;
import bean.Ville;
import controler.util.SearchUtil;
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
public class RestaurantFacade extends AbstractFacade<Restaurant> {

    @PersistenceContext(unitName = "com.mycompany_FoodOnline_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @EJB
    private MenuFacade menuFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public MenuFacade getMenuFacade() {
        return menuFacade;
    }

    public void setMenuFacade(MenuFacade menuFacade) {
        this.menuFacade = menuFacade;
    }

    public RestaurantFacade() {
        super(Restaurant.class);
    }

    public List<Restaurant> findRestauByQuartier(Quartier quartier) {
        String requette = "";
        if (quartier != null) {
            requette += "select resto from Restaurant resto where resto.quartier.id='" + quartier.getId() + "'";
        }
        return em.createQuery(requette).getResultList();
    }

    private List<Menu> findMenuByCuisine(Cuisine cuisine) {
        List<Menu> menus = new ArrayList<>();
        if (cuisine != null) {
            String requette = "select mc.Menu_ID from menu_cuisine mc where mc.cuisines_ID ='" + cuisine.getId() + "'";
            List<Long> listes = em.createNativeQuery(requette).getResultList();
            for (Long liste : listes) {
                Menu menu = menuFacade.find(liste);
                menus.add(menu);
            }
        }

        return menus;
    }

    public List<Restaurant> findRestauByCuisine(Cuisine cuisine) {
        List<Menu> resMenus = findMenuByCuisine(cuisine);
        List<Restaurant> res = new ArrayList<>();
        for (Menu resMenu : resMenus) {
            res.add(resMenu.getRestaurant());
        }
        return res;
    }

    public List<Restaurant> search(Ville ville, Quartier quartier, Cuisine cuisine) {
        String requette = "select resto from Restaurant resto where 1=1";
        if (ville != null) {
            requette += SearchUtil.addConstraint("resto", "quartier.ville.id", "=", ville.getId());
        }
        if (quartier != null) {
            requette += SearchUtil.addConstraint("resto", "quartier.id", "=", quartier.getId());
        }
        List<Restaurant> res = em.createQuery(requette).getResultList();
        if (cuisine != null) {
            List<Restaurant> resByCuisine = findRestauByCuisine(cuisine);
            for (Restaurant restaurant : resByCuisine) {
                if (restaurant.getQuartier().equals(quartier) && !res.contains(restaurant)) {
                    res.add(restaurant);
                }
            }
        }
        return res;
    }
    
    private void clone(Restaurant restaurantSource, Restaurant restaurantDestination){
        restaurantDestination.setId(restaurantSource.getId());
        restaurantDestination.setAdresse(restaurantSource.getAdresse());
        restaurantDestination.setLat(restaurantSource.getLat());
        restaurantDestination.setLng(restaurantSource.getLng());
        restaurantDestination.setNum(restaurantSource.getNum());
        restaurantDestination.setNbrEtoile(restaurantSource.getNbrEtoile()); 
        restaurantDestination.setQuartier(restaurantSource.getQuartier());
    }
    
    public Restaurant clone(Restaurant restaurant){
        Restaurant cloned = new Restaurant();
        clone(restaurant, cloned);
        return cloned;
    }
    
}
