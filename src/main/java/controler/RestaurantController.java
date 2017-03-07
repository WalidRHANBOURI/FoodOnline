package controler;

import bean.Cuisine;
import bean.Quartier;
import bean.Restaurant;
import bean.Ville;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.RestaurantFacade;

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

@Named("restaurantController")
@SessionScoped
public class RestaurantController implements Serializable {

    @EJB
    private service.RestaurantFacade ejbFacade;
    private List<Restaurant> items = null;
    private Restaurant selected;
    private Ville ville;
    private Quartier quartier;
    private Cuisine cuisine;
    
    

    public RestaurantFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(RestaurantFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

  

    
    public Ville getVille() {
        if(ville == null){
            ville = new Ville();
        }
        return ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public Quartier getQuartier() {
        if(quartier == null){
            quartier = new Quartier();
        }
        return quartier;
    }

    public void setQuartier(Quartier quartier) {
        this.quartier = quartier;
    }

    public Cuisine getCuisine() {
        if(cuisine == null){
            cuisine = new Cuisine();
        }
        return cuisine;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    public void findByQuartier(){
         System.out.println(quartier);
       items= ejbFacade.findRestauByQuartier(quartier);
        System.out.println(items);
    }
    public void findRestauByCuisine(){
        items=ejbFacade.findRestauByCuisine(cuisine);
    }
    public void search(){
       items= ejbFacade.search(ville, quartier, cuisine);
        System.out.println(items);
    }
    public RestaurantController() {
    }

    public Restaurant getSelected() {
        return selected;
    }

    public void setSelected(Restaurant selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private RestaurantFacade getFacade() {
        return ejbFacade;
    }

    public Restaurant prepareCreate() {
        selected = new Restaurant();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("RestaurantCreated"));
        if (!JsfUtil.isValidationFailed()) {
            getItems().add(ejbFacade.clone(selected));    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("RestaurantUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("RestaurantDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Restaurant> getItems() {
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

    public Restaurant getRestaurant(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<Restaurant> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Restaurant> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Restaurant.class)
    public static class RestaurantControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RestaurantController controller = (RestaurantController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "restaurantController");
            return controller.getRestaurant(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
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
            if (object instanceof Restaurant) {
                Restaurant o = (Restaurant) object;
                return o.getId();
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Restaurant.class.getName()});
                return null;
            }
        }

    }

}
