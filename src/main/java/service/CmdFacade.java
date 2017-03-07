/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Cmd;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author walid
 */
@Stateless
public class CmdFacade extends AbstractFacade<Cmd> {

    @PersistenceContext(unitName = "com.mycompany_FoodOnline_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CmdFacade() {
        super(Cmd.class);
    }
    
    private void clone(Cmd cmdSource, Cmd cmdDestination){
        cmdDestination.setId(cmdSource.getId());
        cmdDestination.setDateCmd(cmdSource.getDateCmd());
        cmdDestination.setTotal(cmdSource.getTotal());
    }
    
    public Cmd clone(Cmd cmd){
        Cmd cloned = new Cmd();
        clone(cmd, cloned);
        return cloned;
    }
    
}
