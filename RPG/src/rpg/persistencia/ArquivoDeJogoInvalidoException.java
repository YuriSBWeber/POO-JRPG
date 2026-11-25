package rpg.persistencia;

/** O arquivo selecionado não contém uma partida compatível. */
public class ArquivoDeJogoInvalidoException extends PersistenciaException {
    private static final long serialVersionUID = 1L;

    public ArquivoDeJogoInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
