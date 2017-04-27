package controler;

import bean.Cuisine;
import bean.Menu;
import bean.Plat;
import bean.User;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import service.MenuFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("menuController")
@SessionScoped
public class MenuController implements Serializable {

    @EJB
    private service.MenuFacade ejbFacade;
    
    private List<Menu> items = null;
    private Menu selected;
    private List<Cuisine> cuisines;
    private Cuisine cuisine;
    private List<Plat> plats = null;
    private List<Plat> platsChoisi = null;
    
    
//      public void findByCuisine(){
//        System.out.println(cuisine);
//       items =  ejbFacade.findRestauByCuisine(cuisine);
//    } 
    public void createCuisineMenu(){
        System.out.println(selected);
        ejbFacade.remplirListCuisine(selected, cuisines);
    }
        public void findPlatsByCuisines() {
        List<Plat> liste = new ArrayList<>();
        System.out.println("hanii dkhalt la fct findPlatsByCuisines");
        System.out.println("ha houma les couisines ");
        System.out.println("ha houma les cuisines choisis" + selected.getCuisines());
        System.out.println(ejbFacade.findAllPlatsByCuisine(selected.getCuisines()));
        plats = ejbFacade.findAllPlatsByCuisine(selected.getCuisines());

    }
           public String createMenu() {
        User user = (User) SessionUtil.getConnectedUser();
        ejbFacade.creeMenu(user, selected, platsChoisi);
        return "platMenu/List.xhtml";
    }
    public MenuController() {
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    public List<Plat> getPlats() {
        return plats;
    }

    public void setPlats(List<Plat> plats) {
        this.plats = plats;
    }

    public List<Plat> getPlatsChoisi() {
        return platsChoisi;
    }

    public void setPlatsChoisi(List<Plat> platsChoisi) {
        this.platsChoisi = platsChoisi;
    }
    
  
    public MenuFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(MenuFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public List<Cuisine> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<Cuisine> cuisines) {
        this.cuisines = cuisines;
    }
    
    public Menu getSelected() {
        if(selected == null){
            selected = new Menu();
        }
        return selected;
    }

    public void setSelected(Menu selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MenuFacade getFacade() {
        return ejbFacade;
    }

    public Menu prepareCreate() {
        selected = new Menu();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MenuCreated"));
        if (!JsfUtil.isValidationFailed()) {
            getItems().add(ejbFacade.clone(selected));   // Invalidate list of items to trigger re-query.
            System.out.println(items);
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MenuUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MenuDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Menu> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
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

    public Menu getMenu(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Menu> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Menu> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter("menuControllerConverter")
    public static class MenuControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MenuController controller = (MenuController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "menuController");
            return controller.getMenu(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
         public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Menu) {
                Menu o = (Menu) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Menu.class.getName()});
                return null;
            }
        }

    }

}
