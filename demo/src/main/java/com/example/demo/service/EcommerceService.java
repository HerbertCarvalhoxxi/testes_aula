package com.example.demo.service;

import com.example.demo.model.ItemCarrinho;
import com.example.demo.model.Produto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ItemCarrinhoRepository;
import com.example.demo.repository.ProdutoRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EcommerceService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ItemCarrinhoRepository itemCarrinhoRepository;

    public Usuario autenticar(String email, String senha) {
        return usuarioRepository.findByEmail(email)
                .filter(u -> u.getSenha().equals(senha))
                .orElseThrow(() -> new RuntimeException("E-mail ou senha inválidos"));
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public void adicionarAoCarrinho(Long usuarioId, Long produtoId, int quantidade) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ou não logado"));
        
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!produto.temEstoque(quantidade)) {
            throw new RuntimeException("Produto sem estoque suficiente");
        }

        ItemCarrinho item = new ItemCarrinho();
        item.setUsuario(usuario);
        item.setProduto(produto);
        item.setQuantidade(quantidade);

        itemCarrinhoRepository.save(item);
    }

    @Transactional
    public String efetuarCompra(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getCarrinho().isEmpty()) {
            throw new RuntimeException("Seu carrinho está vazio. Adicione produtos antes de finalizar a compra.");
        }

        double total = 0.0;
        for (ItemCarrinho item : usuario.getCarrinho()) {
            if (!item.getProduto().temEstoque(item.getQuantidade())) {
                throw new RuntimeException("Produto " + item.getProduto().getNome() + " esgotado.");
            }
            total += item.getProduto().getPreco() * item.getQuantidade();
        }

        if (!usuario.temSaldo(total)) {
            throw new RuntimeException("Saldo insuficiente para realizar a compra.");
        }

        // Debitar saldo, decrementar estoque e limpar carrinho
        usuario.debitarSaldo(total);
        for (ItemCarrinho item : usuario.getCarrinho()) {
            item.getProduto().decrementarEstoque(item.getQuantidade());
            produtoRepository.save(item.getProduto());
        }

        usuario.getCarrinho().clear();
        usuarioRepository.save(usuario);

        return "Compra efetuada com sucesso!";
    }

    public Usuario cadastrar(Usuario novoUsuario) {
    if (usuarioRepository.findByEmail(novoUsuario.getEmail()).isPresent()) {
        throw new RuntimeException("Este e-mail já está registado.");
    }
    // Se não for informado um saldo inicial válido, define um padrão de R$ 1500.0
    if (novoUsuario.getSaldo() <= 0) {
        novoUsuario.setSaldo(1500.0);
    }
    return usuarioRepository.save(novoUsuario);
}
}