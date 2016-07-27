package br.com.javaparaweb.financeiro.web;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.conta.ContaRN;
import br.com.javaparaweb.financeiro.usuario.Usuario;
import br.com.javaparaweb.financeiro.usuario.UsuarioRN;

@ManagedBean
@SessionScoped
public class ContextoBean implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8138086865266211286L;
	
	private int codigoContaAtiva = 0;
	
	public Usuario getUsuarioLogado() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext external = context.getExternalContext();
		String login = external.getRemoteUser();
		
		if (login != null) {
			UsuarioRN usuarioRN = new UsuarioRN();
			return usuarioRN.buscarPorLogin(login);
		}
		
		return null;
	}
	
	public Conta getContaAtiva() {
		Conta contaAtiva = null;
		
		if (codigoContaAtiva == 0) {
			contaAtiva = getContaAtivaPadrao();
		} else {
			ContaRN contaRN = new ContaRN();
			contaAtiva = contaRN.carregar(codigoContaAtiva);
		}
		
		if (contaAtiva != null) {
			codigoContaAtiva = contaAtiva.getConta();
			return contaAtiva;
		}
		
		return null;
	}
	
	private Conta getContaAtivaPadrao() {
		ContaRN contaRN = new ContaRN();
		Conta contaAtiva = null;
		Usuario usuario = getUsuarioLogado();
		contaAtiva = contaRN.buscarFavorita(usuario);
		
		if (contaAtiva == null) {
			List<Conta> contas = contaRN.listar(usuario);
			
			if (contas != null && contas.size() > 0) {
				contaAtiva = contas.get(0);
			}
		}
		
		return contaAtiva;
	}
	
	public void changeContaAtiva(ValueChangeEvent event) {
		codigoContaAtiva = (Integer) event.getNewValue();
	}
	
}
