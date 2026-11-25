package rpg.combat;

import rpg.model.Combatente;

public class Santuario extends Habilidade {

    public Santuario() {
        super("Santuário", 30, 0.6, true);
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {

        int valorCura = (int) (usuario.getAtaque()
                * getMultiplicador());

        alvo.curar(valorCura);
    }
}