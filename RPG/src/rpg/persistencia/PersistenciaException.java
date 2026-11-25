package rpg.persistencia;

/** Erro ao salvar ou carregar uma partida. */
public class PersistenciaException extends Exception {
    private static final long serialVersionUID = 1L;

    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
