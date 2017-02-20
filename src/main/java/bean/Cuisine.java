/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author walid
 */
@Entity
public class Cuisine implements Serializable {

    @ManyToMany(mappedBy = "cuisine")

    private static final long serialVersionUID = 1L;
    @Id
    private String idCuisine;
    @ManyToMany
    private List<Menu> menus;
    @OneToMany(mappedBy = "cuisine")
    private List<Plat> plats;

    public List<Plat> getPlats() {
        return plats;
    }

    public void setPlats(List<Plat> plats) {
        this.plats = plats;
    }
    

    public String getIdCuisine() {
        return idCuisine;
    }

    public void setIdCuisine(String idCuisine) {
        this.idCuisine = idCuisine;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCuisine != null ? idCuisine.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the idCuisine fields are not set
        if (!(object instanceof Cuisine)) {
            return false;
        }
        Cuisine other = (Cuisine) object;
        if ((this.idCuisine == null && other.idCuisine != null) || (this.idCuisine != null && !this.idCuisine.equals(other.idCuisine))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Cuisine[ id=" + idCuisine + " ]";
    }

}
