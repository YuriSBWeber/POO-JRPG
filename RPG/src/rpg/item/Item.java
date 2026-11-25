package rpg.item;

public abstract class Item implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private int preco;

    public Item(String nome, int preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public int getPreco() {
        return preco;
    }

    @Override
    public String toString() {
        return nome;
    }
}
