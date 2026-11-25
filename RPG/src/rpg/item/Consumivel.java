package rpg.item;

public class Consumivel extends Item {

    private static final long serialVersionUID = 1L;

    private int curaHp;
    private int recuperaMp;
    private boolean revive;

    public Consumivel(String nome, int preco, int curaHp,
            int recuperaMp) {

        this(nome, preco, curaHp, recuperaMp, false);
    }

    public Consumivel(String nome, int preco, int curaHp,
            int recuperaMp, boolean revive) {

        super(nome, preco);

        this.curaHp = curaHp;
        this.recuperaMp = recuperaMp;
        this.revive = revive;
    }

    public int getCuraHp() {
        return curaHp;
    }

    public int getRecuperaMp() {
        return recuperaMp;
    }

    public boolean isRevive() { return revive; }

    public boolean podeUsar(rpg.model.Personagem alvo) {
        return revive ? !alvo.estaVivo() : alvo.estaVivo()
                && ((curaHp > 0 && alvo.getHpAtual() < alvo.getHpMaximo())
                || (recuperaMp > 0 && alvo.getMpAtual() < alvo.getMpMaximo()));
    }

    public void aplicar(rpg.model.Personagem alvo) {
        if (!podeUsar(alvo)) throw new IllegalArgumentException("Consumível não pode ser usado neste alvo.");
        if (revive) alvo.curar(Math.max(1, alvo.getHpMaximo() / 2));
        else { alvo.curar(curaHp); alvo.recuperarMp(recuperaMp); }
    }

    @Override
    public String toString() {
        return getNome() + (revive ? " (revive com 50% do HP)"
                : " (" + (curaHp > 0 ? "+" + curaHp + " HP" : "+" + recuperaMp + " MP") + ")");
    }
}
