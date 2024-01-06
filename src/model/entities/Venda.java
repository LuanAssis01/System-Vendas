package model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.services.ServicoPagamento;

public class Venda {
	private Integer numero;
	private LocalDate data;
	private Double desconto;
	private Integer parcelas;
	private Cliente cliente;
	private ServicoPagamento sp;
	private Pago pago;
	private TipoPagamento tp;
	private LocalDate dataPagamento;
	private List<String> descricaoParcelas = new ArrayList<>();
	private List<ItemVenda> itemvenda = new ArrayList<>();

	public Venda() {

	}

	public Venda(Integer numero, LocalDate data, Double desconto, Integer parcelas, Cliente cliente,
			ServicoPagamento sp, Pago pago, TipoPagamento tp) {
		this.numero = numero;
		this.data = data;
		this.desconto = 0.0;
		this.parcelas = 0;
		this.cliente = cliente;
		this.sp = sp;
		this.pago = pago;
		this.tp = tp;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Integer getParcelas() {
		return parcelas;
	}

	public void setParcelas(Integer parcelas) {
		this.parcelas = parcelas;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ServicoPagamento getSp() {
		return sp;
	}

	public void setSp(ServicoPagamento sp) {
		this.sp = sp;
	}

	public Pago getPago() {
		return pago;
	}

	public void setPago(Pago pago) {
		this.pago = pago;
	}

	public TipoPagamento getTp() {
		return tp;
	}

	public void setTp(TipoPagamento tp) {
		this.tp = tp;
	}

	public void adicionarDescricaoParcela(String descricao) {
		descricaoParcelas.add(descricao);
	}

	public List<String> getDescricaoParcelas() {
		return descricaoParcelas;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	
	public void addItem(ItemVenda itemVenda) {
		if (!itemvenda.contains(itemVenda)) {
			itemvenda.add(itemVenda);
		}
	}

	public void removerItem(ItemVenda itemParaRemover) {

		List<ItemVenda> copiaItemVenda = new ArrayList<>(itemvenda);

		for (ItemVenda itemVenda : copiaItemVenda) {
			if (itemVenda.equals(itemParaRemover)) {
				itemvenda.remove(itemVenda);
				return;
			}
		}

		System.out.println("Item não encontrado na venda.");
	}

	public Double calcularDesconto(Double value) {
	    if (desconto == null) {
	        return 0.0;
	    }
	    return value * (desconto / 100.0);
	}

	public Double totalSemDesconto() {
		Double calculoTotal = 0.0;
		for (ItemVenda iv : itemvenda) {
			calculoTotal += iv.subTotal();
		}
		return calculoTotal;
	}

	public String recibo() {
		StringBuilder recibo = new StringBuilder();
		Double total = totalSemDesconto();

		for (ItemVenda iv : itemvenda) {
			recibo.append(iv.getNumero()).append(" ").append(iv.getNome()).append(" ").append(iv.getQuantidade())
					.append(" ").append(iv.getValor()).append(" ").append(iv.subTotal()).append("\n");
		}

		recibo.append("Total: ").append(total);

		return recibo.toString();
	}

	public void realizarPagamento(Pago pago, TipoPagamento tipoPagamento, ServicoPagamento servicoPagamento) {
		if (itemvenda.isEmpty()) {
			System.out.println("O carrinho está vazio. Adicione itens primeiro.");
			return;
		}

		setPago(pago);
		setTp(tipoPagamento);
		setSp(servicoPagamento);

		double totalComDesconto = totalSemDesconto() - calcularDesconto(totalSemDesconto());

		System.out.println("Recibo da Compra:");
		System.out.println(recibo());
		System.out.printf("Total com desconto: %.2f%n", totalComDesconto);
		System.out.println();

		itemvenda.clear();

	}

	public List<ItemVenda> getItemvenda() {
		return itemvenda;
	}

	public double subTotal() {
	    if (!getItemvenda().isEmpty() && getParcelas() != null && getParcelas() > 0) {
	        
	        return totalSemDesconto();
	    } else {
	       
	        return totalSemDesconto() - calcularDesconto(totalSemDesconto());
	    }
	}
}
