package controler;

import bean.Menu;
import bean.Plat;
import bean.PlatMenu;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.PlatMenuFacade;

import java.io.Serializable;
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
import service.CuisineFacade;
import service.PlatFacade;

@Named("platMenuController")
@SessionScoped
public class PlatMenuController implements Serializable {

    @EJB
    private service.PlatMenuFacade ejbFacade;
    @EJB
    private service.CuisineFacade ejbCuisineFacade;
    @EJB
    private service.PlatFacade ejbPlatFacade;
    private List<PlatMenu> items = null;
    private PlatMenu selected;
    private Menu menu;
    private List<Plat> plats;
      public void cuisineByMenu() {
        selected.getMenu().setCuisines(ejbCuisineFacade.cuisineByMenu(selected.getMenu()));

    }
      public void createPlatMenu(){
          ejbFacade.createPlatMenu(selected, plats);
      }
        public void platByCuisine() {
        System.out.println("haniiiii");
        selected.getCuisine().setPlats(ejbPlatFacade.findPlatByCuisine(selected.getCuisine()));
        System.out.println(selected.getCuisine().getPlats());
        System.out.println(selected.getPlat());

    }

    public PlatMenuController() {
    }

    public PlatFacade getEjbPlatFacade() {
        return ejbPlatFacade;
    }

    public void setEjbPlatFacade(PlatFacade ejbPlatFacade) {
        this.ejbPlatFacade = ejbPlatFacade;
    }

    public List<Plat> getPlats() {
        return plats;
    }

    public void setPlats(List<Plat> plats) {
        this.plats = plats;
    }
    
    
    public PlatMenu getSelected() {
        return selected;
    }

    public void setSelected(PlatMenu selected) {
        this.selected = selected;
    }

    public PlatMenuFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(PlatMenuFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public CuisineFacade getEjbCuisineFacade() {
        return ejbCuisineFacade;
    }

    public void setEjbCuisineFacade(CuisineFacade ejbCuisineFacade) {
        this.ejbCuisineFacade = ejbCuisineFacade;
    }

    public Menu getMenu() {
        if(menu==null){
            menu= new Menu();
        }
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
    
    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PlatMenuFacade getFacade() {
        return ejbFacade;
    }
//    public void findCuisineByMenu(){
//        System.out.println(menu);
//      selected.getMenu().setCuisines(ejbCuisineFacade.findCuisineByMenu(menu));
//        System.out.println(menu.getCuisines());
//    }
    public PlatMenu prepareCreate() {
        selected = new PlatMenu();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PlatMenuCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PlatMenuUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PlatMenuDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<PlatMenu> getItems() {
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

    public PlatMenu getPlatMenu(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<PlatMenu> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PlatMenu> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = PlatMenu.class)
    public static class PlatMenuControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PlatMenuController controller = (PlatMenuController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "platMenuController");
            return controller.getPlatMenu(getKey(value));
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
            if (object instanceof PlatMenu) {
                PlatMenu o = (PlatMenu) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PlatMenu.class.getName()});
                return null;
            }
        }

    }

}
