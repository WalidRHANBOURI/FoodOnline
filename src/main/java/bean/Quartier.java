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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author walid
 */
@Entity
public class Quartier implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String idQuartier;
    @ManyToOne
    private Ville ville;
    @OneToMany
    private List<Restaurant> restaurant;

    public String getIdQuartier() {
        return idQuartier;
    }

    public void setIdQuartier(String idQuartier) {
        this.idQuartier = idQuartier;
    }

    public List<Restaurant> getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(List<Restaurant> restaurant) {
        this.restaurant = restaurant;
    }

    public Ville getVille() {
        return ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idQuartier != null ? idQuartier.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the idQuartier fields are not set
        if (!(object instanceof Quartier)) {
            return false;
        }
        Quartier other = (Quartier) object;
        if ((this.idQuartier == null && other.idQuartier != null) || (this.idQuartier != null && !this.idQuartier.equals(other.idQuartier))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Quartier[ id=" + idQuartier + " ]";
    }

}
