/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.IngredientPlat;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author walid
 */
@Stateless
public class IngredientPlatFacade extends AbstractFacade<IngredientPlat> {

    @PersistenceContext(unitName = "com.mycompany_FoodOnline_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IngredientPlatFacade() {
        super(IngredientPlat.class);
    }
    
    private void clone(IngredientPlat ingredientPlatSource, IngredientPlat ingredientPlatDestination){
        ingredientPlatDestination.setId(ingredientPlatSource.getId());
        ingredientPlatDestination.setPrix(ingredientPlatSource.getPrix());
    }
    
    public IngredientPlat clone(IngredientPlat ingredientPlat){
        IngredientPlat cloned = new IngredientPlat();
        clone(ingredientPlat, cloned);
        return cloned;
    }
    
}
