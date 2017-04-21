/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Cmd;
import bean.CmdItem;
import bean.Restaurant;
import bean.User;
import controler.util.SearchUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
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
    public Cmd saveCmd(List<CmdItem> cmdItems, Double total, String adresseLivraison,User client){
        Cmd cmd = new Cmd();
        Date date = new Date();
        cmd.setDateCmd(date);
        cmd.setTotal(total);
        cmd.setUser(client);
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
    public List<Cmd> findCmdByResto(Restaurant restaurant){
        List<Cmd> res = new ArrayList<>();
        List<Cmd> allCmd = findAll();
        for (Cmd cmd : allCmd) {
            List<CmdItem> cmdItems = findCmdItemByCmd(cmd);
            for (CmdItem cmdItem : cmdItems) {
                if(cmdItem.getRestaurant().equals(restaurant)){
                    res.add(cmd);
                }
            }
        }
        return res;
    }
    public List<Cmd> search(Double totalMin,Double totalMax,Date dateCmdDebut,Date dateCmdFin){
        String req = "select cmd from Cmd cmd where 1=1";

             req += SearchUtil.addConstraintMinMax("cmd", "total",totalMin,totalMax);
            req += SearchUtil.addConstraintMinMaxDate("cmd","dateCmd", dateCmdDebut, dateCmdFin);
            List<Cmd> res = em.createQuery(req).getResultList();
            return res;

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
