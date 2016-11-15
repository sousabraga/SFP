package br.com.javaparaweb.financeiro.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.model.StreamedContent;

import br.com.javaparaweb.financeiro.categoria.Categoria;
import br.com.javaparaweb.financeiro.cheque.Cheque;
import br.com.javaparaweb.financeiro.cheque.ChequeId;
import br.com.javaparaweb.financeiro.cheque.ChequeRN;
import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.lancamento.Lancamento;
import br.com.javaparaweb.financeiro.lancamento.LancamentoRN;
import br.com.javaparaweb.financeiro.util.RNException;
import br.com.javaparaweb.financeiro.util.RelatorioUtil;
import br.com.javaparaweb.financeiro.util.UtilException;

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
	
	private Integer numeroCheque;
	
	private Date dataInicialRelatorio;
	
	private Date dataFinalRelatorio;
	
	private StreamedContent arquivoRetorno;
	
	public LancamentoBean() {
		novo();
	}
	
	public String novo() {
		editado = new Lancamento();
		editado.setData(new Date());
		numeroCheque = null;
		
		return null;
	}
	
	public void editar() {
		Cheque cheque = editado.getCheque();
		
		if (cheque != null) 
			numeroCheque = cheque.getChequeId().getCheque();
	}
	
	public void salvar() {
		editado.setUsuario(contextoBean.getUsuarioLogado());
		editado.setConta(contextoBean.getContaAtiva());
		
		ChequeRN chequeRN = new ChequeRN();
		ChequeId chequeId = null;
		
		if (numeroCheque != null) {
			chequeId = new ChequeId();
			chequeId.setConta(contextoBean.getContaAtiva().getConta());
			chequeId.setCheque(numeroCheque);
			
			Cheque cheque = chequeRN.carregar(chequeId);
			
			FacesContext context = FacesContext.getCurrentInstance();
			
			if (cheque == null) {
				context.addMessage(null, new FacesMessage("Cheque não cadastrado"));
				return;
			} else if (cheque.getSituacao() == Cheque.SITUACAO_CHEQUE_CANCELADO) {
				context.addMessage(null, new FacesMessage("Cheque já cancelado"));
				return;
			} else {
				editado.setCheque(cheque);
				chequeRN.baixarCheque(chequeId, editado);
			}
		}
		
		LancamentoRN lancamentoRN = new LancamentoRN();
		lancamentoRN.salvar(editado);
		
		novo();
		
		lista = null;
	}
	
	public void mudouCheque(ValueChangeEvent event) {
		Integer chequeAnterior = (Integer) event.getOldValue();
		
		if (chequeAnterior != null) {
			ChequeRN chequeRN = new ChequeRN();
			
			try {
				chequeRN.desvinculaLancamento(contextoBean.getContaAtiva(), chequeAnterior);
			} catch (RNException e) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(e.getMessage()));
				return;
			}
		}
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

	public Integer getNumeroCheque() {
		return numeroCheque;
	}

	public void setNumeroCheque(Integer numeroCheque) {
		this.numeroCheque = numeroCheque;
	}

	public Date getDataInicialRelatorio() {
		return dataInicialRelatorio;
	}

	public void setDataInicialRelatorio(Date dataInicialRelatorio) {
		this.dataInicialRelatorio = dataInicialRelatorio;
	}

	public Date getDataFinalRelatorio() {
		return dataFinalRelatorio;
	}

	public void setDataFinalRelatorio(Date dataFinalRelatorio) {
		this.dataFinalRelatorio = dataFinalRelatorio;
	}

	public StreamedContent getArquivoRetorno() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		String usuario = contextoBean.getUsuarioLogado().getLogin();
		
		String nomeRelatorioJasper = "extrato";
		String nomeRelatorioSaida = usuario + "_extrato";
		
		LancamentoRN lancamentoRN = new LancamentoRN();
		
		GregorianCalendar calendario = new GregorianCalendar();
		calendario.setTime(getDataInicialRelatorio());
		calendario.add(Calendar.DAY_OF_MONTH, -1);
		
		Date dataSaldo = new Date(calendario.getTimeInMillis());
		
		RelatorioUtil relatorioUtil = new RelatorioUtil();
		
		HashMap<String, Object> parametrosRelatorio = new HashMap<>();
		parametrosRelatorio.put("codigoUsuario", contextoBean.getUsuarioLogado().getCodigo());
		parametrosRelatorio.put("numeroConta", contextoBean.getContaAtiva().getConta());
		parametrosRelatorio.put("dataInicial", getDataInicialRelatorio());
		parametrosRelatorio.put("dataFinal", getDataFinalRelatorio());
		parametrosRelatorio.put("saldoAnterior", lancamentoRN.saldo(contextoBean.getContaAtiva(), dataSaldo));
		
		try {
			
			arquivoRetorno = relatorioUtil.gerarRelatorio(parametrosRelatorio, nomeRelatorioJasper, 
					nomeRelatorioSaida, RelatorioUtil.RELATORIO_PDF);
					
		} catch (UtilException e) {
			context.addMessage(null, new FacesMessage(e.getMessage()));
			return null;
		}
		
		return arquivoRetorno;
	}

	public void setArquivoRetorno(StreamedContent arquivoRetorno) {
		this.arquivoRetorno = arquivoRetorno;
	}
	
}
