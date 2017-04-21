package controler;

import bean.Cmd;
import bean.CmdItem;
import bean.Ingredient;
import bean.User;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.CmdFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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

@Named("cmdController")
@SessionScoped
public class CmdController implements Serializable {

    @EJB
    private service.CmdFacade ejbFacade;
    private List<Cmd> items = null;
    private Cmd selected;
    private List<CmdItem> cmdItems;
    private Date dateCmdDebut;
    private Date dateCmdFin;
    private Double totalMin;
    private Double totalMax;

    
    public void findCmdItemByCmd(Cmd cmd){
       cmdItems =  ejbFacade.findCmdItemByCmd(cmd);
       
    }
    public void search(){
        ejbFacade.search( totalMin, totalMax, dateCmdDebut, dateCmdFin);
    }
    public CmdController() {
    }

    public Cmd getSelected() {
        return selected;
    }

    public void setSelected(Cmd selected) {
        this.selected = selected;
    }

    public List<CmdItem> getCmdItems() {
        if(cmdItems == null){
            cmdItems = new ArrayList<>();
        }
        return cmdItems;
    }

    public void setCmdItems(List<CmdItem> cmdItems) {
        this.cmdItems = cmdItems;
    }

    public Date getDateCmdDebut() {
        return dateCmdDebut;
    }

    public void setDateCmdDebut(Date dateCmdDebut) {
        this.dateCmdDebut = dateCmdDebut;
    }

    public Date getDateCmdFin() {
        return dateCmdFin;
    }

    public void setDateCmdFin(Date dateCmdFin) {
        this.dateCmdFin = dateCmdFin;
    }

    public Double getTotalMin() {
        return totalMin;
    }

    public void setTotalMin(Double totalMin) {
        this.totalMin = totalMin;
    }

    public Double getTotalMax() {
        return totalMax;
    }

    public void setTotalMax(Double totalMax) {
        this.totalMax = totalMax;
    }


    
    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CmdFacade getFacade() {
        return ejbFacade;
    }

    public Cmd prepareCreate() {
        selected = new Cmd();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CmdCreated"));
        if (!JsfUtil.isValidationFailed()) {
            getItems().add(ejbFacade.clone(selected));   // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CmdUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CmdDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Cmd> getItems() {
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

    public Cmd getCmd(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Cmd> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Cmd> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Cmd.class)
    public static class CmdControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CmdController controller = (CmdController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cmdController");
            return controller.getCmd(getKey(value));
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
            if (object instanceof Cmd) {
                Cmd o = (Cmd) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Cmd.class.getName()});
                return null;
            }
        }

    }

}
