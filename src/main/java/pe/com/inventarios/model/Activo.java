package pe.com.inventarios.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

@Entity(name="inventario")
public class Activo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private Long cuenta_contable;
	private Long clase;
	private String denominacion_clase;
	private Long activo;
	private String nombre_denominacon;
	private String marca;
	private String nro_serie;
	private String modelo;
	private String feccap;
	private Long ceco;
	private Long cecor;
	private Long emplaz;
	private String den_emplaz;
	@Column(name="num_sup")
	private String numSup;
	private String secuencial;
	private String centro_servicio;	
	private String matricula;
	
	private String nro_interno;
	@Column(name="nro_persona")
	private Long nroPersona;
	
	@Column(nullable=true)
	private String vehi_nro_motor;
	private String vehi_anio_fabricacion;
	@Column(nullable=true)
	private String vehi_tipo;
	@Column(nullable=true)
	private String estado_conservacion;
	@Column(nullable=true)
	private String operatividad;
	@Column(nullable=true)
	private String color;
	@Column(nullable=true)
	private String equipo_proyecto;
	private String gerencia;
	@Column(name="ficha_usuario")
	private Long fichaUsuario;
	private String equip_tipo_estructura;
	private String equip_ubigeo;
	private String direccion;
	private String distrito;
	private String equip_nombre_operador;
	private String equip_telefono_operador;
	private String cprc;
	private String xx;
	private String yy;
	private String zz;
	@Column(nullable=true)
	private String habilitacion;
	@Column(nullable=true)
	private String ref_direc_historica;
	private String umb;
	
	private Double cantidad;
	@Column(nullable=true)
	private String centro_costo_denomi;
	@Column(nullable=true)
	private String material;
	@Column(nullable=true)
	private String uso;
	@Column(nullable=true)
	private Double valor_adquisic;
	@Column(nullable=true)
	private Double valor_contable;
	@Column(nullable=true)
	private String item;

	private String username;
	@Column(nullable=true)
	private Date fecha_registro;
	@Column(nullable=true)
	private String username_actualiza;
	@Column(nullable=true)
	private Date fecha_actualiza;
	@Column(nullable=true)
	private Date fecha_inventario;
	
	@Column(name="numero_impresion")
	private String numeroImpresion;
	
	private String chasis;
	@Column(nullable=true)
	private String estado_registro;
	@Column(nullable=true)
	private String observacion;
	@Column(nullable=true)
	private String placa;
	@Column(nullable=true)
	private String seq2015;
	
	private String numero_folio;
	private String posesion;
	
	@Transient
	private Long activo_sedapal;
	
//	@Column(nullable=true)
//	private String seq2013;
	
