package controler;

import bean.Ville;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.VilleFacade;

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
import service.QuartierFacade;

@Named("villeController")
@SessionScoped
public class VilleController implements Serializable {

    @EJB
    private service.VilleFacade ejbFacade;
    private List<Ville> items = null;
    private Ville selected;
    @EJB
    private service.QuartierFacade quartierFacade;

    public void findByVille() {
        selected.setQuartiers(quartierFacade.findQuartierByVille(selected));
        System.out.println(selected);
    }

    public VilleFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(VilleFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public QuartierFacade getQuartierFacade() {
        return quartierFacade;
    }

    public void setQuartierFacade(QuartierFacade quartierFacade) {
        this.quartierFacade = quartierFacade;
    }

    public VilleController() {
    }

    public Ville getSelected() {
        if (selected == null) {
            selected = new Ville();
        }
        return selected;
    }

    public void setSelected(Ville selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private VilleFacade getFacade() {
        return ejbFacade;
    }

    public Ville prepareCreate() {
        selected = new Ville();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("VilleCreated"));
        if (!JsfUtil.isValidationFailed()) {
            getItems().add(ejbFacade.clone(selected));   // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VilleUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VilleDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Ville> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public void setItems(List<Ville> items) {
        this.items = items;
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

    public Ville getVille(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<Ville> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Ville> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Ville.class)
    public static class VilleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VilleController controller = (VilleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "villeController");
            return controller.getVille(getKey(value));
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
            if (object instanceof Ville) {
                Ville o = (Ville) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Ville.class.getName()});
                return null;
            }
        }

    }

}
