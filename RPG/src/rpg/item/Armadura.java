package rpg.item;

public class Armadura extends Item {

    private static final long serialVersionUID = 1L;

    private int bonusHp;
    private int bonusDefesa;
    private boolean pesada;

    public Armadura(String nome, int preco, int bonusHp,
            int bonusDefesa) {

        this(nome, preco, bonusHp, bonusDefesa, false);
    }

    public Armadura(String nome, int preco, int bonusHp,
            int bonusDefesa, boolean pesada) {

        super(nome, preco);

        this.bonusHp = bonusHp;
        this.bonusDefesa = bonusDefesa;
        this.pesada = pesada;
    }

    public int getBonusHp() {
        return bonusHp;
    }

    public int getBonusDefesa() {
        return bonusDefesa;
    }

    public boolean permite(rpg.model.Personagem personagem) {
        return !pesada || personagem instanceof rpg.model.Guerreiro;
    }

    @Override
    public String toString() {
        return getNome() + " (+" + bonusHp + " HP, +" + bonusDefesa
                + " defesa; " + (pesada ? "Guerreiro" : "todas as classes") + ")";
    }
}
