package rpg.combat;

import rpg.model.Combatente;

public interface AcaoCombate {

    String getNome();

    int getCustoMp();

    void executar(Combatente usuario, Combatente alvo);
}