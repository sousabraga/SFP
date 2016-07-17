package br.com.javaparaweb.financeiro.usuario;

import java.util.List;

import br.com.javaparaweb.financeiro.util.DAOFactory;

public class UsuarioRN {

	private UsuarioDAO usuarioDAO;
	
	public UsuarioRN() {
		usuarioDAO = DAOFactory.criarUsuarioDAO();
	}
	
	public void salvar(Usuario usuario) {
		Integer codigo = usuario.getCodigo();
		if (codigo == null || codigo == 0) 
			usuarioDAO.salvar(usuario);
		else 
			usuarioDAO.atualizar(usuario);
	}
	
	public void excluir(Usuario usuario) {
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
}
