package br.com.javaparaweb.financeiro.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import br.com.javaparaweb.financeiro.categoria.Categoria;
import br.com.javaparaweb.financeiro.categoria.CategoriaRN;

@ManagedBean
@RequestScoped
public class CategoriaBean {

	private TreeNode categoriasTree;
	
	private Categoria editada = new Categoria();
	
	private List<SelectItem> categoriasSelect;
	
	private boolean mostraEdicao = false;
	
	@ManagedProperty(value = "#{contextoBean}")
	private ContextoBean contextoBean;
	
	public void novo() {
		Categoria pai = null;
		
		if (editada.getCodigo() != null) {
			CategoriaRN categoriaRN = new CategoriaRN();
			pai = categoriaRN.carregar(editada.getCodigo());
		}
		
		editada = new Categoria();
		editada.setPai(pai);
		mostraEdicao = true;
		
	}
	
	public void selecionar(NodeSelectEvent event) {
		editada = (Categoria) event.getTreeNode().getData();
		
		Categoria pai = editada.getPai();
		
		if (editada != null && pai.getCodigo() != null)
			mostraEdicao = true;
		else
			mostraEdicao = false;
		
	}
	
	public String salvar() {
		CategoriaRN categoriaRN = new CategoriaRN();
		editada.setUsuario(contextoBean.getUsuarioLogado());
		categoriaRN.salvar(editada);
		
		editada = null;
		mostraEdicao = false;
		categoriasTree = null;
		categoriasSelect = null;
		
		return null;
	}
	
	public String excluir() {
		CategoriaRN categoriaRN = new CategoriaRN();
		categoriaRN.excluir(editada);
		
		editada = null;
		mostraEdicao = false;
		categoriasTree = null;
		categoriasSelect = null;
		
		return null;
	}
	
	public TreeNode getCategoriasTree() {
		if (categoriasTree == null) {
			CategoriaRN categoriaRN = new CategoriaRN();
			List<Categoria> categorias = categoriaRN.listar(contextoBean.getUsuarioLogado());
			categoriasTree = new DefaultTreeNode(null, null);
			montaDadosTree(categoriasTree, categorias);
		}
		
		return categoriasTree;
	}
	
	private void montaDadosTree(TreeNode pai, List<Categoria> lista) {
		if (lista != null && lista.size() > 0) {
			TreeNode filho = null;
			
			for (Categoria categoria : lista) {
				filho = new DefaultTreeNode(categoria, pai);
				montaDadosTree(filho, categoria.getFilhos());
			}
		}
	}
	
	public List<SelectItem> getCategoriasSelect() {
		if (categoriasSelect == null) {
			categoriasSelect = new ArrayList<>();
			CategoriaRN categoriaRN = new CategoriaRN(); 
			List<Categoria> categorias = categoriaRN.listar(contextoBean.getUsuarioLogado());
			montaDadosSelect(categoriasSelect, categorias, "");
		}
		
		return categoriasSelect;
	}
	
	private void montaDadosSelect(List<SelectItem> select, List<Categoria> categorias, String prefixo) {
		SelectItem item = null;
		
		if (categorias != null) {
			for (Categoria categoria : categorias) {
				item = new SelectItem(categoria, prefixo + categoria.getDescricao());
				item.setEscape(false);
				select.add(item);
				montaDadosSelect(select, categoria.getFilhos(), prefixo + "&nbsp;&nbsp;");
			}
		}
	}

	public Categoria getEditada() {
		return editada;
	}

	public void setEditada(Categoria editada) {
		this.editada = editada;
	}

	public boolean isMostraEdicao() {
		return mostraEdicao;
	}

	public void setMostraEdicao(boolean mostraEdicao) {
		this.mostraEdicao = mostraEdicao;
	}

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}

	public void setCategoriasSelect(List<SelectItem> categoriasSelect) {
		this.categoriasSelect = categoriasSelect;
	}
	
}
