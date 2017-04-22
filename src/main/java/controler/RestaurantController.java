package controler;

import bean.Cuisine;
import bean.Plat;
import bean.Quartier;
import bean.Restaurant;
import bean.Ville;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import service.RestaurantFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.ReverseGeocodeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import service.PlatMenuFacade;

@Named("restaurantController")
@SessionScoped
public class RestaurantController implements Serializable {

    @EJB
    private service.RestaurantFacade ejbFacade;
    @EJB
    private service.PlatMenuFacade ejbPlatMenuFacade;
    @EJB
    private service.CuisineFacade cuisineFacade;
    @EJB
    private service.CmdFacade cmdFacade;
    private List<Restaurant> items;
    private Restaurant selected;
    private Ville ville;
    private Quartier quartier;
    private Cuisine cuisine;
    private List<Plat> itemsPlat;
    private List<Cuisine> cuisines;
    private MapModel emptyModel;
    private MapModel revGeoModel;
    private Marker marker;
    private String centerRevGeoMap = "33.53333, -7.58333";

    @PostConstruct
    public void init() {
        emptyModel = new DefaultMapModel();
        revGeoModel = new DefaultMapModel();
        for (int i = 0; i < getItems().size(); i++) {
            Restaurant restaurant = getItems().get(i);
            LatLng coord = new LatLng(restaurant.getLat(), restaurant.getLng());
            revGeoModel.addOverlay(new Marker(coord, restaurant.getId()));
        }
    }

    public MapModel getEmptyModel() {
        return emptyModel;
    }

    public void setEmptyModel(MapModel emptyModel) {
        this.emptyModel = emptyModel;
    }

    public MapModel getRevGeoModel() {
        if (revGeoModel == null) {
            revGeoModel = new DefaultMapModel();
        }
        return revGeoModel;
    }

    public Marker getMarker() {
        return marker;
    }

    public String getCenterRevGeoMap() {
        return centerRevGeoMap;
    }

    public void onReverseGeocode(ReverseGeocodeEvent event) {
        System.out.println("hada howa mochkil : " + selected);
        ejbFacade.create(selected);
        List<String> addresses = event.getAddresses();
        LatLng coord = event.getLatlng();
        if (addresses != null && !addresses.isEmpty()) {
            centerRevGeoMap = coord.getLat() + "," + coord.getLng();
            revGeoModel.addOverlay(new Marker(coord, addresses.get(0)));
        }

    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Restaurant : ", marker.getTitle()));
    }

    public void findByQuartier() {
        System.out.println(quartier);
        items = ejbFacade.findRestauByQuartier(quartier);
        System.out.println(items);
    }

    public void findRestauByCuisine() {
        items = ejbFacade.findRestauByCuisine(cuisine);
    }

    public void search() {
        items = ejbFacade.search(ville, quartier, cuisine);
        System.out.println(items);
    }

    public void menuResto(Restaurant item) {
        System.out.println("item " + item);
        selected = item;
        SessionUtil.setAttribute("anaResto", selected);
        System.out.println("hahwa resto selectionne" + selected);
//        itemsPlat = platController.getItems();
        itemsPlat = ejbPlatMenuFacade.findPlatByResto(selected);
        cuisines = cuisineFacade.cuisineByMenu(selected.getMenu());

        System.out.println(itemsPlat);
    }

    public void findCmdByRestaurant(Restaurant restaurant) {
        cmdFacade.findCmdByResto(restaurant);
    }

    public List<Cuisine> getCuisines() {
        if (cuisines == null) {
            cuisines = new ArrayList<>();
        }
        return cuisines;
    }

    public void setCuisines(List<Cuisine> cuisines) {
        this.cuisines = cuisines;
    }

    public RestaurantFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(RestaurantFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public PlatMenuFacade getEjbPlatMenuFacade() {
        return ejbPlatMenuFacade;
    }

    public void setEjbPlatMenuFacade(PlatMenuFacade ejbPlatMenuFacade) {
        this.ejbPlatMenuFacade = ejbPlatMenuFacade;
    }

    public List<Plat> getItemsPlat() {
        if (itemsPlat == null) {
            itemsPlat = new ArrayList<>();
        }
        return itemsPlat;
    }

    public void setItemsPlat(List<Plat> itemsPlat) {
        this.itemsPlat = itemsPlat;
    }

    public Ville getVille() {
        if (ville == null) {
            ville = new Ville();
        }
        return ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public Quartier getQuartier() {
        if (quartier == null) {
            quartier = new Quartier();
        }
        return quartier;
    }

    public void setQuartier(Quartier quartier) {
        this.quartier = quartier;
    }

    public Cuisine getCuisine() {
        if (cuisine == null) {
            cuisine = new Cuisine();
        }
        return cuisine;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    public RestaurantController() {
    }

    public Restaurant getSelected() {
        if (selected == null) {
            selected = new Restaurant();
        }
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
