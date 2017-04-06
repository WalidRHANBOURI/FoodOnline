/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.CmdItem;
import bean.IngredientPlat;
import bean.Plat;
import bean.Restaurant;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author walid
 */
@Stateless
public class CmdItemFacade extends AbstractFacade<CmdItem> {

    @PersistenceContext(unitName = "com.mycompany_FoodOnline_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    private service.IngredientPlatFacade ingredientPlatFacade;

    public CmdItem remplirCmdItem(Plat plat, List<IngredientPlat> ingPlats,Restaurant restaurant) {
        CmdItem cmdItem = new CmdItem();
        cmdItem.setId(generateId("CmdItem", "id"));
        cmdItem.setRestaurant(restaurant);
        cmdItem.setPlat(plat);
        cmdItem.setQuantite(1);
        Double total = 0.0d;
        total += total(plat, ingPlats);
        cmdItem.setPrix(total);
        if(ingPlats != null && plat.getType().equalsIgnoreCase("personnalise")){
        for (IngredientPlat ingPlat : ingPlats) {
            cmdItem.getIngredientChoisits().add(ingPlat);
        }
        }
        return cmdItem;
    }
  
     public Double prixIngredient(IngredientPlat ingredientPlat) {
        System.out.println("hani kan7seb l prix dial" + ingredientPlat);
        Double prixIng = 0.0d;
        prixIng = ingredientPlat.getPrix() + ingredientPlat.getIngredient().getPrix();
        System.out.println(prixIng);
        return prixIng;
    }

    public Double prixTotalIngredient(List<IngredientPlat> ingredientPlats) {
        System.out.println("haniiii f prix total ingredient");
        System.out.println(ingredientPlats);
        Double price = 0.0d;
        for (IngredientPlat ingredientPlat : ingredientPlats) {
            System.out.println(ingredientPlat);
            price += prixIngredient(ingredientPlat);

        }
        return price;
    }

    public Double total(Plat plat, List<IngredientPlat> ingChoisis) {
            Double prixIngr = 0.0d;
            Double prixPlat = 0.0d;
            if(ingChoisis != null && plat.getType().equalsIgnoreCase("personnalise")){
                prixIngr += prixTotalIngredient(ingChoisis);
            }
            prixPlat += plat.getPrix() ;
            System.out.println("hahowa l prix dial l plat" + prixPlat);
            Double total = prixIngr + prixPlat;

            return total;
        
    }
    public Double totalDesCmdItem(List<CmdItem> cmdItems){
        Double price = 0.0;
        for (CmdItem cmdItem : cmdItems) {
            price += cmdItem.getPrix();
            
        }
        return price ;
    }
    public void updateQteAndPrice(CmdItem cmdItem , int qte){
        cmdItem.setQuantite(qte);
        Double prix = total(cmdItem.getPlat(), cmdItem.getIngredientChoisits());
        System.out.println(cmdItem.getPrix());
        cmdItem.setPrix(prix * qte);
        System.out.println(cmdItem.getPrix());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CmdItemFacade() {
        super(CmdItem.class);
    }

    private void clone(CmdItem cmdItemSource, CmdItem cmdItemDestination) {
        cmdItemDestination.setId(cmdItemSource.getId());
        cmdItemDestination.setPrix(cmdItemSource.getPrix());
        cmdItemDestination.setQuantite(cmdItemSource.getQuantite());
        cmdItemDestination.setPlat(cmdItemSource.getPlat());
    }

    public CmdItem clone(CmdItem cmdItem) {
        CmdItem cloned = new CmdItem();
        clone(cmdItem, cloned);
        return cloned;
    }

}
