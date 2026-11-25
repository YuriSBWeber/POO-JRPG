package rpg.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rpg.model.Monstro;
import rpg.world.Area;

public class GeradorEncontros {

    private Random random;

    public GeradorEncontros() {
        random = new Random();
    }

    public List<Monstro> criarEncontroComum(Area area) {
        int quantidade;

        switch (area.getNome()) {
            case "Planície":
                quantidade = 1 + random.nextInt(2);
                break;

            case "Floresta":
                quantidade = 1 + random.nextInt(3);
                break;

            default:
                quantidade = 2 + random.nextInt(2);
                break;
        }

        List<Monstro> monstros = new ArrayList<>();

        for (int i = 0; i < quantidade; i++) {
            monstros.add(area.sortearMonstro(random));
        }

        return monstros;
    }
}