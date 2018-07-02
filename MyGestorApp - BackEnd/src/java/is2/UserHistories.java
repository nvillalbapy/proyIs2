/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is2;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Micaela
 */
@Entity
@Table(name = "user_histories")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserHistories.findAll", query = "SELECT u FROM UserHistories u")
    , @NamedQuery(name = "UserHistories.findByIdUs", query = "SELECT u FROM UserHistories u WHERE u.idUs = :idUs")
    , @NamedQuery(name = "UserHistories.findByTituloUs", query = "SELECT u FROM UserHistories u WHERE u.tituloUs = :tituloUs")
    , @NamedQuery(name = "UserHistories.findByEstado", query = "SELECT u FROM UserHistories u WHERE u.estado = :estado")
    , @NamedQuery(name = "UserHistories.findByFechaInicio", query = "SELECT u FROM UserHistories u WHERE u.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "UserHistories.findByFechaFin", query = "SELECT u FROM UserHistories u WHERE u.fechaFin = :fechaFin")
    , @NamedQuery(name = "UserHistories.findByDescripcionActividad", query = "SELECT u FROM UserHistories u WHERE u.descripcionActividad = :descripcionActividad")
    , @NamedQuery(name = "UserHistories.findByIdUsuario", query = "SELECT u FROM UserHistories u WHERE u.idUsuario = :idUsuario")})
public class UserHistories implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="uh_generator", sequenceName = "seq_user_histories", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uh_generator")
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_us")
    private Integer idUs;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "titulo_us")
    private String tituloUs;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion_actividad")
    private String descripcionActividad;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    public UserHistories() {
    }

    public UserHistories(Integer idUs) {
        this.idUs = idUs;
    }

    public UserHistories(Integer idUs, String tituloUs, String estado, Date fechaInicio, Date fechaFin, String descripcionActividad) {
        this.idUs = idUs;
        this.tituloUs = tituloUs;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcionActividad = descripcionActividad;
    }

    public Integer getIdUs() {
        return idUs;
    }

    public void setIdUs(Integer idUs) {
        this.idUs = idUs;
    }

    public String getTituloUs() {
        return tituloUs;
    }

    public void setTituloUs(String tituloUs) {
        this.tituloUs = tituloUs;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDescripcionActividad() {
        return descripcionActividad;
    }

    public void setDescripcionActividad(String descripcionActividad) {
        this.descripcionActividad = descripcionActividad;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUs != null ? idUs.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserHistories)) {
            return false;
        }
        UserHistories other = (UserHistories) object;
        if ((this.idUs == null && other.idUs != null) || (this.idUs != null && !this.idUs.equals(other.idUs))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "is2.UserHistories[ idUs=" + idUs + " ]";
    }
    
}
