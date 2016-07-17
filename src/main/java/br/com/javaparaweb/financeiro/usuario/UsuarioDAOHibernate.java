package br.com.javaparaweb.financeiro.usuario;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public class UsuarioDAOHibernate implements UsuarioDAO {

	private Session session;
	
	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	public void salvar(Usuario usuario) {
		session.save(usuario);
	}

	@Override
	public void atualizar(Usuario usuario) {
		session.update(usuario);
	}

	@Override
	public void excluir(Usuario usuario) {
		session.delete(usuario);
	}

	@Override
	public Usuario carregar(Integer codigo) {
		return (Usuario) session.get(Usuario.class, codigo);
	}

	@Override
	public Usuario buscarPorLogin(String login) {
		String hql = "SELECT u FROM Usuario u WHERE u.login = :login";
		Query consulta = session.createQuery(hql);
		consulta.setString("login", login);
		return (Usuario) consulta.uniqueResult();
	}

	@Override
	public List<Usuario> listar() {
		return session.createCriteria(Usuario.class).list();
	}

}
