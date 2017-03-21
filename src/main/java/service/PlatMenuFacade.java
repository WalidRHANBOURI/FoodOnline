/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Plat;
import bean.PlatMenu;
import bean.Restaurant;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author HP
 */
@Stateless
public class PlatMenuFacade extends AbstractFacade<PlatMenu> {

    @PersistenceContext(unitName = "com.mycompany_FoodOnline_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    public List<Plat> findPlatByResto(Restaurant restaurant){
        String requette="";
        if(restaurant !=null){
           requette += "select p.plat from PlatMenu p where p.menu.id='"+restaurant.getMenu().getId()+"'";
        }
        System.out.println(em.createQuery(requette).getResultList());
       return em.createQuery(requette).getResultList();
    }
    public void createPlatMenu(PlatMenu platMenu, List<Plat> plats){
      platMenu.setId(generateId("PlatMenu","id"));

        for (Plat plat : plats) {
            PlatMenu newPlatMenu = new PlatMenu();
            newPlatMenu.setCuisine(platMenu.getCuisine());
            newPlatMenu.setMenu(platMenu.getMenu());
            newPlatMenu.setPlat(plat);
            create(newPlatMenu);
        }
    }
    

    public PlatMenuFacade() {
        super(PlatMenu.class);
    }
    
}
