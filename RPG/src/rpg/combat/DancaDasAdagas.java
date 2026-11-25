package rpg.combat;

import java.util.Random;
import rpg.model.Combatente;

public class DancaDasAdagas extends Habilidade {

    private Random random;

    public DancaDasAdagas() {
        super("Dança das Adagas", 40, 0.5, true);
        random = new Random();
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {

        for (int i = 0; i < 6; i++) {
            int dano = (int) (usuario.getAtaque() * getMultiplicador())
                    - alvo.getDefesa();

            boolean critico = random.nextInt(100) < 25;

            if (critico) {
                dano = (int) (dano * 1.75);
            }

            if (dano < 1) {
                dano = 1;
            }

            alvo.receberDano(dano);
        }
    }
}