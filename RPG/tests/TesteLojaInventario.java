import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import rpg.Jogo;
import rpg.combat.AtaqueBasico;
import rpg.combat.Batalha;
import rpg.combat.UsarConsumivel;
import rpg.item.Arma;
import rpg.item.Armadura;
import rpg.item.Consumivel;
import rpg.item.Inventario;
import rpg.item.Item;
import rpg.model.Guerreiro;
import rpg.model.Mago;
import rpg.model.Monstro;
import rpg.persistencia.RepositorioJogo;
import rpg.world.ConfiguradorMundo;
import rpg.world.Grupo;
import rpg.world.Loja;

public class TesteLojaInventario {
    private static void conferir(boolean condicao, String mensagem) {
        if (!condicao) throw new AssertionError(mensagem);
    }
    private static void falha(Runnable acao) {
        try { acao.run(); } catch (IllegalArgumentException | IllegalStateException esperado) { return; }
        throw new AssertionError("Operação inválida foi aceita");
    }
    private static Item item(Loja loja, Jogo jogo, String nome) {
        return loja.listarCatalogo(jogo).stream().filter(i -> i.getNome().equals(nome)).findFirst().orElseThrow();
    }
    private static boolean tem(Loja loja, Jogo jogo, String nome) {
        return loja.listarCatalogo(jogo).stream().anyMatch(i -> i.getNome().equals(nome));
    }
    public static void main(String[] args) throws Exception {
        Guerreiro guerreiro = new Guerreiro("Herói");
        Mago mago = new Mago("Mago");
        Grupo grupo = new Grupo(guerreiro);
        grupo.adicionarAliado(mago);
        Jogo jogo = new Jogo(grupo);
        ConfiguradorMundo.configurarAreas(jogo);
        Loja loja = jogo.getCidade().getLoja();
        Inventario inventario = grupo.getInventario();
        conferir(tem(loja, jogo, "Espada de Madeira") && !tem(loja, jogo, "Espada de Ferro"), "estoque inicial");
        Item espada = item(loja, jogo, "Espada de Madeira");
        falha(() -> loja.comprar(jogo, espada));
        conferir(inventario.tamanho() == 0, "compra sem moedas não altera inventário");
        jogo.adicionarMoedas(1000);
        loja.comprar(jogo, espada);
        Armadura pesada = (Armadura) item(loja, jogo, "Armadura pesada de Madeira");
        loja.comprar(jogo, pesada);
        falha(() -> inventario.equiparItem(mago, espada, grupo.getMembros()));
        falha(() -> inventario.equiparItem(mago, pesada, grupo.getMembros()));
        inventario.equiparItem(guerreiro, espada, grupo.getMembros());
        inventario.equiparItem(guerreiro, pesada, grupo.getMembros());
        conferir(guerreiro.getAtaque() == 21 && guerreiro.getDefesa() == 15 && guerreiro.getHpMaximo() == 145, "bônus");
        Monstro alvo = new Monstro("Alvo", 1, 100, 0, 1, 2, 1, 0, 0);
        new AtaqueBasico().executar(guerreiro, alvo);
        conferir(alvo.getHpAtual() == 81, "arma afeta combate");
        new AtaqueBasico().executar(alvo, guerreiro);
        conferir(guerreiro.getHpAtual() == 124, "armadura afeta combate");
        Consumivel cura = (Consumivel) item(loja, jogo, "Poção de cura");
        Consumivel mana = (Consumivel) item(loja, jogo, "Poção de mana");
        Consumivel elixir = (Consumivel) item(loja, jogo, "Elixir de reviver");
        loja.comprar(jogo, cura); loja.comprar(jogo, mana); loja.comprar(jogo, elixir);
        inventario.equiparConsumivel(cura); inventario.equiparConsumivel(mana); inventario.equiparConsumivel(elixir);
        Consumivel extra = (Consumivel) item(loja, jogo, "Poção de cura");
        loja.comprar(jogo, extra);
        falha(() -> inventario.equiparConsumivel(extra));
        Batalha batalha = new Batalha(grupo, List.of(alvo));
        batalha.executarAcao(new UsarConsumivel(cura, inventario), guerreiro, guerreiro);
        conferir(guerreiro.getHpAtual() == 145 && !inventario.getConsumiveis().contains(cura), "cura e consumo");
        guerreiro.gastarMp(30);
        batalha.executarAcao(new UsarConsumivel(mana, inventario), guerreiro, guerreiro);
        conferir(guerreiro.getMpAtual() == guerreiro.getMpMaximo(), "mana");
        mago.receberDano(9999);
        batalha.executarAcao(new UsarConsumivel(elixir, inventario), guerreiro, mago);
        conferir(mago.getHpAtual() == mago.getHpMaximo() / 2, "reviver");
        inventario.descartar(pesada, grupo.getMembros());
        conferir(guerreiro.getArmaduraEquipada() == null && guerreiro.getHpAtual() == guerreiro.getHpMaximo(), "descarte ajusta equipamento e HP");
        jogo.entrarNaArea(jogo.getAreas().get(0)); jogo.concluirAreaAtual();
        conferir(!tem(loja, jogo, "Espada de Ferro"), "ferro não libera após primeiro chefe");
        jogo.entrarNaArea(jogo.getAreas().get(1)); jogo.concluirAreaAtual();
        conferir(tem(loja, jogo, "Espada de Ferro") && !tem(loja, jogo, "Espada de Mithril"), "estoque ferro");
        jogo.entrarNaArea(jogo.getAreas().get(2)); jogo.concluirAreaAtual();
        conferir(tem(loja, jogo, "Espada de Mithril"), "estoque mithril");
        inventario.equiparConsumivel(extra);
        Consumivel reviver = (Consumivel) item(loja, jogo, "Elixir de reviver");
        loja.comprar(jogo, reviver);
        inventario.equiparConsumivel(reviver);
        guerreiro.receberDano(9999);
        Monstro inimigo = new Monstro("Inimigo", 1, 500, 0, 1, 0, 1, 0, 0);
        new Batalha(grupo, List.of(inimigo)).executarRodada(new AtaqueBasico(), inimigo);
        conferir(guerreiro.estaVivo() && !inventario.getConsumiveis().contains(reviver), "aliado revive protagonista em combate");
        Path arquivo = Files.createTempFile("rpg-loja-", ".rpg");
        try {
            RepositorioJogo repositorio = new RepositorioJogo();
            repositorio.salvar(jogo, arquivo);
            Jogo carregado = repositorio.carregar(arquivo);
            conferir(carregado.chefeDerrotado(2) && carregado.getGrupo().getInventario().getConsumiveisEquipados().size() == 1, "persistência de progressão e slots");
        } finally { Files.deleteIfExists(arquivo); }
        while (inventario.temEspaco()) inventario.adicionarConsumivel(new Consumivel("Teste", 1, 1, 0));
        int moedas = jogo.getMoedas();
        falha(() -> loja.comprar(jogo, item(loja, jogo, "Poção de cura")));
        conferir(inventario.tamanho() == 20 && jogo.getMoedas() == moedas, "capacidade e compra atômica");
        System.out.println("Testes de loja e inventário passaram.");
    }
}
