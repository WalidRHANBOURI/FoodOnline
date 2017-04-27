package controler;


import bean.User;
import controler.util.EmailUtil;
import controler.util.HashageUtil;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import java.io.IOException;
import service.UserFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

@Named("userController")
@SessionScoped
public class UserController implements Serializable {

    @EJB
    private service.UserFacade ejbFacade;
    private List<User> items ;
    private User selected;
    private String username;
    private String password;
   

    public void connect() throws IOException {
        Object[] res = getFacade().connect(selected);
        System.out.println("haanii");
        int resEnt = (int) res[0];
        if (resEnt == -1) {
            System.out.println("hanii f resEnt " + resEnt);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login incorrect!", "login incorrect"));

        } else if (resEnt == -2) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "password incorrect!", "password incorrect"));

        } else {
            SessionUtil.registerUser(selected);
            SessionUtil.redirect("/FoodOnline/faces/restaurant/Welcome.xhtml");
            System.out.println(SessionUtil.getConnectedUser());
            SessionUtil.setAttribute("clt", selected);
            System.out.println("hahwa client  "+SessionUtil.getConnectedUser());
        }

    }
    public void connectDialog() throws IOException {
        Object[] res = getFacade().connect(selected);
        System.out.println("haanii");
        int resEnt = (int) res[0];
        if (resEnt == -1) {
            System.out.println("hanii f resEnt " + resEnt);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login incorrect!", "login incorrect"));

        } else if (resEnt == -2) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "password incorrect!", "password incorrect"));

        } else {
            SessionUtil.registerUser(selected);
            RequestContext.getCurrentInstance().execute("PF('ConnexDialog').hide()");
            System.out.println(SessionUtil.getConnectedUser());
            SessionUtil.setAttribute("clt", selected);
            System.out.println("hahwa client  "+SessionUtil.getConnectedUser());
        }

    }
     public void connectRespo() throws IOException {
        Object[] res = getFacade().connect(selected);
        System.out.println("haanii");
        int resEnt = (int) res[0];
        if (resEnt == -1) {
            System.out.println("hanii f resEnt " + resEnt);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login incorrect!", "login incorrect"));

        } else if (resEnt == -2) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "password incorrect!", "password incorrect"));

        } else {
            SessionUtil.registerUser(selected);
             SessionUtil.redirect("/FoodOnline/faces/menu/Wizard.xhtml");
            System.out.println(SessionUtil.getConnectedUser());
            SessionUtil.setAttribute("clt", selected);
            System.out.println("hahwa respo  "+SessionUtil.getConnectedUser());
        }

    }
    
    public void deconnect(){
        ejbFacade.seDeConnnecter();
        
      
    }

    public void envoieMsgRenitialisation() throws MessagingException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        User user = ejbFacade.findUserByEmail(selected.getEmail());
        if (user == null) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "cet utilisateur n'existe pas ou bien vous n'avez pas saisi correctement votre adresse mail"));
        } else {
            System.out.println("haa session " + request.getSession());
            request.getSession().setAttribute("mail", selected.getEmail());
            System.out.println("haa session " + request.getSession().getAttribute("mail"));
//             request.setAttribute(selected.getEmail(), "mail");
//            SessionUtil.getSession().setAttribute("mail", selected.getEmail());
            String to = selected.getEmail();
            String msg = "We heard that you lost your foodOnligne password. Sorry about that! But don’t worry! You can use the following link to reset your password:";
            msg += "<br/><a href=\"http://localhost:8080/generationTest/faces/changerMdp.xhtml\">clic here</a>";
            if (EmailUtil.senMail("wijdane.boukaid@gmail.com", "wiji123.", msg, to, "[foodOnLigne] Please reset your password")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes!", "check your mail"));
                System.out.println("avec succés");
            } else {
                System.out.println("fail!!!");
            }
        }
    }

//    public UserFacade getEjbFacade() {
//        return ejbFacade;
//    }

    public void setEjbFacade(UserFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserController() {
    }

    public User getSelected() {
        if (selected == null) {
            selected = new User();
        }
        return selected;
    }

    public void setSelected(User selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UserFacade getFacade() {
        return ejbFacade;
    }

    public User prepareCreate() {
        selected = new User();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UserCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UserUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UserDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<User> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        System.out.println("hanii");
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    
                    SessionUtil.setAttribute("mdp", selected.getMotDePasse());
                    selected.setMotDePasse(HashageUtil.sha256(selected.getMotDePasse()));
                    System.out.println("avant send email");
                    String msg = "voila votre login : " + selected.getLogin() + " et votre mot de passe est </br>" + (String) SessionUtil.getAttribute("mdp");
                    msg += "<br/> <a href=\"http://localhost:8080/FoodOnline/faces/Connexion.xhtml\">clic sur ce lien pour vous connecter</a>";
                    EmailUtil.senMail("wijdane.boukaid@gmail.com", "wiji123.", msg, selected.getEmail(), "[foodOnLigne] acceder a votre compte");
                    System.out.println("ha howa mdp" + (String) SessionUtil.getAttribute("mdp"));
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public User getUser(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<User> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<User> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = User.class)
    public static class UserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserController controller = (UserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userController");
            return controller.getUser(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof User) {
                User o = (User) object;
                return getStringKey(o.getLogin());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), User.class.getName()});
                return null;
            }
        }

    }

}
