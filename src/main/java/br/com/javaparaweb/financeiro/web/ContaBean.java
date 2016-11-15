package br.com.javaparaweb.financeiro.web;

import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.StreamedContent;

import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.conta.ContaRN;
import br.com.javaparaweb.financeiro.util.RelatorioUtil;
import br.com.javaparaweb.financeiro.util.UtilException;

@ManagedBean
@RequestScoped
public class ContaBean {

	private Conta selecionada = new Conta();
	
	private List<Conta> lista = null;
	
	@ManagedProperty(value = "#{contextoBean}")
	private ContextoBean contextoBean;
	
	private StreamedContent arquivoRetorno;
	
	private int tipoRelatorio;
	
	public String salvar() {
		selecionada.setUsuario(contextoBean.getUsuarioLogado());
		ContaRN contaRN = new ContaRN();
		contaRN.salvar(selecionada);
		selecionada = new Conta();
		lista = null;
		return null;
	}
	
	public String excluir() {
		ContaRN contaRN = new ContaRN();
		contaRN.excluir(selecionada);
		selecionada = new Conta();
		lista = null;
		return null;
	}
	
	public String tornarFavorita() {
		ContaRN contaRN = new ContaRN();
		contaRN.tornarFavorita(selecionada);
		selecionada = new Conta();
		return null;
	}

	public Conta getSelecionada() {
		return selecionada;
	}

	public void setSelecionada(Conta selecionada) {
		this.selecionada = selecionada;
	}

	public List<Conta> getLista() {
		if (lista == null) {
			ContaRN contaRN = new ContaRN();
			lista = contaRN.listar(contextoBean.getUsuarioLogado());
		}
		
		return lista;
	}

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}
	
	public StreamedContent getArquivoRetorno() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		String usuario = contextoBean.getUsuarioLogado().getLogin();
		String nomeRelatorioJasper = "contas";
		String nomeRelatorioSaida = usuario + "_contas";
		
		RelatorioUtil relatorioUtil = new RelatorioUtil();
		
		HashMap<String, Object> parametrosRelatorio = new HashMap<>();
		parametrosRelatorio.put("codigoUsuario", contextoBean.getUsuarioLogado().getCodigo());
		
		try {
			
			arquivoRetorno = relatorioUtil.gerarRelatorio(parametrosRelatorio, nomeRelatorioJasper, nomeRelatorioSaida, tipoRelatorio);
		
		} catch (UtilException e) {
			context.addMessage(null, new FacesMessage(e.getMessage()));
			return null;
		}
		
		return arquivoRetorno;
	}
	
	public int getTipoRelatorio() {
		return tipoRelatorio;
	}
	
	public void setTipoRelatorio(int tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}
	
}
