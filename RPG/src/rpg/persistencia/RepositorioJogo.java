package rpg.persistencia;

import java.io.IOException;
import java.io.EOFException;
import java.io.InvalidClassException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import rpg.Jogo;

public class RepositorioJogo {
    private static final Set<String> CLASSES_PERMITIDAS = Set.of(
            "rpg.Jogo", "rpg.world.Area", "rpg.world.Cidade",
            "rpg.world.Grupo", "rpg.world.Loja", "rpg.world.Taverna",
            "rpg.item.Item", "rpg.item.Inventario", "rpg.item.Arma",
            "rpg.item.Armadura", "rpg.item.Consumivel",
            "rpg.model.Combatente", "rpg.model.Personagem",
            "rpg.model.Guerreiro", "rpg.model.Mago", "rpg.model.Ladino",
            "rpg.model.Clerigo", "rpg.model.Monstro", "rpg.model.Chefe",
            "java.util.ArrayList", "java.lang.Object", "java.lang.String");

    public void salvar(Jogo jogo, Path destino) throws PersistenciaException {
        Path temporario = null;
        try {
            Path arquivo = destino.toAbsolutePath();
            temporario = Files.createTempFile(arquivo.getParent(), "rpg-", ".tmp");
            try (ObjectOutputStream saida = new ObjectOutputStream(Files.newOutputStream(temporario))) {
                saida.writeObject(jogo);
            }
            try {
                Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException | SecurityException e) {
            throw new PersistenciaException("Não foi possível salvar a partida. Verifique o local e as permissões.", e);
        } finally {
            if (temporario != null) {
                try {
                    Files.deleteIfExists(temporario);
                } catch (IOException | SecurityException ignorada) {
                    // A falha principal já é informada ao jogador.
                }
            }
        }
    }

    public Jogo carregar(Path arquivo) throws PersistenciaException {
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(arquivo))) {
            entrada.setObjectInputFilter(info -> {
                if (info.depth() > 50 || info.references() > 10000 || info.streamBytes() > 10_000_000
                        || info.arrayLength() > 10000) {
                    return ObjectInputFilter.Status.REJECTED;
                }
                Class<?> classe = info.serialClass();
                if (classe == null) return ObjectInputFilter.Status.UNDECIDED;
                while (classe.isArray()) classe = classe.getComponentType();
                return classe.isPrimitive() || CLASSES_PERMITIDAS.contains(classe.getName())
                        ? ObjectInputFilter.Status.ALLOWED : ObjectInputFilter.Status.REJECTED;
            });
            Object objeto = entrada.readObject();
            if (!(objeto instanceof Jogo)) {
                throw new ArquivoDeJogoInvalidoException("O arquivo não contém uma partida válida.", null);
            }
            Jogo jogo = (Jogo) objeto;
            if (jogo.getGrupo() == null || jogo.getGrupo().getMembros() == null
                    || jogo.getGrupo().getMembros().isEmpty() || jogo.getCidade() == null
                    || jogo.getAreas() == null || jogo.getAreas().isEmpty()) {
                throw new ArquivoDeJogoInvalidoException("A partida está incompleta ou corrompida.", null);
            }
            return jogo;
        } catch (InvalidClassException | StreamCorruptedException | EOFException | ClassNotFoundException e) {
            throw new ArquivoDeJogoInvalidoException("Arquivo inválido ou incompatível com esta versão do jogo.", e);
        } catch (IOException | SecurityException e) {
            throw new PersistenciaException("Não foi possível ler a partida. Verifique o arquivo e as permissões.", e);
        }
    }
}
