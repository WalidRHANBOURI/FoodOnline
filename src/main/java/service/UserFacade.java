/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.User;
import controler.util.HashageUtil;
import controler.util.JsfUtil;
import controler.util.SessionUtil;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author HP
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "com.mycompany_FoodOnline_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    @Override
    public User find(Object id) {
        try {
            User user = (User) em.createQuery("select u from User u where u.login = '" + id + "'").getSingleResult();
            if (user != null) {
                return user;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Login incorrect");
        }
        return null;
    }

    public void seDeConnnecter() {
      
        SessionUtil.getSession().invalidate();

    }

   

    

    public int changePassword(String login, String oldPassword, String newPassword, String newPasswordConfirmation) {
        System.out.println("voila hana dkhalt le service verifierPassword");
        User loadedeUser = find(login);

        if (!newPasswordConfirmation.equals(newPassword)) {
            return -1;
        } else if (!loadedeUser.getMotDePasse().equals(HashageUtil.sha256(oldPassword))) {
            return -2;
        } else if (oldPassword.equals(newPassword)) {
            return -3;
        }
        loadedeUser.setMotDePasse(HashageUtil.sha256(newPassword));
        edit(loadedeUser);
        return 1;
    }

    public void changeData(User user) {
        User loadedUser = find(user.getLogin());
        clone(user, loadedUser);
        edit(loadedUser);
    }

    public void clone(User userSource, User userDestination) {
        userDestination.setNom(userSource.getNom());
        userDestination.setPrenom(userSource.getPrenom());
        userDestination.setNumTelephone(userSource.getNumTelephone());
        userDestination.setEmail(userSource.getEmail());
        userDestination.setLogin(userSource.getLogin());
        userDestination.setBlocked(userSource.getBlocked());
        
        userDestination.setNbrCnx(userSource.getNbrCnx());
        userDestination.setNom(userSource.getNom());
        userDestination.setMotDePasse(userSource.getMotDePasse());
        userDestination.setPrenom(userSource.getPrenom());
    }

    public int seConnnecter(User user) {
        if (user == null || user.getLogin() == null) {
            JsfUtil.addErrorMessage("Veuilliez saisir votre login");
            return -5;
        } else {
            User loadedUser = find(user.getLogin());
            if (loadedUser == null) {
                return -4;
            } else if (!loadedUser.getMotDePasse().equals(HashageUtil.sha256(user.getMotDePasse()))) {
                if (loadedUser.getNbrCnx() < 3) {
                    System.out.println("hana loadedUser.getNbrCnx() < 3 ::: " + loadedUser.getNbrCnx());
                    loadedUser.setNbrCnx(loadedUser.getNbrCnx() + 1);
                } else if (loadedUser.getNbrCnx() >= 3) {
                    System.out.println("hana loadedUser.getNbrCnx() >= 3::: " + loadedUser.getNbrCnx());
                    loadedUser.setBlocked(1);
                    // edit(loadedUser);
                }
                JsfUtil.addErrorMessage("Mot de passe incorrect");
                return -3;
            } else if (loadedUser.getBlocked() == 1) {
                JsfUtil.addErrorMessage("Cet utilisateur est bloqué");
                return -2;
            } else {
                loadedUser.setNbrCnx(0);
                //edit(loadedUser);
                user = UserFacade.this.clone(loadedUser);
                
                user.setMdpChanged(loadedUser.isMdpChanged());
                user.setMotDePasse(null);
                
                
                return 1;
            }
        }
    }

    
  

    public User clone(User user) {
        User clone = new User();
    
        
        return clone;
    }

}
