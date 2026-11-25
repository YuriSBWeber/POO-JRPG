package rpg.combat;

import rpg.model.Combatente;

public class DestruirArmadura extends Habilidade {

    public DestruirArmadura() {
        super("Destruir Armadura", 14, 1.3);
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {

        int dano = (int) (usuario.getAtaque() * getMultiplicador())
                - alvo.getDefesa();

        if (dano < 1) {
            dano = 1;
        }

        alvo.receberDano(dano);
        alvo.aplicarDestruirArmadura();
    }
}