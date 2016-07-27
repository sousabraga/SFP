package br.com.javaparaweb.financeiro.web;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.conta.ContaRN;

@ManagedBean
@RequestScoped
public class ContaBean {

	private Conta selecionada = new Conta();
	
	private List<Conta> lista = null;
	
	@ManagedProperty(value = "#{contextoBean}")
	private ContextoBean contextoBean;
	
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
	
}
