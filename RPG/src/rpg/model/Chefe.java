package rpg.model;

public class Chefe extends Monstro {

    private static final long serialVersionUID = 1L;

    private String habilidadeEspecial;

    public Chefe(String nome, int nivel, int hpMaximo, int mpMaximo,
            int ataque, int defesa, int velocidade,
            int experienciaRecompensa, int moedasRecompensa,
            String habilidadeEspecial) {

        super(nome, nivel, hpMaximo, mpMaximo, ataque, defesa, velocidade,
                experienciaRecompensa, moedasRecompensa);

        this.habilidadeEspecial = habilidadeEspecial;
    }

    public String getHabilidadeEspecial() {
        return habilidadeEspecial;
    }
    @Override
    public Chefe criarCopia() {
        return new Chefe(
                getNome(),
                getNivel(),
                getHpMaximo(),
                getMpMaximo(),
                getAtaque(),
                getDefesa(),
                getVelocidade(),
                getExperienciaRecompensa(),
                getMoedasRecompensa(),
                habilidadeEspecial
        );
    }
}
