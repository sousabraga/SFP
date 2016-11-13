package br.com.javaparaweb.financeiro.usuario;

import java.util.List;
import java.util.Locale;

import br.com.javaparaweb.financeiro.categoria.CategoriaRN;
import br.com.javaparaweb.financeiro.util.DAOFactory;
import br.com.javaparaweb.financeiro.util.EmailUtil;
import br.com.javaparaweb.financeiro.util.MensagemUtil;
import br.com.javaparaweb.financeiro.util.RNException;
import br.com.javaparaweb.financeiro.util.UtilException;

public class UsuarioRN {

	private UsuarioDAO usuarioDAO;
	
	public UsuarioRN() {
		usuarioDAO = DAOFactory.criarUsuarioDAO();
	}
	
	public void salvar(Usuario usuario) {
		Integer codigo = usuario.getCodigo();
		if (codigo == null || codigo == 0) {
			usuario.getPermissao().add("ROLE_USUARIO");
			usuarioDAO.salvar(usuario);
			
			CategoriaRN categoriaRN = new CategoriaRN();
			categoriaRN.salvaEstruturaPadrao(usuario);
		} else 
			usuarioDAO.atualizar(usuario);
	}
	
	public void excluir(Usuario usuario) {
		CategoriaRN categoriaRN = new CategoriaRN();
		categoriaRN.excluir(usuario);
		
		usuarioDAO.excluir(usuario);
	}
	
	public Usuario carregar(Integer codigo) {
		return usuarioDAO.carregar(codigo);
	}
	
	public Usuario buscarPorLogin(String login) {
		return usuarioDAO.buscarPorLogin(login);
	}
	
	public List<Usuario> listar() {
		return usuarioDAO.listar();
	}
	
	public void enviarEmailPosCadastramento(Usuario usuario) throws RNException {
		String[] info = usuario.getIdioma().split("_");
		Locale locale = new Locale(info[0], info[1]);
		
		String titulo = MensagemUtil.getMensagem(locale, "email_titulo");
		String mensagem = MensagemUtil.getMensagem(locale, "email_mensagem", usuario.getNome(), usuario.getLogin(), usuario.getSenha());
		
		try {
			
			EmailUtil emailUtil = new EmailUtil();
			emailUtil.enviarEmail(null, usuario.getEmail(), titulo, mensagem);
			
		} catch (UtilException e) {
			throw new RNException(e);
		}
		
	}
	
}
