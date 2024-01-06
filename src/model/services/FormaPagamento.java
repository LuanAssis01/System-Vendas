package model.services;

public interface FormaPagamento {
	Double calcularPagamento(Double amount, Integer parcela);
	Double taxa(Double amount);
}	
