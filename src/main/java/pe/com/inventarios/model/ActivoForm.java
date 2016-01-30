package pe.com.inventarios.model;

import java.util.Date;
import java.util.Set;

public class ActivoForm {
	
	private Date fecha;
	private Long ceco;
	private String ceco_denominacion;
	private	String equipo;
	private String gerencia;
	private String nombres;
	private Long ficha;
	private Long emplazamiento;
	private String empl_denominacion;
	private Long clase;
	private String denominacion_clase;
	private Long cuenta_contable;
	private String estado_registro;
	private Set<Foto> fotos;
	private String process = "update";
	private String nro_impresion;
	private String usuaCreacion;
	
	public ActivoForm(){}
	
	public ActivoForm(String process){
		this.process = process;
		this.nro_impresion = "";
	}
	
	public ActivoForm(Date fecha, Long ceco, String ceco_denominacion, String equipo, String gerencia, String nombres,
			Long ficha, Long emplazamiento, String empl_denominacion, String denominacion_clase) {
		this.fecha = fecha;
		this.ceco = ceco;
		this.ceco_denominacion = ceco_denominacion;
		this.equipo = equipo;
		this.gerencia = gerencia;
		this.nombres = nombres;
		this.ficha = ficha;
		this.emplazamiento = emplazamiento;
		this.empl_denominacion = empl_denominacion;
		this.denominacion_clase = denominacion_clase;
	}
	
	public ActivoForm(Activo activo){
		this.fecha = activo.getFecha_inventario();
		this.ceco = activo.getCeco();
		this.ceco_denominacion = activo.getCentro_costo_denomi();
		this.equipo = activo.getEquipo_proyecto();
		this.gerencia = activo.getGerencia();
		this.nombres = activo.getFicha().getNombres();
		this.ficha = activo.getFichaUsuario();
		this.emplazamiento = activo.getEmplaz();
		this.empl_denominacion = activo.getDen_emplaz();
		
		this.clase = activo.getClase();
		this.cuenta_contable = activo.getCuenta_contable();
		this.denominacion_clase = activo.getDenominacion_clase();
		this.estado_registro = activo.getEstado_registro();
		this.nro_impresion = activo.getNumeroImpresion();
		this.usuaCreacion = activo.getUsername();
	}
	
	public ActivoForm(Activo activo, String fullname){
		this(activo);
		this.nombres = fullname;				
	}
	
	public ActivoForm(Ficha ficha){
		this.fecha = new Date();
		this.nombres = ficha.getNombreCompleto();
		this.ficha = ficha.getIdficha();
		this.ceco = ficha.getCeco().getCentrocosto();
		this.ceco_denominacion = ficha.getCeco().getDenominacion();
		this.equipo = ficha.getCeco().getCodigoDocumentario();
		this.gerencia = ficha.getCeco().getGerencia();
		this.estado_registro = "";
		this.nro_impresion = "";
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Long getCeco() {
		return ceco;
	}
	public void setCeco(Long ceco) {
		this.ceco = ceco;
	}
	public String getCeco_denominacion() {
		return ceco_denominacion;
	}
	public void setCeco_denominacion(String ceco_denominacion) {
		this.ceco_denominacion = ceco_denominacion;
	}
	public String getEquipo() {
		return equipo;
	}
	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}
	public String getGerencia() {
		return gerencia;
	}
	public void setGerencia(String gerencia) {
		this.gerencia = gerencia;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public Long getFicha() {
		return ficha;
	}
	public void setFicha(Long ficha) {
		this.ficha = ficha;
	}
	public Long getEmplazamiento() {
		return emplazamiento;
	}
	public void setEmplazamiento(Long emplazamiento) {
		this.emplazamiento = emplazamiento;
	}
	public String getEmpl_denominacion() {
		return empl_denominacion;
	}
	public void setEmpl_denominacion(String empl_denominacion) {
		this.empl_denominacion = empl_denominacion;
	}

	public Set<Foto> getFotos() {
		return fotos;
	}

	public void setFotos(Set<Foto> fotos) {
		this.fotos = fotos;
	}

	public Long getClase() {
		return clase;
	}

	public void setClase(Long clase) {
		this.clase = clase;
	}

	public Long getCuenta_contable() {
		return cuenta_contable;
	}

	public void setCuenta_contable(Long cuenta_contable) {
		this.cuenta_contable = cuenta_contable;
	}

	public String getEstado_registro() {
		return estado_registro;
	}

	public void setEstado_registro(String estado_registro) {
		this.estado_registro = estado_registro;
	}

	public String getDenominacion_clase() {
		return denominacion_clase;
	}

	public void setDenominacion_clase(String denominacion_clase) {
		this.denominacion_clase = denominacion_clase;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getNro_impresion() {
		return nro_impresion;
	}

	public void setNro_impresion(String nro_impresion) {
		this.nro_impresion = nro_impresion;
	}

	public String getUsuaCreacion() {
		return usuaCreacion;
	}

	public void setUsuaCreacion(String usuaCreacion) {
		this.usuaCreacion = usuaCreacion;
	}
	

}
