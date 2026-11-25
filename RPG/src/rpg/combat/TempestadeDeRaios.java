package rpg.combat;

import rpg.model.Combatente;

public class TempestadeDeRaios extends Habilidade {

    public TempestadeDeRaios() {
        super("Tempestade de Raios", 35, 1.6, true);
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