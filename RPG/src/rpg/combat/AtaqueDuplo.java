package rpg.combat;

import java.util.Random;
import rpg.model.Combatente;

public class AtaqueDuplo extends Habilidade {

    private Random random;

    public AtaqueDuplo() {
        super("Ataque Duplo", 18, 0.9);
        random = new Random();
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {

        aplicarGolpe(usuario, alvo);
        aplicarGolpe(usuario, alvo);
    }

    private void aplicarGolpe(Combatente usuario, Combatente alvo) {
        int dano = (int) (usuario.getAtaque() * getMultiplicador())
                - alvo.getDefesa();

        boolean critico = random.nextInt(100) < 35;

        if (critico) {
            dano = (int) (dano * 1.75);
        }

        if (dano < 1) {
            dano = 1;
        }

        alvo.receberDano(dano);
    }
}