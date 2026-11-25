package rpg.combat;

import rpg.model.Combatente;

public class AtaqueBasico implements AcaoCombate {

    @Override
    public String getNome() {
        return "Ataque básico";
    }

    @Override
    public int getCustoMp() {
        return 0;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        int dano = usuario.getAtaque() - alvo.getDefesa();

        if (dano < 1) {
            dano = 1;
        }

        alvo.receberDano(dano);
    }
    @Override
    public String toString() {
        return getNome();
    }
}