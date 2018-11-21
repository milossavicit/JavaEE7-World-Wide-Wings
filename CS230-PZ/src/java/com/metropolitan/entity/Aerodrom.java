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
@Table(name = "aerodrom")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aerodrom.findAll", query = "SELECT a FROM Aerodrom a")
    , @NamedQuery(name = "Aerodrom.findById", query = "SELECT a FROM Aerodrom a WHERE a.id = :id")
    , @NamedQuery(name = "Aerodrom.findByGrad", query = "SELECT a FROM Aerodrom a WHERE a.grad = :grad")
    , @NamedQuery(name = "Aerodrom.findByDrzava", query = "SELECT a FROM Aerodrom a WHERE a.drzava = :drzava")
    , @NamedQuery(name = "Aerodrom.findByNaziv", query = "SELECT a FROM Aerodrom a WHERE a.naziv = :naziv")})
public class Aerodrom implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "grad")
    private String grad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "drzava")
    private String drzava;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "naziv")
    private String naziv;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aerodromIdDolazak")
    private Collection<Let> letCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aerodromIdPolazak")
    private Collection<Let> letCollection1;

    public Aerodrom() {
    }

    public Aerodrom(Integer id) {
        this.id = id;
    }

    public Aerodrom(Integer id, String grad, String drzava, String naziv) {
        this.id = id;
        this.grad = grad;
        this.drzava = drzava;
        this.naziv = naziv;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @XmlTransient
    public Collection<Let> getLetCollection() {
        return letCollection;
    }

    public void setLetCollection(Collection<Let> letCollection) {
        this.letCollection = letCollection;
    }

    @XmlTransient
    public Collection<Let> getLetCollection1() {
        return letCollection1;
    }

    public void setLetCollection1(Collection<Let> letCollection1) {
        this.letCollection1 = letCollection1;
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
        if (!(object instanceof Aerodrom)) {
            return false;
        }
        Aerodrom other = (Aerodrom) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return naziv;
    }
    
}
