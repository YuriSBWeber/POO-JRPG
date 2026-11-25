package rpg.model;

public class Monstro extends Combatente {

    private static final long serialVersionUID = 1L;

    private int experienciaRecompensa;
    private int moedasRecompensa;

    public Monstro(String nome, int nivel, int hpMaximo, int mpMaximo,
            int ataque, int defesa, int velocidade,
            int experienciaRecompensa, int moedasRecompensa) {

        super(nome, nivel, hpMaximo, mpMaximo, ataque, defesa, velocidade);

        this.experienciaRecompensa = experienciaRecompensa;
        this.moedasRecompensa = moedasRecompensa;
    }

    public int getExperienciaRecompensa() {
        return experienciaRecompensa;
    }

    public int getMoedasRecompensa() {
        return moedasRecompensa;
    }
    public Monstro criarCopia() {
        return new Monstro(
                getNome(),
                getNivel(),
                getHpMaximo(),
                getMpMaximo(),
                getAtaque(),
                getDefesa(),
                getVelocidade(),
                experienciaRecompensa,
                moedasRecompensa
        );
    }
    @Override
    public String toString() {
        return getNome() + " - HP: " + getHpAtual()
                + "/" + getHpMaximo();
    }
}
