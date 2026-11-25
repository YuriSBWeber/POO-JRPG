package rpg.combat;

import rpg.model.Combatente;

public class LancaDeGelo extends Habilidade {

    public LancaDeGelo() {
        super("Lança de Gelo", 18, 3.0);
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