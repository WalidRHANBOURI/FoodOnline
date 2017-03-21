package controler;

import bean.CmdItem;
import bean.IngredientPlat;
import bean.Plat;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.PlatFacade;

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
import service.IngredientPlatFacade;

@Named("platController")
@SessionScoped
public class PlatController implements Serializable {

    @EJB
    private service.PlatFacade ejbFacade;
    @EJB
    private service.IngredientPlatFacade ingredientPlatFacade;
    private List<Plat> items = null;
    private Plat selected;
    private CmdItem cmdItem;
    private Double prix;
    private Double total;
    private List<IngredientPlat> ingredientPlats;
    private List<IngredientPlat> ingredientChoisis;
    private List<CmdItem> panier;
    
    
    
      public void findIngredientByPlat(){
          System.out.println(selected);
          ingredientPlats = ingredientPlatFacade.findIngredientByPlat(selected);
    }
    public void prixTotalIngredient(){
        System.out.println(ingredientChoisis);
        prix = ingredientPlatFacade.prixTotalIngredient(ingredientChoisis);
      
    }
    public void total(){
        total = ingredientPlatFacade.total(selected, ingredientChoisis);

    }

    public CmdItem getCmdItem() {
        return cmdItem;
    }

    public void setCmdItem(CmdItem cmdItem) {
        this.cmdItem = cmdItem;
    }

    public List<CmdItem> getPanier() {
        return panier;
    }

    public void setPanier(List<CmdItem> panier) {
        this.panier = panier;
    }
    

    public List<IngredientPlat> getIngredientChoisis() {
        return ingredientChoisis;
    }

    public void setIngredientChoisis(List<IngredientPlat> ingredientChoisis) {
        this.ingredientChoisis = ingredientChoisis;
    }
    
    public PlatFacade getEjbFacade() {
        return ejbFacade;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
    
    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }
    
    public void setEjbFacade(PlatFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public IngredientPlatFacade getIngredientPlatFacade() {
        return ingredientPlatFacade;
    }

    public void setIngredientPlatFacade(IngredientPlatFacade ingredientPlatFacade) {
        this.ingredientPlatFacade = ingredientPlatFacade;
    }

    public List<IngredientPlat> getIngredientPlats() {
        return ingredientPlats;
    }

    public void setIngredientPlats(List<IngredientPlat> ingredientPlats) {
        this.ingredientPlats = ingredientPlats;
    }
    
    public PlatController() {
    }

    public Plat getSelected() {
        if(selected == null){
            selected = new Plat();
        }
        return selected;
    }

    public void setSelected(Plat selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PlatFacade getFacade() {
        return ejbFacade;
    }

    public Plat prepareCreate() {
        selected = new Plat();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PlatCreated"));
        if (!JsfUtil.isValidationFailed()) {
            getItems().add(ejbFacade.clone(selected));   // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PlatUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PlatDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Plat> getItems() {
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

    public Plat getPlat(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<Plat> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Plat> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter("platControllerConverter")
    public static class PlatControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PlatController controller = (PlatController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "platController");
            return controller.getPlat(getKey(value));
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
            if (object instanceof Plat) {
                Plat o = (Plat) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Plat.class.getName()});
                return null;
            }
        }

    }

}
