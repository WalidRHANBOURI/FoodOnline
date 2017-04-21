/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Demande;
import bean.User;
import controler.util.DeviceUtil;
import controler.util.EmailUtil;
import controler.util.HashageUtil;
import controler.util.JsfUtil;
import controler.util.SessionUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
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
    @EJB
    public DeviceFacade deviceFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
//    public void create(User selected,Demande demande){
//               SessionUtil.setAttribute("mdp", selected.getMotDePasse());
//                    selected.setMotDePasse(HashageUtil.sha256(selected.getMotDePasse()));
//                    System.out.println("avant send email");
//                    String msg = "voila votre login : " + selected.getLogin() + " et votre mot de passe est </br>" + (String) SessionUtil.getAttribute("mdp");
//                    msg += "<br/> <a href=\"http://localhost:8080/FoodOnline/faces/Connexion.xhtml\">clic sur ce lien pour vous connecter</a>";
//        try {
//            EmailUtil.senMail("wijdane.boukaid@gmail.com", "wiji123.", msg, selected.getEmail(), "[foodOnLigne] acceder a votre compte");
//        } catch (MessagingException ex) {
//            Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
//        }
//                    System.out.println("ha howa mdp" + (String) SessionUtil.getAttribute("mdp"));
//                    edit(selected);
//    }

 

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

    public User findUserByEmail(String email) {
        String requette = "select u from User u where u.email='" + email + "'";
        List<User> users = em.createQuery(requette).getResultList();
        System.out.println(users.get(0));
        return users.get(0);
    }

    public Object[] connect(User user) {
        List<User> res = em.createQuery("select u from User u where u.login='" + user.getLogin() + "'").getResultList();
        if (res == null || res.isEmpty()) {
            return new Object[]{-1, null};
        } else if (!res.get(0).getMotDePasse().equals(HashageUtil.sha256(user.getMotDePasse()))) {
            return new Object[]{-2, null};

        } else {
            System.out.println(DeviceUtil.getDevice());
            System.out.println(res.get(0));
            System.out.println("hahwa result dial checkDevice" +deviceFacade.checkDevice(res.get(0), DeviceUtil.getDevice()));
            int resDevice = deviceFacade.checkDevice(res.get(0), DeviceUtil.getDevice());
            System.out.println(resDevice);
//            switch(resDevice) {
//                case 1:
//                deviceFacade.create(DeviceUtil.getDevice());
//                return new Object[]{1, res.get(0)};
//                case 2:
//                return new Object[]{2, res.get(0)};
//                default:
//                return new Object[]{3, res.get(0)};
//
//            }
            if (resDevice == 1 || resDevice == -1) {
                System.out.println("mal9itch device w ghadi ndir leh la creeation");
                deviceFacade.save(user, DeviceUtil.getDevice());
                return new Object[]{1, res.get(0)};
            } else {
                System.out.println("kayn deja et je vais pas le créér");
                return new Object[]{2, res.get(0)};
            }
        }
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
        cloneData(user, loadedUser);
        edit(loadedUser);
    }

    public void cloneData(User userSource, User userDestination) {
        userDestination.setNom(userSource.getNom());
        userDestination.setPrenom(userSource.getPrenom());
        userDestination.setNumTelephone(userSource.getNumTelephone());
        userDestination.setEmail(userSource.getEmail());
    }

    public void clone(User userSource, User userDestination) {
        userDestination.setNom(userSource.getNom());
        userDestination.setPrenom(userSource.getPrenom());
        userDestination.setNumTelephone(userSource.getNumTelephone());
        userDestination.setEmail(userSource.getEmail());
        userDestination.setLogin(userSource.getLogin());

        userDestination.setNom(userSource.getNom());
        userDestination.setMotDePasse(userSource.getMotDePasse());
        userDestination.setPrenom(userSource.getPrenom());
    }

    public User clone(User user) {
        User clone = new User();

        return clone;
    }

}
