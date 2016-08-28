package br.com.javaparaweb.financeiro.categoria;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.javaparaweb.financeiro.usuario.Usuario;

public class CategoriaDAOHibernate implements CategoriaDAO {

	private Session session;
	
	public void setSession(Session session) {
		this.session = session;
	}
	
	@Override
	public Categoria salvar(Categoria categoria) {
		Categoria merged = (Categoria) session.merge(categoria);
		session.flush();
		session.clear();
		return merged;
	}

	@Override
	public void excluir(Categoria categoria) {
		categoria = (Categoria) carregar(categoria.getCodigo());
		session.delete(categoria);
		session.flush();
		session.clear();
	}

	@Override
	public Categoria carregar(Integer categoria) {
		return (Categoria) session.get(Categoria.class, categoria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Categoria> listar(Usuario usuario) {
		String hql = "SELECT c FROM Categoria c WHERE c.pai IS NULL AND c.usuario = :usuario";
		
		Query query = session.createQuery(hql);
		query.setInteger("usuario", usuario.getCodigo());
		
		List<Categoria> lista = query.list();
		
		return lista;
	}

}
