package pe.com.inventarios.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Clase implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private Long clase;
	private String denominacion;
	private Integer anexo;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getClase() {
		return clase;
	}
	public void setClase(Long clase) {
		this.clase = clase;
	}
	public String getDenominacion() {
		return denominacion;
	}
	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}
	public Integer getAnexo() {
		return anexo;
	}
	public void setAnexo(Integer anexo) {
		this.anexo = anexo;
	}
	
}
