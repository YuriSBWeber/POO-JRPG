package rpg.world;

import java.util.ArrayList;
import java.util.List;

import rpg.item.Item;
import rpg.item.Arma;
import rpg.item.Armadura;
import rpg.item.Consumivel;
import rpg.Jogo;

public class Loja implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private List<Item> itensDisponiveis;

    public Loja() {
        itensDisponiveis = new ArrayList<>();
    }

    public void adicionarItem(Item item) {
        itensDisponiveis.add(item);
    }

    public List<Item> getItensDisponiveis() {
        return itensDisponiveis;
    }

    public List<Item> listarCatalogo(Jogo jogo) {
        List<Item> itens = new ArrayList<>();
        adicionarMaterial(itens, "Madeira", 4, 3, 12, 5, 20, 25, 30);
        if (jogo.chefeDerrotado(1))
            adicionarMaterial(itens, "Ferro", 9, 7, 28, 11, 45, 90, 110);
        if (jogo.chefeDerrotado(2))
            adicionarMaterial(itens, "Mithril", 16, 12, 50, 18, 75, 220, 250);
        itens.add(new Consumivel("Poção de cura", 20, 65, 0));
        itens.add(new Consumivel("Poção de mana", 20, 0, 35));
        itens.add(new Consumivel("Elixir de reviver", 55, 0, 0, true));
        return itens;
    }

    private void adicionarMaterial(List<Item> itens, String material, int ataque,
            int defesaLeve, int hpLeve, int defesaPesada, int hpPesado,
            int precoArma, int precoArmadura) {
        itens.add(new Arma("Espada de " + material, precoArma, ataque, "Guerreiro"));
        itens.add(new Arma("Cajado de " + material, precoArma, ataque, "Mago"));
        itens.add(new Arma("Adagas de " + material, precoArma, ataque, "Ladino"));
        itens.add(new Arma("Cetro de " + material, precoArma, ataque, "Clerigo"));
        itens.add(new Armadura("Armadura leve de " + material, precoArmadura, hpLeve, defesaLeve));
        itens.add(new Armadura("Armadura pesada de " + material, precoArmadura + 10, hpPesado, defesaPesada, true));
    }

    public void comprar(Jogo jogo, Item item) {
        if (!jogo.getGrupo().getInventario().temEspaco())
            throw new IllegalStateException("Inventário cheio: descarte um item antes de comprar.");
        if (!jogo.gastarMoedas(item.getPreco()))
            throw new IllegalStateException("Moedas insuficientes para esta compra.");
        jogo.getGrupo().getInventario().adicionar(item);
    }
}
