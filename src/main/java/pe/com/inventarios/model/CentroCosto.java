package pe.com.inventarios.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="centro_costo")
public class CentroCosto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String gerencia;
	
	@Column(name = "centrocosto")
	private Long centrocosto;
	
	@Column(name = "codigo_gestion")
	private Long codigoGestion;
	
	@Column(name = "codigo_documentario")
	private String codigoDocumentario;

    @Column(name = "unidad_organica")
	private String unidadOrganica;

	private String denominacion;
	private String destino;
	
	@OneToOne(mappedBy="ceco")
	private Ficha ficha;

	
	public CentroCosto(){}
	
	public CentroCosto(Long codigo_gestion, String codigo_documentario, String unidadOrganica, Long centroCosto,
			String denominacion, String destino) {
		this.codigoGestion = codigo_gestion;
		this.codigoDocumentario = codigo_documentario;
		this.unidadOrganica = unidadOrganica;
		this.centrocosto = centroCosto;
		this.denominacion = denominacion;
		this.destino = destino;
	}
	
	
	public CentroCosto(Long centrocosto, String denominacion) {
		this.centrocosto = centrocosto;
		this.denominacion = denominacion;
	}

	public Long getCodigoGestion() {
		return codigoGestion;
	}
	public void setCodigoGestion(Long codigo_gestion) {
		this.codigoGestion = codigo_gestion;
	}
	public String getCodigoDocumentario() {
		return codigoDocumentario;
	}
	public void setCodigoDocumentario(String codigo_documentario) {
		this.codigoDocumentario = codigo_documentario;
	}
	public String getUnidadOrganica() {
		return unidadOrganica;
	}
	public void setUnidadOrganica(String unidadOrganica) {
		this.unidadOrganica = unidadOrganica;
	}
	public String getDenominacion() {
		return denominacion;
	}
	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}

    public String getGerencia() {
        return gerencia;
    }

    public void setGerencia(String gerencia) {
        this.gerencia = gerencia;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCentrocosto() {
		return centrocosto;
	}

	public void setCentrocosto(Long cencos) {
		this.centrocosto = cencos;
	}

	public Ficha getFicha() {
		return ficha;
	}

	public void setFicha(Ficha ficha) {
		this.ficha = ficha;
	}
}
