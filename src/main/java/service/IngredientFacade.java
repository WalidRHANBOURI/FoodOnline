/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Ingredient;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author walid
 */
@Stateless
public class IngredientFacade extends AbstractFacade<Ingredient> {

    @PersistenceContext(unitName = "com.mycompany_FoodOnline_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IngredientFacade() {
        super(Ingredient.class);
    }
    
        private void clone(Ingredient ingredientSource, Ingredient ingredientDestination){
        ingredientDestination.setId(ingredientSource.getId());
        ingredientDestination.setPrix(ingredientSource.getPrix());
    }
    
    public Ingredient clone(Ingredient ingredient){
        Ingredient cloned = new Ingredient();
        clone(ingredient, cloned);
        return cloned;
    }
    
    
}
