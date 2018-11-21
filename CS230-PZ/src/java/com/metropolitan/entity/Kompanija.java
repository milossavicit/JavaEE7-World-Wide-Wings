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
@Table(name = "kompanija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Kompanija.findAll", query = "SELECT k FROM Kompanija k")
    , @NamedQuery(name = "Kompanija.findById", query = "SELECT k FROM Kompanija k WHERE k.id = :id")
    , @NamedQuery(name = "Kompanija.findByNazivKompanije", query = "SELECT k FROM Kompanija k WHERE k.nazivKompanije = :nazivKompanije")
    , @NamedQuery(name = "Kompanija.findByDrzava", query = "SELECT k FROM Kompanija k WHERE k.drzava = :drzava")
    , @NamedQuery(name = "Kompanija.findBySediste", query = "SELECT k FROM Kompanija k WHERE k.sediste = :sediste")})
public class Kompanija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "naziv_kompanije")
    private String nazivKompanije;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "drzava")
    private String drzava;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sediste")
    private String sediste;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idKompanije")
    private Collection<Avion> avionCollection;

    public Kompanija() {
    }

    public Kompanija(Integer id) {
        this.id = id;
    }

    public Kompanija(Integer id, String nazivKompanije, String drzava, String sediste) {
        this.id = id;
        this.nazivKompanije = nazivKompanije;
        this.drzava = drzava;
        this.sediste = sediste;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNazivKompanije() {
        return nazivKompanije;
    }

    public void setNazivKompanije(String nazivKompanije) {
        this.nazivKompanije = nazivKompanije;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public String getSediste() {
        return sediste;
    }

    public void setSediste(String sediste) {
        this.sediste = sediste;
    }

    @XmlTransient
    public Collection<Avion> getAvionCollection() {
        return avionCollection;
    }

    public void setAvionCollection(Collection<Avion> avionCollection) {
        this.avionCollection = avionCollection;
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
        if (!(object instanceof Kompanija)) {
            return false;
        }
        Kompanija other = (Kompanija) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nazivKompanije;
    }
    
}
