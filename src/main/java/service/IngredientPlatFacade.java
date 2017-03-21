/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;


import bean.IngredientPlat;
import bean.Plat;

import controler.util.SearchUtil;
import java.util.List;
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
    
    public List<IngredientPlat> findIngredientByPlat(Plat plat){
        String requette= "select ip from IngredientPlat ip where 1=1";
        requette += SearchUtil.addConstraint("ip","plat.id", "=", plat.getId());
        return em.createQuery(requette).getResultList();
    }
    public Double prixIngredient(IngredientPlat ingredientPlat){
        Double prixIng = 0.0;
        prixIng = ingredientPlat.getPrix() + ingredientPlat.getIngredient().getPrix();
        return prixIng;
    }
    public Double prixTotalIngredient(List<IngredientPlat> ingredientPlats){
        Double price = 0.0;
        for (IngredientPlat ingredientPlat : ingredientPlats) {
            price += prixIngredient(ingredientPlat);
            
        }
        return price; 
    }
    public Double total(Plat plat, List<IngredientPlat> ingChoisis){
        Double prixIngr = prixTotalIngredient(ingChoisis);
        Double prixPlat = plat.getPrix();
        Double total = prixIngr + prixPlat ;
        
        return total;
    }
   


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
