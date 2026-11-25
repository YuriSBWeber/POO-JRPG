package rpg.model;

public class Mago extends Personagem {

    private static final long serialVersionUID = 1L;

    public Mago(String nome) {
        super(nome, 1, 75, 80, 14, 3, 5);
    }

    @Override
    public void subirNivel() {
        aumentarAtributos(10, 12, 3, 1, 1);
    }
}
