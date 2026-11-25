package rpg.model;

import rpg.item.Armadura;
import rpg.item.Arma;

public abstract class Personagem extends Combatente {

    private static final long serialVersionUID = 1L;

    private int experiencia;

    private Arma armaEquipada;
    private Armadura armaduraEquipada;

    public Personagem(String nome, int nivel, int hpMaximo, int mpMaximo,
            int ataque, int defesa, int velocidade) {

        super(nome, nivel, hpMaximo, mpMaximo, ataque, defesa, velocidade);

        this.experiencia = 0;
        this.armaEquipada = null;
        this.armaduraEquipada = null;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void ganharExperiencia(int quantidade) {
        experiencia += quantidade;

        while (getNivel() < 10
                && experiencia >= getNivel() * 30) {

            experiencia -= getNivel() * 30;
            subirNivel();
        }
    }

    public abstract void subirNivel();

    public Arma getArmaEquipada() {
        return armaEquipada;
    }

    public Armadura getArmaduraEquipada() {
        return armaduraEquipada;
    }

    public void equiparArma(Arma arma) {
        if (arma != null && !arma.permite(this)) throw new IllegalArgumentException("Esta arma não pode ser usada por esta classe.");
        armaEquipada = arma;
    }

    public void equiparArmadura(Armadura armadura) {
        if (armadura != null && !armadura.permite(this)) throw new IllegalArgumentException("Esta armadura não pode ser usada por esta classe.");
        armaduraEquipada = armadura;
        ajustarHpAoMaximo();
    }

    @Override
    public int getAtaque() {
        int bonus = 0;

        if (armaEquipada != null) {
            bonus = armaEquipada.getBonusAtaque();
        }

        return super.getAtaque() + bonus;
    }

    @Override
    public int getDefesa() {
        int bonus = 0;

        if (armaduraEquipada != null) {
            bonus = armaduraEquipada.getBonusDefesa();
        }

        return super.getDefesa() + bonus;
    }

    @Override
    public int getHpMaximo() {
        int bonus = 0;

        if (armaduraEquipada != null) {
            bonus = armaduraEquipada.getBonusHp();
        }

        return super.getHpMaximo() + bonus;
    }
    public int getAtaqueTotal() { return getAtaque(); }
    public int getDefesaTotal() { return getDefesa(); }
    public int getHpMaximoTotal() { return getHpMaximo(); }
    @Override
    public String toString() {
        return getNome()
                + " - " + getClass().getSimpleName()
                + " nível " + getNivel();
    }
}
