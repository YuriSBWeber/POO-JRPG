package rpg.combat;

import rpg.model.Combatente;

public class GolpePesado extends Habilidade {

    public GolpePesado() {
        super("Golpe Pesado", 12, 1.7);
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