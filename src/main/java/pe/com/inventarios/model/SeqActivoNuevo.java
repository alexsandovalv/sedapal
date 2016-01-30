package pe.com.inventarios.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="seq_activo_nuevo")
public class SeqActivoNuevo  implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_activo_nuevo")
	private Long id;
	private Long anexo;
	private Long activo_referencia;
	
	public SeqActivoNuevo() {}
	
	public SeqActivoNuevo(Long anexo) {
		this.anexo = anexo;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAnexo() {
		return anexo;
	}
	public void setAnexo(Long anexo) {
		this.anexo = anexo;
	}

	public Long getActivo_referencia() {
		return activo_referencia;
	}

	public void setActivo_referencia(Long activo_referencia) {
		this.activo_referencia = activo_referencia;
	}
	
	
	

}
