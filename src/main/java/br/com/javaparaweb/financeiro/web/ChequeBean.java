package br.com.javaparaweb.financeiro.web;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import br.com.javaparaweb.financeiro.cheque.Cheque;
import br.com.javaparaweb.financeiro.cheque.ChequeRN;
import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.util.MensagemUtil;
import br.com.javaparaweb.financeiro.util.RNException;

@ManagedBean
@RequestScoped
public class ChequeBean {

	private Cheque chequeSelecionado = new Cheque();
	
	private List<Cheque> listaCheques = null;
	
	private Integer chequeInicial;
	
	private Integer chequeFinal;
	
	@ManagedProperty(value = "#{contextoBean}")
	private ContextoBean contextoBean;
	
	public void salvar() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		Conta conta = contextoBean.getContaAtiva();
		
		if (chequeInicial == null || chequeFinal == null) {
			String texto = MensagemUtil.getMensagem("cheque_erro_sequencia");
			context.addMessage(null, new FacesMessage(texto));
		} else if (chequeFinal < chequeInicial) {
			String texto = MensagemUtil.getMensagem("cheque_erro_inicial_final", chequeInicial, chequeFinal);
			context.addMessage(null, new FacesMessage(texto));
		} else {
			ChequeRN chequeRN = new ChequeRN();
			
			int totalCheques = chequeRN.salvarSequencia(conta, chequeInicial, chequeFinal);
			
			String texto = MensagemUtil.getMensagem("cheque_info_cadastro", chequeFinal, totalCheques);
			context.addMessage(null, new FacesMessage(texto));
			
			listaCheques = null;
		}
	}
	
	public void excluir() {
		ChequeRN chequeRN = new ChequeRN();
		
		try {
			chequeRN.excluir(chequeSelecionado);
		} catch (RNException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			String texto = MensagemUtil.getMensagem("cheque_erro_excluir");
			
			FacesMessage msg = new FacesMessage(texto);
			msg.setSeverity(FacesMessage.SEVERITY_WARN);
			
			context.addMessage(null, msg);
		}
		
		listaCheques = null;
	}
	
	public void cancelar() {
		ChequeRN chequeRN = new ChequeRN();
		
		try {
			chequeRN.cancelarCheque(chequeSelecionado);
		} catch (RNException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			String texto = MensagemUtil.getMensagem("cheque_erro_cancelar");
			
			FacesMessage msg = new FacesMessage(texto);
			msg.setSeverity(FacesMessage.SEVERITY_WARN);
			
			context.addMessage(null, msg);
		}
		
		listaCheques = null;
	}
	
	public List<Cheque> getListaCheques() {
		if (listaCheques == null) {
			Conta conta = contextoBean.getContaAtiva();
			
			ChequeRN chequeRN = new ChequeRN();
			
			listaCheques = chequeRN.listar(conta);
		}
		
		return listaCheques;
	}

	public Cheque getChequeSelecionado() {
		return chequeSelecionado;
	}

	public void setChequeSelecionado(Cheque chequeSelecionado) {
		this.chequeSelecionado = chequeSelecionado;
	}

	public void setListaCheques(List<Cheque> listaCheques) {
		this.listaCheques = listaCheques;
	}

	public Integer getChequeInicial() {
		return chequeInicial;
	}

	public void setChequeInicial(Integer chequeInicial) {
		this.chequeInicial = chequeInicial;
	}

	public Integer getChequeFinal() {
		return chequeFinal;
	}

	public void setChequeFinal(Integer chequeFinal) {
		this.chequeFinal = chequeFinal;
	}

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}
	
}
