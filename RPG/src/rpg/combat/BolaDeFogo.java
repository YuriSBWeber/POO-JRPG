package rpg.combat;

import rpg.model.Combatente;

public class BolaDeFogo extends Habilidade {

    public BolaDeFogo() {
        super("Bola de Fogo", 14, 1.15, true);
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {

        int dano = (int) (usuario.getAtaque() * getMultiplicador())
                - alvo.getDefesa();

        if (dano < 1) {
            dano = 1;
        }

        alvo.receberDano(dano);
    }
}