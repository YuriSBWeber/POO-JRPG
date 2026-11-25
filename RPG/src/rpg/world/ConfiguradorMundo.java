package rpg.world;

import rpg.Jogo;
import rpg.model.Chefe;
import rpg.model.Monstro;

public class ConfiguradorMundo {

    public static void configurarAreas(Jogo jogo) {
        Area planicie = new Area("Planície", 1, true);

        planicie.adicionarTipoDeMonstro(
                new Monstro("Slime", 1, 45, 0, 9, 2, 5, 10, 7));
        planicie.adicionarTipoDeMonstro(
                new Monstro("Lobo", 1, 55, 0, 11, 3, 10, 12, 8));
        planicie.adicionarTipoDeMonstro(
                new Monstro("Goblin", 1, 60, 0, 12, 4, 8, 14, 10));
        planicie.adicionarTipoDeMonstro(
                new Monstro("Javali Selvagem", 1, 70, 0, 13, 5, 6, 16, 11));
        planicie.adicionarTipoDeMonstro(
                new Monstro("Bandido", 1, 65, 0, 14, 4, 9, 16, 12));

        planicie.definirChefe(
                new Chefe("Ogro da Planície", 2, 180, 30, 20, 7,
                        6, 50, 60, "Esmagamento"));

        Area floresta = new Area("Floresta", 3, false);

        floresta.adicionarTipoDeMonstro(
                new Monstro("Aranha Gigante", 3, 95, 0, 18, 7, 12, 22, 17));
        floresta.adicionarTipoDeMonstro(
                new Monstro("Lobo Sombrio", 3, 105, 0, 20, 8, 15, 24, 18));
        floresta.adicionarTipoDeMonstro(
                new Monstro("Ent Jovem", 3, 125, 0, 19, 11, 5, 26, 20));
        floresta.adicionarTipoDeMonstro(
                new Monstro("Fada Corrompida", 3, 90, 0, 22, 6, 14, 25, 20));
        floresta.adicionarTipoDeMonstro(
                new Monstro("Goblin Xamã", 3, 100, 0, 21, 7, 10, 27, 22));

        floresta.definirChefe(
                new Chefe("Ent Ancião", 4, 310, 40, 27, 13, 5,
                        80, 110, "Raízes Furiosas"));

        Area montanha = new Area("Montanha", 5, false);

        montanha.adicionarTipoDeMonstro(
                new Monstro("Harpia", 5, 145, 0, 28, 10, 18, 34, 28));
        montanha.adicionarTipoDeMonstro(
                new Monstro("Golem de Pedra", 5, 190, 0, 27, 17, 4, 38, 32));
        montanha.adicionarTipoDeMonstro(
                new Monstro("Troll", 5, 210, 0, 30, 14, 7, 40, 35));
        montanha.adicionarTipoDeMonstro(
                new Monstro("Salamandra", 5, 160, 0, 32, 11, 13, 39, 34));
        montanha.adicionarTipoDeMonstro(
                new Monstro("Elemental de Vento", 5, 150, 0, 29, 9, 20, 42, 36));

        montanha.definirChefe(
                new Chefe("Dragão da Montanha", 7, 520, 80, 42, 18, 16,
                        150, 250, "Sopro Flamejante"));

        jogo.adicionarArea(planicie);
        jogo.adicionarArea(floresta);
        jogo.adicionarArea(montanha);
    }
}