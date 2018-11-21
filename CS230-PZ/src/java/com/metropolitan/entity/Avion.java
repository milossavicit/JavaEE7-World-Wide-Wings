/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Milos
 */
@Entity
@Table(name = "avion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Avion.findAll", query = "SELECT a FROM Avion a")
    , @NamedQuery(name = "Avion.findById", query = "SELECT a FROM Avion a WHERE a.id = :id")
    , @NamedQuery(name = "Avion.findByModel", query = "SELECT a FROM Avion a WHERE a.model = :model")
    , @NamedQuery(name = "Avion.findByBrMesta", query = "SELECT a FROM Avion a WHERE a.brMesta = :brMesta")
    , @NamedQuery(name = "Avion.findByRegistarskiBr", query = "SELECT a FROM Avion a WHERE a.registarskiBr = :registarskiBr")})
public class Avion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "model")
    private String model;
    @Basic(optional = false)
    @NotNull
    @Column(name = "br_mesta")
    private int brMesta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "registarski_br")
    private String registarskiBr;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "avionId")
    private Collection<Let> letCollection;
    @JoinColumn(name = "id_kompanije", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Kompanija idKompanije;

    public Avion() {
    }

    public Avion(Integer id) {
        this.id = id;
    }

    public Avion(Integer id, String model, int brMesta, String registarskiBr) {
        this.id = id;
        this.model = model;
        this.brMesta = brMesta;
        this.registarskiBr = registarskiBr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getBrMesta() {
        return brMesta;
    }

    public void setBrMesta(int brMesta) {
        this.brMesta = brMesta;
    }

    public String getRegistarskiBr() {
        return registarskiBr;
    }

    public void setRegistarskiBr(String registarskiBr) {
        this.registarskiBr = registarskiBr;
    }

    @XmlTransient
    public Collection<Let> getLetCollection() {
        return letCollection;
    }

    public void setLetCollection(Collection<Let> letCollection) {
        this.letCollection = letCollection;
    }

    public Kompanija getIdKompanije() {
        return idKompanije;
    }

    public void setIdKompanije(Kompanija idKompanije) {
        this.idKompanije = idKompanije;
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
        if (!(object instanceof Avion)) {
            return false;
        }
        Avion other = (Avion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return model;
    }
    
}
