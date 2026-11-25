package rpg.model;

public class Clerigo extends Personagem {

    private static final long serialVersionUID = 1L;

    public Clerigo(String nome) {
        super(nome, 1, 100, 70, 12, 6, 7);
    }

    @Override
    public void subirNivel() {
        aumentarAtributos(15, 10, 2, 1, 2);
    }
}
