/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Milos
 */
@Entity
@Table(name = "let")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Let.findAll", query = "SELECT l FROM Let l")
    , @NamedQuery(name = "Let.findById", query = "SELECT l FROM Let l WHERE l.id = :id")
    , @NamedQuery(name = "Let.findByVremePolaska", query = "SELECT l FROM Let l WHERE l.vremePolaska = :vremePolaska")
    , @NamedQuery(name = "Let.findByVremeDolaska", query = "SELECT l FROM Let l WHERE l.vremeDolaska = :vremeDolaska")})
public class Let implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vreme_polaska")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vremePolaska;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vreme_dolaska")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vremeDolaska;
    @JoinColumn(name = "aerodrom_id_dolazak", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Aerodrom aerodromIdDolazak;
    @JoinColumn(name = "aerodrom_id_polazak", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Aerodrom aerodromIdPolazak;
    @JoinColumn(name = "avion_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Avion avionId;
    @JoinColumn(name = "pilot", referencedColumnName = "username")
    @ManyToOne
    private Users pilot;

    public Let() {
    }

    public Let(Integer id) {
        this.id = id;
    }

    public Let(Integer id, Date vremePolaska, Date vremeDolaska) {
        this.id = id;
        this.vremePolaska = vremePolaska;
        this.vremeDolaska = vremeDolaska;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getVremePolaska() {
        return vremePolaska;
    }

    public void setVremePolaska(Date vremePolaska) {
        this.vremePolaska = vremePolaska;
    }

    public Date getVremeDolaska() {
        return vremeDolaska;
    }

    public void setVremeDolaska(Date vremeDolaska) {
        this.vremeDolaska = vremeDolaska;
    }

    public Aerodrom getAerodromIdDolazak() {
        return aerodromIdDolazak;
    }

    public void setAerodromIdDolazak(Aerodrom aerodromIdDolazak) {
        this.aerodromIdDolazak = aerodromIdDolazak;
    }

    public Aerodrom getAerodromIdPolazak() {
        return aerodromIdPolazak;
    }

    public void setAerodromIdPolazak(Aerodrom aerodromIdPolazak) {
        this.aerodromIdPolazak = aerodromIdPolazak;
    }

    public Avion getAvionId() {
        return avionId;
    }

    public void setAvionId(Avion avionId) {
        this.avionId = avionId;
    }

    public Users getPilot() {
        return pilot;
    }

    public void setPilot(Users pilot) {
        this.pilot = pilot;
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
        if (!(object instanceof Let)) {
            return false;
        }
        Let other = (Let) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.metropolitan.entity.Let[ id=" + id + " ]";
    }
    
}
