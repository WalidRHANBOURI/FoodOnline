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
public class Plat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String idPlat;
    private String type;
    private Double prix;
    @ManyToOne
    private Cuisine cuisine;
    @OneToMany(mappedBy = "plat")
    private List<IngredientPlat> ingredientPlats;

    public String getIdPlat() {
        return idPlat;
    }

    public void setIdPlat(String idPlat) {
        this.idPlat = idPlat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    public List<IngredientPlat> getIngredientPlats() {
        return ingredientPlats;
    }

    public void setIngredientPlats(List<IngredientPlat> ingredientPlats) {
        this.ingredientPlats = ingredientPlats;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlat != null ? idPlat.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the idPlat fields are not set
        if (!(object instanceof Plat)) {
            return false;
        }
        Plat other = (Plat) object;
        if ((this.idPlat == null && other.idPlat != null) || (this.idPlat != null && !this.idPlat.equals(other.idPlat))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Plat[ id=" + idPlat + " ]";
    }

}
