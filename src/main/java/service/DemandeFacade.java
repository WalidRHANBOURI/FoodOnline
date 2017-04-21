/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Demande;
import bean.Restaurant;
import bean.User;
import controler.util.SessionUtil;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author HP
 */
@Stateless
public class DemandeFacade extends AbstractFacade<Demande> {

    @PersistenceContext(unitName = "com.mycompany_FoodOnline_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @EJB
    private service.UserFacade userFacade;
    @EJB
    private service.RestaurantFacade restaurantFacade;
    private controler.UserController userController;
    
    public void acceptDemande(Demande demande){
         User user = demande.getResponsable();
        user.setType(3);
        userFacade.edit(user);
        Restaurant resto = new Restaurant();
        resto.setId(demande.getNomResto());
        resto.setQuartier(demande.getQuartierResto());
        resto.setNbrEtoile(demande.getNbrEtoile());
        resto.setNum(demande.getTelResto());
        resto.setAdresse(demande.getAdresseResto());
        resto.setResponsableResto(demande.getResponsable());
        restaurantFacade.create(resto);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DemandeFacade() {
        super(Demande.class);
    }
    
}
