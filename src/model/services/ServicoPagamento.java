package model.services;

import model.entities.Venda;

public class ServicoPagamento {
    private FormaPagamento formaPagamento;

    public ServicoPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public void processaPagamento(Venda venda) {
        if (venda == null || venda.getItemvenda().isEmpty()) {
            throw new IllegalArgumentException("Venda não pode ser processada sem itens.");
        }

        if (venda.getDesconto() == null) {
            venda.setDesconto(0.0);
        }

        Double valorBase = venda.totalSemDesconto() / venda.getParcelas();

        for (int i = 1; i <= venda.getParcelas(); i++) {
            Double valorComJuros = formaPagamento.calcularPagamento(valorBase, i);
            Double valorTotal = valorComJuros + formaPagamento.taxa(valorComJuros);
            venda.adicionarDescricaoParcela("Parcela #" + i + ": " + valorTotal);
        }

        System.out.println("Pagamento processado com sucesso. Detalhes na descrição de parcelas.");
    }
}
