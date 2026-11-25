package rpg.combat;

import rpg.model.Combatente;

public class Cura extends Habilidade {

    public Cura() {
        super("Cura", 16, 2.5);
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {

        int valorCura = (int) (usuario.getAtaque()
                * getMultiplicador());

        alvo.curar(valorCura);
    }
}