package rpg.world;

import java.util.ArrayList;
import java.util.List;

import rpg.item.Inventario;
import rpg.model.Personagem;

public class Grupo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private List<Personagem> membros;
    private Inventario inventario;

    public Grupo(Personagem protagonista) {
        membros = new ArrayList<>();
        membros.add(protagonista);

        inventario = new Inventario();
    }

    public List<Personagem> getMembros() {
        return membros;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public boolean adicionarAliado(Personagem aliado) {
        if (membros.size() >= 3) {
            return false;
        }

        membros.add(aliado);
        return true;
    }

    public List<Personagem> getMembrosVivos() {
        List<Personagem> vivos = new ArrayList<>();

        for (Personagem personagem : membros) {
            if (personagem.estaVivo()) {
                vivos.add(personagem);
            }
        }

        return vivos;
    }
    public void restaurarGrupoAposBatalha() {
        for (Personagem personagem : membros) {
            personagem.restaurarAposBatalha();
        }
    }
}
