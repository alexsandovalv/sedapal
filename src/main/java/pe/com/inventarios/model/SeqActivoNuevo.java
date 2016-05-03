package pe.com.inventarios.model;

import java.io.Serializable;
import java.util.Date;

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
	private Date fecha_registro;
	private String usuario_registro;
	
	public SeqActivoNuevo() {}
	
	public SeqActivoNuevo(Long anexo, String usuario_registro) {
		this.anexo = anexo;
		this.fecha_registro = new Date();
		this.usuario_registro = usuario_registro;
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

	public Date getFecha_registro() {
		return fecha_registro;
	}

	public void setFecha_registro(Date fecha_registro) {
		this.fecha_registro = fecha_registro;
	}

	public String getUsuario_registro() {
		return usuario_registro;
	}

	public void setUsuario_registro(String usuario_registro) {
		this.usuario_registro = usuario_registro;
	}
	
	
	

}
