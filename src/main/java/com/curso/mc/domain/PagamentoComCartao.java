package com.curso.mc.domain;

import enums.EstadoPagamento;

public class PagamentoComCartao extends Pagamento {
	
	private static final long serialVersionUID = 1L;
	
	private Integer numeroParcelas;
	
	public PagamentoComCartao() {
	}

	public PagamentoComCartao(Integer idPagamento, EstadoPagamento estado, Pedido pedido, Integer numeroParcelas) {
		super(idPagamento, estado, pedido);
		this.numeroParcelas = numeroParcelas;
	}

	public Integer getNumeroParcelas() {
		return numeroParcelas;
	}

	public void setNumeroParcelas(Integer numeroParcelas) {
		this.numeroParcelas = numeroParcelas;
	}
}
