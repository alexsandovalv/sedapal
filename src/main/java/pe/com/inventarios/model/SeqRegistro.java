package pe.com.inventarios.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="seq_registro")
public class SeqRegistro implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_registro")
	private Long id;
	private Long anexo;
	
	public SeqRegistro() {}
	
	public SeqRegistro(Long anexo) {
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

}
