package pe.com.inventarios.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by sandovlu on 06/11/2015.
 */
@Entity
public class Emplazamiento implements Serializable {

    @Id
    private Long codigo;
    
    @Column(name = "centro_servicio")
    private String centroServicio;
    @Column(name = "cs_vinculado")
    private String vinculado;
    
    public Emplazamiento() {
	}
    
    public Emplazamiento(Long codigo, String centroServicio, String ccss_vinculado) {
		this.codigo = codigo;
		this.centroServicio = centroServicio;
		this.vinculado = ccss_vinculado;
	}

	public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getCentroServicio() {
        return centroServicio;
    }

    public void setCentroServicio(String centroServicio) {
        this.centroServicio = centroServicio;
    }

    public String getVinculado() {
        return vinculado;
    }

    public void setVinculado(String ccss_vinculado) {
        this.vinculado = ccss_vinculado;
    }

    @Override
    public String toString() {
        return "Emplazamiento{" +
                "codigo=" + codigo +
                ", centroServicio='" + centroServicio + '\'' +
                ", cs_vinculado='" + vinculado + '\'' +
                '}';
    }
}
