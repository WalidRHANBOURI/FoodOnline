/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Cmd;
import bean.CmdItem;
import controler.util.SearchUtil;
import java.util.Date;
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
public class CmdFacade extends AbstractFacade<Cmd> {

    @PersistenceContext(unitName = "com.mycompany_FoodOnline_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @EJB
    private service.CmdItemFacade cmdItemFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CmdFacade() {
        super(Cmd.class);
    }
    public Cmd saveCmd(List<CmdItem> cmdItems, Double total, String adresseLivraison){
        Cmd cmd = new Cmd();
        Date date = new Date();
        cmd.setDateCmd(date);
        cmd.setTotal(total);
        cmd.setAdresseLivraison(adresseLivraison);
        for (CmdItem cmdItem : cmdItems) {
            cmdItem.setCmd(cmd);
            cmd.getCmdItems().add(cmdItem);
            cmdItemFacade.create(cmdItem);
        }
        create(cmd);
        return cmd;
    }
    public List<CmdItem> findCmdItemByCmd(Cmd cmd){
        String requette = "select ci from CmdItem ci where 1=1";
        requette += SearchUtil.addConstraint("ci", "cmd.id", "=", cmd.getId());
        return em.createQuery(requette).getResultList();
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
