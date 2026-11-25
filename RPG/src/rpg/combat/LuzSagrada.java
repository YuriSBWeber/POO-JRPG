package rpg.combat;

import rpg.model.Combatente;

public class LuzSagrada extends Habilidade {

    public LuzSagrada() {
        super("Luz Sagrada", 17, 1.8);
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