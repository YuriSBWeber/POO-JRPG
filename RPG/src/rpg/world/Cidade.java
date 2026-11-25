package rpg.world;

public class Cidade implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private Loja loja;
    private Taverna taverna;

    public Cidade(String nome) {
        this.nome = nome;
        this.loja = new Loja();
        this.taverna = new Taverna();
    }

    public String getNome() {
        return nome;
    }

    public Loja getLoja() {
        return loja;
    }

    public Taverna getTaverna() {
        return taverna;
    }
}
