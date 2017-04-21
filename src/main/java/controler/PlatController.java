package controler;

import bean.CmdItem;
import bean.Cuisine;
import bean.IngredientPlat;
import static bean.Menu_.restaurant;
import bean.Plat;
import bean.Quartier;
import bean.Restaurant;
import bean.User;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import service.PlatFacade;

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
import org.primefaces.context.RequestContext;
import service.CuisineFacade;
import service.IngredientPlatFacade;

@Named("platController")
@SessionScoped
public class PlatController implements Serializable {

    @EJB
    private service.PlatFacade ejbFacade;
    @EJB
    private service.IngredientPlatFacade ingredientPlatFacade;
    @EJB
    private service.CuisineFacade cuisineFacade;
    @EJB
    private service.PlatMenuFacade platMenuFacade;
    @EJB
    private service.CmdItemFacade cmdItemFacade;
    @EJB
    private service.CmdFacade cmdFacade;
    @EJB
    private service.QuartierFacade quartierFacade;
    private controler.RestaurantController restaurantController;
    private List<Plat> items = null;
    private Plat selected;
    private CmdItem cmdItem;
    private Double prix;
    private Double total;
    private List<IngredientPlat> ingredientPlats;
    private List<IngredientPlat> ingredientChoisis;
    private List<CmdItem> panier;
    private List<Cuisine> cuisines;
    private Cuisine cuisine ;
    private CmdItem selectedCmdItem;
    private Double prixTotal;
    private int qte ; 
    private String adresseLivraison;
    private List<Quartier> quartiers;
    private Restaurant restaurant =(Restaurant) SessionUtil.getAttribute("anaResto");
//    private User client = SessionUtil.getConnectedUser();
    private User client = (User)SessionUtil.getAttribute("clt");
    

    
    
    public void findPlatByCuisine(){
        if(cuisine == null){
          
            System.out.println(restaurant);
            items= platMenuFacade.findPlatByResto(restaurant);
        }
        else{
        System.out.println("ha l cuisine li bghit"+cuisine);
       items = ejbFacade.findPlatByCuisine(cuisine);
    }
    }
  
      public void findIngredientByPlat(Plat item){
          selected = item;
          System.out.println(selected);
          if(selected.getType().equalsIgnoreCase("personnalise")){
          ingredientPlats = ingredientPlatFacade.findIngredientByPlat(selected);
              RequestContext.getCurrentInstance().update("choixSupp");
              RequestContext.getCurrentInstance().execute("PF('ChoixSuppDialog').show()");
          }
          else{
              remplirPanier();
              RequestContext.getCurrentInstance().update("panier:cmdItemPanier");
          }
  
    }
   
    public void prixTotalIngredient(){
        System.out.println(ingredientChoisis);
        prix = ingredientPlatFacade.prixTotalIngredient(ingredientChoisis);
      
    }
    
    public void total(){
        total = ingredientPlatFacade.total(selected, ingredientChoisis);

    }
    public void remplirPanier(){
        System.out.println("hahwa resto li fih had l plat "+restaurant);
      CmdItem cmdItem =  cmdItemFacade.remplirCmdItem(selected, ingredientChoisis,restaurant);
        System.out.println(cmdItem);
      panier.add(cmdItem);
      prixTotal = cmdItemFacade.totalDesCmdItem(panier);
    }
    public void removeCmdItem(CmdItem cmdItem){
        selectedCmdItem = cmdItem;
        panier.remove(selectedCmdItem);
        prixTotal = prixTotal - selectedCmdItem.getPrix();
        System.out.println(panier);
    }
    public void updateQteAndPrice(CmdItem selectCmdItem){
        System.out.println("hahia cmdItem selectionne "+selectCmdItem);
        System.out.println(qte);
        cmdItemFacade.updateQteAndPrice(selectCmdItem, selectCmdItem.getQuantite());
         prixTotal = cmdItemFacade.totalDesCmdItem(panier);
         qte = 1;
    }
    public void saveCmd(){
        if(client == null){
            JsfUtil.addErrorMessage("Vous devez vous connecter pour passer la commande");
        }
        System.out.println("hahwa total dial cmd"+prixTotal);
        System.out.println(panier);
        System.out.println("hahia adresse "+adresseLivraison);
        cmdFacade.saveCmd(panier, prixTotal,adresseLivraison,client);
    }
    public void prepareCmd(){
        if(restaurant != null)
     quartiers = quartierFacade.findQuartierByVille(restaurant.getQuartier().getVille());
    }
    public void mince(CmdItem cmdItem){
        cmdItem.setQuantite(cmdItem.getQuantite()-1);
        updateQteAndPrice(cmdItem);
    }
    public void plus(CmdItem cmdItem){
        cmdItem.setQuantite(cmdItem.getQuantite() + 1);
        updateQteAndPrice(cmdItem);
    }
    public Cuisine getCuisine() {
        return cuisine;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    public User getClient() {
        if(client == null){
            client = new User();
        }
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }
    
    public CuisineFacade getCuisineFacade() {
        return cuisineFacade;
    }

    public void setCuisineFacade(CuisineFacade cuisineFacade) {
        this.cuisineFacade = cuisineFacade;
    }

    public List<Cuisine> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<Cuisine> cuisines) {
        this.cuisines = cuisines;
    }
    
    public CmdItem getCmdItem() {
        if(cmdItem == null){
            cmdItem = new CmdItem();
        }
        return cmdItem;
    }

    public void setCmdItem(CmdItem cmdItem) {
        this.cmdItem = cmdItem;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
   
    public List<CmdItem> getPanier() {
        if(panier ==null){
            panier = new ArrayList<>();
        }
        return panier;
    }

    public void setPanier(List<CmdItem> panier) {
        this.panier = panier;
    }

    public List<Quartier> getQuartiers() {
        if(quartiers == null){
            quartiers = new ArrayList<>();
        }
        return quartiers;
    }

    public void setQuartiers(List<Quartier> quartiers) {
        if(quartiers == null){
            quartiers = new ArrayList<Quartier>();
        }
        this.quartiers = quartiers;
    }
    
    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
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

    public CmdItem getSelectedCmdItem() {
        if(selectedCmdItem == null){
            selectedCmdItem = new CmdItem();
        }
        return selectedCmdItem;
    }

    public void setSelectedCmdItem(CmdItem selectedCmdItem) {
        this.selectedCmdItem = selectedCmdItem;
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

    public Double getPrixTotal() {

        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public String getAdresseLivraison() {
        return adresseLivraison;
    }

    public void setAdresseLivraison(String adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
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
