/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.CmdItem;
import bean.IngredientPlat;
import bean.Plat;
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

    
 
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CmdItemFacade() {
        super(CmdItem.class);
    }
    
    private void clone(CmdItem cmdItemSource, CmdItem cmdItemDestination){
        cmdItemDestination.setId(cmdItemSource.getId());
        cmdItemDestination.setPrix(cmdItemSource.getPrix());
        cmdItemDestination.setQuantite(cmdItemSource.getQuantite());
        cmdItemDestination.setPlat(cmdItemSource.getPlat());
    }
    
    public CmdItem clone(CmdItem cmdItem){
        CmdItem cloned = new CmdItem();
        clone(cmdItem, cloned);
        return cloned;
    }
    
}
