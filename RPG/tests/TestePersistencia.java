import java.nio.file.Files;
import java.nio.file.Path;
import rpg.Jogo;
import rpg.item.Arma;
import rpg.item.Consumivel;
import rpg.model.Guerreiro;
import rpg.model.Mago;
import rpg.model.Personagem;
import rpg.persistencia.ArquivoDeJogoInvalidoException;
import rpg.persistencia.PersistenciaException;
import rpg.persistencia.RepositorioJogo;
import rpg.world.ConfiguradorMundo;
import rpg.world.Grupo;

public class TestePersistencia {
    private static void conferir(boolean condicao, String mensagem) {
        if (!condicao) throw new AssertionError(mensagem);
    }

    public static void main(String[] args) throws Exception {
        Path pasta = Files.createTempDirectory("rpg-teste-");
        Path arquivo = pasta.resolve("partida.rpg");
        Path invalido = pasta.resolve("invalido.rpg");
        try {
            Guerreiro heroi = new Guerreiro("Teste");
            Grupo grupo = new Grupo(heroi);
            grupo.adicionarAliado(new Mago("Aliado"));
            Arma arma = new Arma("Espada", 20, 5);
            grupo.getInventario().adicionarArma(arma);
            grupo.getInventario().adicionarConsumivel(new Consumivel("Poção", 5, 10, 0));
            heroi.equiparArma(arma);
            heroi.ganharExperiencia(40);
            heroi.receberDano(8);
            Jogo jogo = new Jogo(grupo);
            ConfiguradorMundo.configurarAreas(jogo);
            jogo.adicionarMoedas(91);
            jogo.entrarNaArea(jogo.getAreas().get(0));
            jogo.concluirAreaAtual();
            jogo.registrarRecrutamento();
            RepositorioJogo repositorio = new RepositorioJogo();
            repositorio.salvar(jogo, arquivo);
            Jogo carregado = repositorio.carregar(arquivo);
            Personagem copia = carregado.getGrupo().getMembros().get(0);
            conferir(carregado.getMoedas() == 91, "moedas");
            conferir(carregado.getGrupo().getMembros().size() == 2, "grupo");
            conferir(copia.getNivel() == heroi.getNivel() && copia.getExperiencia() == heroi.getExperiencia(), "nível e experiência");
            conferir(copia.getHpAtual() == heroi.getHpAtual(), "HP");
            conferir(copia.getArmaEquipada() == carregado.getGrupo().getInventario().getArmas().get(0), "arma e referência compartilhada");
            conferir(carregado.getGrupo().getInventario().getConsumiveis().size() == 1, "inventário");
            conferir(carregado.getAreas().get(1).isDesbloqueada() && !carregado.podeRecrutarAliado(), "progresso");
            repositorio.salvar(carregado, arquivo);
            conferir(repositorio.carregar(arquivo).getMoedas() == 91, "substituição");
            Files.writeString(invalido, "conteúdo inválido");
            try {
                repositorio.carregar(invalido);
                throw new AssertionError("Arquivo inválido aceito");
            } catch (ArquivoDeJogoInvalidoException esperado) { }
            try {
                repositorio.carregar(pasta.resolve("inexistente.rpg"));
                throw new AssertionError("Arquivo inexistente aceito");
            } catch (PersistenciaException esperado) { }
            try {
                repositorio.salvar(jogo, pasta.resolve("inexistente").resolve("partida.rpg"));
                throw new AssertionError("Salvamento em pasta inexistente aceito");
            } catch (PersistenciaException esperado) { }
            System.out.println("Testes de persistência passaram.");
        } finally {
            Files.deleteIfExists(arquivo);
            Files.deleteIfExists(invalido);
            Files.deleteIfExists(pasta);
        }
    }
}
