package rpg.model;

public class Guerreiro extends Personagem {

    private static final long serialVersionUID = 1L;

    public Guerreiro(String nome) {
        super(nome, 1, 125, 35, 17, 10, 8);
    }

    @Override
    public void subirNivel() {
        aumentarAtributos(18, 5, 3, 2, 2);
    }
}
