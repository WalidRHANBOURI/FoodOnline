/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.IngredientChoisit;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author walid
 */
@Stateless
public class IngredientChoisitFacade extends AbstractFacade<IngredientChoisit> {

    @PersistenceContext(unitName = "com.mycompany_FoodOnline_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IngredientChoisitFacade() {
        super(IngredientChoisit.class);
    }
    
    private void clone(IngredientChoisit ingredientChoisitSource, IngredientChoisit ingredientChoisitDestination){
        ingredientChoisitDestination.setId(ingredientChoisitSource.getId());
    }
    
    public IngredientChoisit clone(IngredientChoisit ingredientChoisit){
        IngredientChoisit cloned = new IngredientChoisit();
        clone(ingredientChoisit, cloned);
        return cloned;
    }
    
}
