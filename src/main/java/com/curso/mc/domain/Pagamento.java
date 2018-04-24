package com.curso.mc.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import enums.EstadoPagamento;

@Entity
public class Pagamento implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idPagamento;
	private EstadoPagamento estado;
	
	@OneToOne
	@JoinColumn(name="fkPedidoId")
	@MapsId
	private Pedido pedido;
	
	public Pagamento() {
		// TODO Auto-generated constructor stub
	}

	public Pagamento(Integer idPagamento, EstadoPagamento estado, Pedido pedido) {
		super();
		this.idPagamento = idPagamento;
		this.estado = estado;
		this.pedido = pedido;
	}

	public Integer getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Integer idPagamento) {
		this.idPagamento = idPagamento;
	}

	public EstadoPagamento getEstado() {
		return estado;
	}

	public void setEstado(EstadoPagamento estado) {
		this.estado = estado;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPagamento == null) ? 0 : idPagamento.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pagamento other = (Pagamento) obj;
		if (idPagamento == null) {
			if (other.idPagamento != null)
				return false;
		} else if (!idPagamento.equals(other.idPagamento))
			return false;
		return true;
	}
}
