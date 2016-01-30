package pe.com.inventarios.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Ficha implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Long idficha;

    @Column( name = "ape_paterno")
    private String paterno;

    @Column( name = "ape_materno")
    private String materno;

    private String nombres;

    private Long idcodigogestion;
    
    @Transient
    private String nombreCompleto;
    
//    @Column(name ="idceco")
//    private Long idCentroCosto;

    @OneToOne
    @JoinColumn(name="idceco", referencedColumnName="centrocosto")
    private CentroCosto ceco;

    public Long getIdficha() {
        return idficha;
    }

    public void setIdficha(Long idficha) {
        this.idficha = idficha;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Long getIdcodigogestion() {
        return idcodigogestion;
    }

    public void setIdcodigogestion(Long idcodigogestion) {
        this.idcodigogestion = idcodigogestion;
    }

	public CentroCosto getCeco() {
		return ceco;
	}

	public void setCeco(CentroCosto ceco) {
		this.ceco = ceco;
	}

	public String getNombreCompleto() {
		StringBuffer nombre = new StringBuffer("");
		if(null != this.nombres)
			nombre.append(this.nombres + " ");
		if(null != this.paterno)
			nombre.append(this.paterno + " ");
		if(null != this.materno)
			nombre.append(this.materno + " ");
		return nombre.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


}
