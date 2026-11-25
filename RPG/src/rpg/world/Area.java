package rpg.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rpg.model.Chefe;
import rpg.model.Monstro;

public class Area implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private int nivelRecomendado;
    private boolean desbloqueada;
    private boolean concluida;

    private List<Monstro> tiposDeMonstro;
    private Chefe chefe;

    public Area(String nome, int nivelRecomendado,
            boolean desbloqueada) {

        this.nome = nome;
        this.nivelRecomendado = nivelRecomendado;
        this.desbloqueada = desbloqueada;

        tiposDeMonstro = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public int getNivelRecomendado() {
        return nivelRecomendado;
    }

    public boolean isDesbloqueada() {
        return desbloqueada;
    }

    public void desbloquear() {
        desbloqueada = true;
    }
    public boolean isConcluida() { return concluida; }
    public void concluir() { concluida = true; }

    public List<Monstro> getTiposDeMonstro() {
        return tiposDeMonstro;
    }

    public void adicionarTipoDeMonstro(Monstro monstro) {
        tiposDeMonstro.add(monstro);
    }

    public Chefe getChefe() {
        return chefe;
    }

    public void definirChefe(Chefe chefe) {
        this.chefe = chefe;
    }
    public Monstro sortearMonstro(Random random) {
        int indice = random.nextInt(tiposDeMonstro.size());

        Monstro modelo = tiposDeMonstro.get(indice);

        return modelo.criarCopia();
    }
}
