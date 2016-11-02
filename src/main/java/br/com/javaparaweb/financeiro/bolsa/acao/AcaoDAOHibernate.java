package br.com.javaparaweb.financeiro.bolsa.acao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.javaparaweb.financeiro.usuario.Usuario;

public class AcaoDAOHibernate implements AcaoDAO {

	private Session session;
	
	public void setSession(Session session) {
		this.session = session;
	}
	
	@Override
	public void salvar(Acao acao) {
		session.saveOrUpdate(acao);
	}

	@Override
	public void excluir(Acao acao) {
		session.delete(acao);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Acao> listar(Usuario usuario) {
		Criteria criteria = session.createCriteria(Acao.class);
		criteria.add(Restrictions.eq("usuario", usuario));
		return criteria.list();
	}

}
