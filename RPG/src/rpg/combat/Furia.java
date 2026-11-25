package rpg.combat;

import rpg.model.Combatente;

public class Furia extends Habilidade {

    public Furia() {
        super("Fúria", 15, 0);
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {

        usuario.ativarFuria();
    }
}