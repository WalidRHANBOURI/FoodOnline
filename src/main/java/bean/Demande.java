/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


/**
 *
 * @author HP
 */
@Entity
public class Demande implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nomResto;
    private int nbrEtoile;
    private String description;
    private String telResto;
    @ManyToOne
    private Ville villeResto;
    @ManyToOne
    private Quartier quartierResto;
    private String adresseResto;
    @OneToOne
    private User responsable ;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomResto() {
        return nomResto;
    }

    public void setNomResto(String nomResto) {
        this.nomResto = nomResto;
    }


    public String getTelResto() {
        return telResto;
    }

    public void setTelResto(String telResto) {
        this.telResto = telResto;
    }

    public String getAdresseResto() {
        return adresseResto;
    }

    public void setAdresseResto(String adresseResto) {
        this.adresseResto = adresseResto;
    }

    public User getResponsable() {
        return responsable;
    }

    public void setResponsable(User responsable) {
        this.responsable = responsable;
    }
    
    public int getNbrEtoile() {
        return nbrEtoile;
    }

    public void setNbrEtoile(int nbrEtoile) {
        this.nbrEtoile = nbrEtoile;
    }




    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Ville getVilleResto() {
        return villeResto;
    }

    public void setVilleResto(Ville villeResto) {
        this.villeResto = villeResto;
    }

    public Quartier getQuartierResto() {
        return quartierResto;
    }

    public void setQuartierResto(Quartier quartierResto) {
        this.quartierResto = quartierResto;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Demande)) {
            return false;
        }
        Demande other = (Demande) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Demande[ id=" + id + " ]";
    }
    
}
