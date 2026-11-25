package rpg.combat;

import java.util.ArrayList;
import java.util.List;

import rpg.model.Clerigo;
import rpg.model.Guerreiro;
import rpg.model.Ladino;
import rpg.model.Mago;
import rpg.model.Personagem;

public class GeradorAcoes {

    public List<AcaoCombate> criarAcoes(Personagem personagem) {
        List<AcaoCombate> acoes = new ArrayList<>();

        acoes.add(new AtaqueBasico());

        if (personagem instanceof Guerreiro) {
            acoes.add(new GolpePesado());

            if (personagem.getNivel() >= 3) {
                acoes.add(new Furia());
            }

            if (personagem.getNivel() >= 5) {
                acoes.add(new DestruirArmadura());
            }
        }

        if (personagem instanceof Mago) {
            acoes.add(new LancaDeGelo());

            if (personagem.getNivel() >= 3) {
                acoes.add(new BolaDeFogo());
            }

            if (personagem.getNivel() >= 5) {
                acoes.add(new TempestadeDeRaios());
            }
        }

        if (personagem instanceof Ladino) {
            acoes.add(new AtaqueFurtivo());

            if (personagem.getNivel() >= 3) {
                acoes.add(new AtaqueDuplo());
            }

            if (personagem.getNivel() >= 5) {
                acoes.add(new DancaDasAdagas());
            }
        }

        if (personagem instanceof Clerigo) {
            acoes.add(new Cura());

            if (personagem.getNivel() >= 3) {
                acoes.add(new LuzSagrada());
            }

            if (personagem.getNivel() >= 5) {
                acoes.add(new Santuario());
            }
        }

        return acoes;
    }
}