package pe.com.inventarios.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Foto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idfoto")
	private Long id;
	
	@Column(name="activo", nullable=false)
	private Long activo;
	
	private String path;
	private String secuence;
	
	
//	@ManyToOne(optional=false, fetch = FetchType.LAZY)
//	@JoinColumn(name="activo", referencedColumnName="activo")
//	private Activo unActivo;
	
	@Lob
	@Column(nullable=true, columnDefinition="longblob")
	private byte[] foto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public Long getActivo() {
		return activo;
	}

	public void setActivo(Long activo) {
		this.activo = activo;
	}
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSecuence() {
		return secuence;
	}

	public void setSecuence(String secuence) {
		this.secuence = secuence;
	}

	@Override
	public String toString() {
		return "Foto [id=" + id + ", activo=" + activo + ", path=" + path + ", secuence=" + secuence + "]";
	}


}
