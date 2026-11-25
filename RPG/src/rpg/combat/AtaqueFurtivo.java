package rpg.combat;

import rpg.model.Combatente;

public class AtaqueFurtivo extends Habilidade {

    public AtaqueFurtivo() {
        super("Ataque Furtivo", 10, 1.35);
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        executarComCritico(usuario, alvo, false);
    }

    public void executarComCritico(Combatente usuario, Combatente alvo,
            boolean critico) {

        int dano = (int) (usuario.getAtaque() * getMultiplicador())
                - alvo.getDefesa();

        if (critico) {
            dano = (int) (dano * 1.75);
        }

        if (dano < 1) {
            dano = 1;
        }

        alvo.receberDano(dano);
    }
}