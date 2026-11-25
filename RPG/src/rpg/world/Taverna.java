package rpg.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rpg.model.Clerigo;
import rpg.model.Guerreiro;
import rpg.model.Ladino;
import rpg.model.Mago;
import rpg.model.Personagem;

public class Taverna implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private List<Personagem> opcoesDeAliados;

    public Taverna() {
        opcoesDeAliados = new ArrayList<>();
    }

    public List<Personagem> gerarOpcoes(Grupo grupo) {
        Personagem protagonista = grupo.getMembros().get(0);
        int nivel = protagonista.getNivel();

        List<String> classes = new ArrayList<>();

        if (!(protagonista instanceof Guerreiro)) {
            classes.add("Guerreiro");
        }

        if (!(protagonista instanceof Mago)) {
            classes.add("Mago");
        }

        if (!(protagonista instanceof Ladino)) {
            classes.add("Ladino");
        }

        if (!(protagonista instanceof Clerigo)) {
            classes.add("Clérigo");
        }

        Collections.shuffle(classes);

        opcoesDeAliados = new ArrayList<>();

        for (String classe : classes) {
            Personagem aliado = criarAliado(classe, nivel);
            opcoesDeAliados.add(aliado);
        }

        return opcoesDeAliados;
    }

    private Personagem criarAliado(String classe, int nivel) {
        Personagem aliado;

        switch (classe) {
            case "Guerreiro":
                aliado = new Guerreiro("Aliado Guerreiro");
                break;

            case "Mago":
                aliado = new Mago("Aliado Mago");
                break;

            case "Ladino":
                aliado = new Ladino("Aliado Ladino");
                break;

            default:
                aliado = new Clerigo("Aliado Clérigo");
                break;
        }

        while (aliado.getNivel() < nivel) {
            aliado.subirNivel();
        }

        return aliado;
    }
}
