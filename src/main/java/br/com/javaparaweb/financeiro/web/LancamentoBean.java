package br.com.javaparaweb.financeiro.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.javaparaweb.financeiro.categoria.Categoria;
import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.lancamento.Lancamento;
import br.com.javaparaweb.financeiro.lancamento.LancamentoRN;

@ManagedBean
@ViewScoped
public class LancamentoBean implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3792950636875297407L;

	private List<Lancamento> lista;
	
	private Conta conta;
	
	private List<Double> saldos;
	
	private float saldoGeral;
	
	private Lancamento editado = new Lancamento();
	
	@ManagedProperty(value = "#{contextoBean}")
	private ContextoBean contextoBean;
	
	public LancamentoBean() {
		novo();
	}
	
	public String novo() {
		editado = new Lancamento();
		editado.setData(new Date());
		
		return null;
	}
	
	public void editar() {
		// TODO
	}
	
	public void salvar() {
		editado.setUsuario(contextoBean.getUsuarioLogado());
		editado.setConta(contextoBean.getContaAtiva());
		
		LancamentoRN lancamentoRN = new LancamentoRN();
		lancamentoRN.salvar(editado);
		
		novo();
		
		lista = null;
	}
	
	public void excluir() {
		LancamentoRN lancamentoRN = new LancamentoRN();
		lancamentoRN.excluir(editado);
		
		lista = null;
	}
	
	public List<Lancamento> getLista() {
		if (lista == null || conta != contextoBean.getContaAtiva()) {
			conta = contextoBean.getContaAtiva();
			
			Calendar dataSaldo = new GregorianCalendar();
			dataSaldo.add(Calendar.MONTH, -1);
			dataSaldo.add(Calendar.DAY_OF_MONTH, -1);
			
			Calendar inicio = new GregorianCalendar();
			inicio.add(Calendar.MONTH, -1);
			
			LancamentoRN lancamentoRN = new LancamentoRN();
			
			saldoGeral = lancamentoRN.saldo(conta, dataSaldo.getTime());
			
			lista = lancamentoRN.listar(conta, inicio.getTime(), null);
			
			Categoria categoria = null;
			
			double saldo = saldoGeral;
			
			saldos = new ArrayList<>();
			
			for (Lancamento lancamento : lista) {
				categoria = lancamento.getCategoria();
				saldo += lancamento.getValor().floatValue() * categoria.getFator();
				saldos.add(saldo);
			}

		}
		
		return lista;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public List<Double> getSaldos() {
		return saldos;
	}

	public void setSaldos(List<Double> saldos) {
		this.saldos = saldos;
	}

	public float getSaldoGeral() {
		return saldoGeral;
	}

	public void setSaldoGeral(float saldoGeral) {
		this.saldoGeral = saldoGeral;
	}

	public Lancamento getEditado() {
		return editado;
	}

	public void setEditado(Lancamento editado) {
		this.editado = editado;
	}

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}

	public void setLista(List<Lancamento> lista) {
		this.lista = lista;
	}
	
}
