package rpg.item;

public class Arma extends Item {

    private static final long serialVersionUID = 1L;

    private int bonusAtaque;
    private String classePermitida;

    public Arma(String nome, int preco, int bonusAtaque) {
        this(nome, preco, bonusAtaque, null);
    }

    public Arma(String nome, int preco, int bonusAtaque, String classePermitida) {
        super(nome, preco);

        this.bonusAtaque = bonusAtaque;
        this.classePermitida = classePermitida;
    }

    public int getBonusAtaque() {
        return bonusAtaque;
    }

    public boolean permite(rpg.model.Personagem personagem) {
        return classePermitida == null || classePermitida.equals(personagem.getClass().getSimpleName());
    }

    @Override
    public String toString() {
        return getNome() + " (+" + bonusAtaque + " ataque; "
                + (classePermitida == null ? "qualquer classe" : classePermitida) + ")";
    }
}
