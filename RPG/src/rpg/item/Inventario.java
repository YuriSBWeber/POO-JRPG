package rpg.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import rpg.model.Personagem;

public class Inventario implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private List<Arma> armas;
    private List<Armadura> armaduras;
    private List<Consumivel> consumiveis;
    private List<Consumivel> consumiveisEquipados;
    public static final int CAPACIDADE = 20;
    public static final int SLOTS_CONSUMIVEIS = 3;

    public Inventario() {
        armas = new ArrayList<>();
        armaduras = new ArrayList<>();
        consumiveis = new ArrayList<>();
        consumiveisEquipados = new ArrayList<>();
    }

    public void adicionarArma(Arma arma) {
        verificarEspaco();
        armas.add(arma);
    }

    public void adicionarArmadura(Armadura armadura) {
        verificarEspaco();
        armaduras.add(armadura);
    }

    public void adicionarConsumivel(Consumivel consumivel) {
        verificarEspaco();
        consumiveis.add(consumivel);
    }

    public List<Arma> getArmas() {
        return Collections.unmodifiableList(armas);
    }

    public List<Armadura> getArmaduras() {
        return Collections.unmodifiableList(armaduras);
    }

    public List<Consumivel> getConsumiveis() {
        return Collections.unmodifiableList(consumiveis);
    }

    public void removerConsumivel(Consumivel consumivel) {
        slots().remove(consumivel);
        consumiveis.remove(consumivel);
    }

    private List<Consumivel> slots() {
        if (consumiveisEquipados == null) consumiveisEquipados = new ArrayList<>();
        return consumiveisEquipados;
    }

    public int tamanho() { return armas.size() + armaduras.size() + consumiveis.size(); }
    public boolean temEspaco() { return tamanho() < CAPACIDADE; }
    private void verificarEspaco() {
        if (!temEspaco()) throw new IllegalStateException("Inventário cheio: limite de 20 itens.");
    }
    public List<Item> getItens() {
        List<Item> itens = new ArrayList<>();
        itens.addAll(armas); itens.addAll(armaduras); itens.addAll(consumiveis);
        return Collections.unmodifiableList(itens);
    }
    public List<Consumivel> getConsumiveisEquipados() {
        return Collections.unmodifiableList(slots());
    }
    public void equiparConsumivel(Consumivel item) {
        if (!consumiveis.contains(item)) throw new IllegalArgumentException("Item não está no inventário.");
        if (slots().contains(item)) throw new IllegalStateException("Consumível já está equipado.");
        if (slots().size() >= SLOTS_CONSUMIVEIS) throw new IllegalStateException("Os três slots de consumíveis estão ocupados.");
        slots().add(item);
    }
    public void desequiparConsumivel(Consumivel item) { slots().remove(item); }
    public void equiparItem(Personagem personagem, Item item, List<Personagem> grupo) {
        if (!grupo.contains(personagem) || !getItens().contains(item)) throw new IllegalArgumentException("Personagem ou item inválido.");
        for (Personagem membro : grupo) {
            if (membro != personagem && (membro.getArmaEquipada() == item || membro.getArmaduraEquipada() == item))
                throw new IllegalStateException("Este item já está equipado por outro personagem.");
        }
        if (item instanceof Arma) personagem.equiparArma((Arma) item);
        else if (item instanceof Armadura) personagem.equiparArmadura((Armadura) item);
        else throw new IllegalArgumentException("Selecione uma arma ou armadura.");
    }
    public void descartar(Item item, List<Personagem> grupo) {
        if (!getItens().contains(item)) throw new IllegalArgumentException("Item não está no inventário.");
        for (Personagem membro : grupo) {
            if (membro.getArmaEquipada() == item) membro.equiparArma(null);
            if (membro.getArmaduraEquipada() == item) membro.equiparArmadura(null);
        }
        if (item instanceof Consumivel) removerConsumivel((Consumivel) item);
        else if (item instanceof Arma) armas.remove(item);
        else if (item instanceof Armadura) armaduras.remove(item);
    }
    public void adicionar(Item item) {
        if (item instanceof Arma) adicionarArma((Arma) item);
        else if (item instanceof Armadura) adicionarArmadura((Armadura) item);
        else if (item instanceof Consumivel) adicionarConsumivel((Consumivel) item);
        else throw new IllegalArgumentException("Tipo de item inválido.");
    }
}
