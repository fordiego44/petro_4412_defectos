package pe.com.petroperu.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table
public class Menu extends EntidadBase implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idMenu;
	@Column(nullable = false)
	private String nombre;
	@Column(nullable = true)
	private String enlace;
	@Column(nullable = true)
	private String icono;
	@Column(name = "menu_superior", nullable = true)
	private Long idMenuSuperior;
	@Column(nullable = false)
	private Integer orden;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "menu_rol", joinColumns = {
			@JoinColumn(name = "id_menu", referencedColumnName = "idMenu") }, inverseJoinColumns = {
					@JoinColumn(name = "id_rol", referencedColumnName = "idRol") })
	private List<Rol> roles;
	@Transient
	private List<Menu> subMenu = new ArrayList<>();

	@Transient
	private Integer cantidad;

	public Long getIdMenu() {
		return this.idMenu;
	}

	public void setIdMenu(Long idMenu) {
		this.idMenu = idMenu;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEnlace() {
		return this.enlace;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}

	public String getIcono() {
		return this.icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public List<Rol> getRoles() {
		return this.roles;
	}

	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}

	public Long getIdMenuSuperior() {
		return this.idMenuSuperior;
	}

	public void setIdMenuSuperior(Long idMenuSuperior) {
		this.idMenuSuperior = idMenuSuperior;
	}

	public List<Menu> getSubMenu() {
		return this.subMenu;
	}

	public void setSubMenu(List<Menu> subMenu) {
		this.subMenu = subMenu;
	}

	public Integer getOrden() {
		return this.orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
}
