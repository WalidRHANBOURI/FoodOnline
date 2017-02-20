/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author walid
 */
@Entity
public class Ville implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String idVille;
    @OneToMany
    private List<Quartier> quartier;

    public String getIdVille() {
        return idVille;
    }

    public void setIdVille(String idVille) {
        this.idVille = idVille;
    }

    public List<Quartier> getQuartier() {
        return quartier;
    }

    public void setQuartier(List<Quartier> quartier) {
        this.quartier = quartier;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVille != null ? idVille.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the idVille fields are not set
        if (!(object instanceof Ville)) {
            return false;
        }
        Ville other = (Ville) object;
        if ((this.idVille == null && other.idVille != null) || (this.idVille != null && !this.idVille.equals(other.idVille))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Ville[ id=" + idVille + " ]";
    }

}