//	@OneToMany(mappedBy="unActivo", fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST})
	@Transient
	private Set<Foto> fotos;
	
	@Transient
	private CentroCosto centroCosto;
    @Transient
    private Emplazamiento emplazamiento;
    @Transient
    private Ficha ficha;
    
    public Activo() {
		this.centroCosto = new CentroCosto();
		this.emplazamiento = new Emplazamiento();
		this.ficha = new Ficha();
		this.fecha_inventario = new Date();
	}
    
    public Activo(ActivoForm form){
    	this.ceco = form.getCeco();
    	this.centro_costo_denomi = form.getCeco_denominacion();
    	this.den_emplaz = form.getEmpl_denominacion();
    	this.emplaz = form.getEmplazamiento();
    	this.equipo_proyecto = form.getEquipo();
    	this.fecha_inventario = form.getFecha();
    	this.fichaUsuario = form.getFicha();
    	this.gerencia = form.getGerencia();
    	this.fotos = form.getFotos();
    	
    	this.clase = form.getClase();
		this.cuenta_contable = form.getCuenta_contable();
		this.denominacion_clase = form.getDenominacion_clase();
		this.estado_registro = form.getEstado_registro();
		this.numeroImpresion = form.getNro_impresion();
    }
    
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public Long getCuenta_contable() {
		return cuenta_contable;
	}
	public void setCuenta_contable(Long cuenta_contable) {
		this.cuenta_contable = cuenta_contable;
	}
	public Long getClase() {
		return clase;
	}
	public void setClase(Long clase) {
		this.clase = clase;
	}
	public String getDenominacion_clase() {
		return denominacion_clase;
	}
	public void setDenominacion_clase(String denominacion_clase) {
		this.denominacion_clase = denominacion_clase;
	}
	public Long getActivo() {
		return activo;
	}
	public void setActivo(Long activo) {
		this.activo = activo;
		if(null != fotos && !fotos.isEmpty()){
			for(Foto foto : fotos){
				foto.setActivo(activo);
			}
		}
	}
	public String getNombre_denominacon() {
		return nombre_denominacon;
	}
	public void setNombre_denominacon(String nombre_denominacon) {
		this.nombre_denominacon = nombre_denominacon;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getNro_serie() {
		return nro_serie;
	}
	public void setNro_serie(String nro_serie) {
		this.nro_serie = nro_serie;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getFeccap() {
		return feccap;
	}
	public void setFeccap(String feccap) {
		this.feccap = feccap;
	}
	public Long getCeco() {
		return ceco;
	}
	public void setCeco(Long ceco) {
		this.ceco = ceco;
	}
	public Long getCecor() {
		return cecor;
	}
	public void setCecor(Long cecor) {
		this.cecor = cecor;
	}
	public Long getEmplaz() {
		return emplaz;
	}
	public void setEmplaz(Long emplaz) {
		this.emplaz = emplaz;
	}
	public String getDen_emplaz() {
		return den_emplaz;
	}
	public void setDen_emplaz(String den_emplaz) {
		this.den_emplaz = den_emplaz;
	}
	public String getNumSup() {
		return numSup;
	}
	public void setNumSup(String num_sup) {
		this.numSup = num_sup;
	}
	public String getSecuencial() {
		return secuencial;
	}
	public void setSecuencial(String secuencial) {
		this.secuencial = secuencial;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getNro_interno() {
		return nro_interno;
	}
	public void setNro_interno(String nro_interno) {
		this.nro_interno = nro_interno;
	}
	public Long getNroPersona() {
		return nroPersona;
	}
	public void setNroPersona(Long nroPersona) {
		this.nroPersona = nroPersona;
	}
	public String getVehi_nro_motor() {
		return vehi_nro_motor;
	}
	public void setVehi_nro_motor(String vehi_nro_motor) {
		this.vehi_nro_motor = vehi_nro_motor;
	}
	public String getVehi_anio_fabricacion() {
		return vehi_anio_fabricacion;
	}
	public void setVehi_anio_fabricacion(String vehi_anio_fabricacion) {
		this.vehi_anio_fabricacion = vehi_anio_fabricacion;
	}
	public String getVehi_tipo() {
		return vehi_tipo;
	}
	public void setVehi_tipo(String vehi_tipo) {
		this.vehi_tipo = vehi_tipo;
	}
	public String getEstado_conservacion() {
		return estado_conservacion;
	}
	public void setEstado_conservacion(String estado_conservacion) {
		this.estado_conservacion = estado_conservacion;
	}
	public String getOperatividad() {
		return operatividad;
	}
	public void setOperatividad(String operatividad) {
		this.operatividad = operatividad;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getEquipo_proyecto() {
		return equipo_proyecto;
	}
	public void setEquipo_proyecto(String equipo_proyecto) {
		this.equipo_proyecto = equipo_proyecto;
	}
	public String getGerencia() {
		return gerencia;
	}
	public void setGerencia(String gerencia) {
		this.gerencia = gerencia;
	}
	public Long getFichaUsuario() {
		return fichaUsuario;
	}
	public void setFichaUsuario(Long ficha_usuario) {
		this.fichaUsuario = ficha_usuario;
	}
	public String getEquip_tipo_estructura() {
		return equip_tipo_estructura;
	}
	public void setEquip_tipo_estructura(String equip_tipo_estructura) {
		this.equip_tipo_estructura = equip_tipo_estructura;
	}
	public String getEquip_ubigeo() {
		return equip_ubigeo;
	}
	public void setEquip_ubigeo(String equip_ubigeo) {
		this.equip_ubigeo = equip_ubigeo;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getDistrito() {
		return distrito;
	}
	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}
	public String getEquip_nombre_operador() {
		return equip_nombre_operador;
	}
	public void setEquip_nombre_operador(String equip_nombre_operador) {
		this.equip_nombre_operador = equip_nombre_operador;
	}
	public String getEquip_telefono_operador() {
		return equip_telefono_operador;
	}
	public void setEquip_telefono_operador(String equip_telefono_operador) {
		this.equip_telefono_operador = equip_telefono_operador;
	}
	public String getCentro_servicio() {
		return centro_servicio;
	}
	public void setCentro_servicio(String centro_servicio) {
		this.centro_servicio = centro_servicio;
	}
	public String getXx() {
		return xx;
	}
	public void setXx(String xx) {
		this.xx = xx;
	}
	public String getYy() {
		return yy;
	}
	public void setYy(String yy) {
		this.yy = yy;
	}
	public String getZz() {
		return zz;
	}
	public void setZz(String zz) {
		this.zz = zz;
	}
	public String getHabilitacion() {
		return habilitacion;
	}
	public void setHabilitacion(String habilitacion) {
		this.habilitacion = habilitacion;
	}
	public String getRef_direc_historica() {
		return ref_direc_historica;
	}
	public void setRef_direc_historica(String ref_direc_historica) {
		this.ref_direc_historica = ref_direc_historica;
	}
	public String getUmb() {
		return umb;
	}
	public void setUmb(String umb) {
		this.umb = umb;
	}
	public Double getCantidad() {
		return cantidad;
	}
	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}
	public String getCentro_costo_denomi() {
		return centro_costo_denomi;
	}
	public void setCentro_costo_denomi(String centro_costo_denomi) {
		this.centro_costo_denomi = centro_costo_denomi;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getUso() {
		return uso;
	}
	public void setUso(String uso) {
		this.uso = uso;
	}
	public String getCprc() {
		return cprc;
	}
	public void setCprc(String cprc) {
		this.cprc = cprc;
	}
	public Double getValor_adquisic() {
		return valor_adquisic;
	}
	public void setValor_adquisic(Double valor_adquisic) {
		this.valor_adquisic = valor_adquisic;
	}
	public Double getValor_contable() {
		return valor_contable;
	}
	public void setValor_contable(Double valor_contable) {
		this.valor_contable = valor_contable;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSeq2015() {
		return seq2015;
	}
	public void setSeq2015(String seq2015) {
		this.seq2015 = seq2015;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getChasis() {
		return chasis;
	}
	public void setChasis(String chasis) {
		this.chasis = chasis;
	}
	public String getEstado() {
		return estado_registro;
	}
	public void setEstado(String estado) {
		this.estado_registro = estado;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public CentroCosto getCentroCosto() {
		return centroCosto;
	}
	public void setCentroCosto(CentroCosto centroCosto) {
		this.centroCosto = centroCosto;
	}
	public Emplazamiento getEmplazamiento() {
		return emplazamiento;
	}
	public void setEmplazamiento(Emplazamiento emplazamiento) {
		this.emplazamiento = emplazamiento;
	}
	public Ficha getFicha() {
		return ficha;
	}
	public void setFicha(Ficha ficha) {
		this.ficha = ficha;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha_registro() {
		return fecha_registro;
	}

	public void setFecha_registro(Date fecha_registro) {
		this.fecha_registro = fecha_registro;
	}

	public String getUsername_actualiza() {
		return username_actualiza;
	}

	public void setUsername_actualiza(String username_actualiza) {
		this.username_actualiza = username_actualiza;
	}

	public Date getFecha_actualiza() {
		return fecha_actualiza;
	}

	public void setFecha_actualiza(Date fecha_actualiza) {
		this.fecha_actualiza = fecha_actualiza;
	}

	public Date getFecha_inventario() {
		return this.fecha_inventario==null?new Date():this.fecha_inventario;
	}

	public void setFecha_inventario(Date fecha_inventario) {
		this.fecha_inventario = fecha_inventario==null?new Date():fecha_inventario;
	}

	public String getNumeroImpresion() {
		return numeroImpresion;
	}

	public void setNumeroImpresion(String numero_impresion) {
		this.numeroImpresion = numero_impresion;
	}

	public String getEstado_registro() {
		return estado_registro;
	}

	public void setEstado_registro(String estado_registro) {
		this.estado_registro = estado_registro;
	}

	public Set<Foto> getFotos() {
		return fotos;
	}

	public void setFotos(Set<Foto> fotos) {
		this.fotos = fotos;
	}

	public String getNumero_folio() {
		return numero_folio;
	}

	public void setNumero_folio(String numero_folio) {
		this.numero_folio = numero_folio;
	}

	public Long getActivo_sedapal() {
		return activo_sedapal;
	}

	public void setActivo_sedapal(Long activo_sedapal) {
		this.activo_sedapal = activo_sedapal;
	}

	public String getPosesion() {
		return posesion;
	}

	public void setPosesion(String posesion) {
		this.posesion = posesion;
	}

	    
}
