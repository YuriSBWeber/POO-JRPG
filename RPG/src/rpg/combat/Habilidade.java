package rpg.combat;

public abstract class Habilidade implements AcaoCombate {

    private String nome;
    private int custoMp;
    private double multiplicador;
    private boolean atingeTodos;

    public Habilidade(String nome, int custoMp, double multiplicador) {
        this(nome, custoMp, multiplicador, false);
    }

    public Habilidade(String nome, int custoMp, double multiplicador,
            boolean atingeTodos) {

        this.nome = nome;
        this.custoMp = custoMp;
        this.multiplicador = multiplicador;
        this.atingeTodos = atingeTodos;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public int getCustoMp() {
        return custoMp;
    }

    public double getMultiplicador() {
        return multiplicador;
    }

    public boolean isAtingeTodos() {
        return atingeTodos;
    }
    @Override
    public String toString() {
        return getNome();
    }
}