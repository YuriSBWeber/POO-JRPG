package rpg.combat;

import rpg.model.Combatente;

/** Golpes especiais identificados pelos nomes configurados em cada chefe. */
public class HabilidadeEspecialChefe extends Habilidade {
    private HabilidadeEspecialChefe(String nome, int custoMp,
            double multiplicador, boolean atingeTodos) {
        super(nome, custoMp, multiplicador, atingeTodos);
    }

    public static HabilidadeEspecialChefe criar(String nome) {
        if ("Esmagamento".equals(nome))
            return new HabilidadeEspecialChefe(nome, 10, 1.6, false);
        if ("Raízes Furiosas".equals(nome))
            return new HabilidadeEspecialChefe(nome, 12, 0.9, true);
        if ("Sopro Flamejante".equals(nome))
            return new HabilidadeEspecialChefe(nome, 20, 1.15, true);
        return null;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        int dano = (int) (usuario.getAtaque() * getMultiplicador()) - alvo.getDefesa();
        alvo.receberDano(Math.max(1, dano));
    }
}
