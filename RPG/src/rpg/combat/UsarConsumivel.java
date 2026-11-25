package rpg.combat;

import rpg.item.Consumivel;
import rpg.item.Inventario;
import rpg.model.Combatente;
import rpg.model.Personagem;

public class UsarConsumivel implements AcaoCombate {
    private final Consumivel item;
    private final Inventario inventario;

    public UsarConsumivel(Consumivel item, Inventario inventario) {
        this.item = item;
        this.inventario = inventario;
    }
    public Consumivel getItem() { return item; }
    public boolean podeExecutar(Combatente alvo) {
        return alvo instanceof Personagem && inventario.getConsumiveisEquipados().contains(item)
                && item.podeUsar((Personagem) alvo);
    }
    @Override public String getNome() { return "Usar " + item; }
    @Override public int getCustoMp() { return 0; }
    @Override public void executar(Combatente usuario, Combatente alvo) {
        if (podeExecutar(alvo)) {
            item.aplicar((Personagem) alvo);
            inventario.removerConsumivel(item);
        }
    }
    @Override public String toString() { return getNome(); }
}
