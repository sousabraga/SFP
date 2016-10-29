package br.com.javaparaweb.financeiro.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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
	
	private List<Locale> idiomas;
	
	public Usuario getUsuarioLogado() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext external = context.getExternalContext();
		String login = external.getRemoteUser();
		
		if (login != null) {
			UsuarioRN usuarioRN = new UsuarioRN();
			Usuario usuario = usuarioRN.buscarPorLogin(login);
			String[] info = usuario.getIdioma().split("_");
			Locale locale = new Locale(info[0], info[1]);
			context.getViewRoot().setLocale(locale);
			return usuario;
		}
		
		return null;
	}
	
	public List<Locale> getIdiomas() {
		FacesContext context = FacesContext.getCurrentInstance();
		Iterator<Locale> locales = context.getApplication().getSupportedLocales();
		idiomas = new ArrayList<>();
		
		while (locales.hasNext()) {
			idiomas.add(locales.next());
		}
		
		return idiomas;
	}
	
	public void setIdiomaUsuario(String idioma) {
		Usuario usuario = getUsuarioLogado();
		usuario.setIdioma(idioma);
		
		UsuarioRN usuarioRN = new UsuarioRN();
		usuarioRN.salvar(usuario);
		
		String[] info = idioma.split("_");
		Locale locale = new Locale(info[0], info[1]);
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.getViewRoot().setLocale(locale);
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
