/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Cuisine;
import bean.Restaurant;
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
    public CuisineFacade() {
        super(Cuisine.class);
    }
    
}
