package rpg.model;

public class Ladino extends Personagem {

    private static final long serialVersionUID = 1L;

    public Ladino(String nome) {
        super(nome, 1, 90, 50, 15, 5, 13);
    }

    @Override
    public void subirNivel() {
        aumentarAtributos(13, 7, 3, 1, 3);
    }
}
