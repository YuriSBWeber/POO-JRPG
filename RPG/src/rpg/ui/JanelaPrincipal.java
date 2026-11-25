package rpg.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import rpg.combat.AcaoCombate;
import rpg.combat.Cura;
import rpg.combat.Furia;
import rpg.combat.GeradorAcoes;
import rpg.combat.Santuario;
import rpg.combat.UsarConsumivel;
import rpg.item.Item;
import rpg.item.Arma;
import rpg.item.Armadura;
import rpg.item.Consumivel;
import rpg.item.Inventario;
import rpg.model.Combatente;
import rpg.Jogo;
import rpg.combat.Batalha;
import rpg.combat.GeradorEncontros;
import rpg.model.Clerigo;
import rpg.model.Guerreiro;
import rpg.model.Ladino;
import rpg.model.Mago;
import rpg.model.Monstro;
import rpg.model.Personagem;
import rpg.persistencia.PersistenciaException;
import rpg.persistencia.RepositorioJogo;
import rpg.world.Area;
import rpg.world.ConfiguradorMundo;
import rpg.world.Grupo;

public class JanelaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;

    private Jogo jogoAtual;
    private Batalha batalhaAtual;

    public JanelaPrincipal() {
        setTitle("RPG de POO");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mostrarMenuPrincipal();
    }

    private void mostrarMenuPrincipal() {
        JPanel painel = new JPanel(new BorderLayout());

        JLabel titulo = new JLabel("RPG de POO", SwingConstants.CENTER);
        painel.add(titulo, BorderLayout.NORTH);

        JPanel botoes = new JPanel(new FlowLayout());

        JButton novoJogo = new JButton("Novo Jogo");
        JButton carregarJogo = new JButton("Carregar Jogo");
        JButton sair = new JButton("Sair");

        novoJogo.addActionListener(evento -> criarNovoJogo());

        carregarJogo.addActionListener(evento -> carregarJogo());

        sair.addActionListener(evento -> System.exit(0));

        botoes.add(novoJogo);
        botoes.add(carregarJogo);
        botoes.add(sair);

        painel.add(botoes, BorderLayout.CENTER);

        setContentPane(painel);
        revalidate();
        repaint();
    }

    private void criarNovoJogo() {
        String nome = JOptionPane.showInputDialog(this,
                "Digite o nome do personagem:");

        if (nome == null || nome.isBlank()) {
            return;
        }

        String[] classes = {
            "Guerreiro",
            "Mago",
            "Ladino",
            "Clérigo"
        };

        String escolha = (String) JOptionPane.showInputDialog(
                this,
                "Escolha uma classe:",
                "Criação de personagem",
                JOptionPane.QUESTION_MESSAGE,
                null,
                classes,
                classes[0]);

        if (escolha == null) {
            return;
        }

        Personagem protagonista;

        switch (escolha) {
            case "Mago":
                protagonista = new Mago(nome);
                break;
            case "Ladino":
                protagonista = new Ladino(nome);
                break;
            case "Clérigo":
                protagonista = new Clerigo(nome);
                break;
            default:
                protagonista = new Guerreiro(nome);
                break;
        }

        Grupo grupo = new Grupo(protagonista);
        jogoAtual = new Jogo(grupo);
        ConfiguradorMundo.configurarAreas(jogoAtual);

        mostrarCidade();
    }

    private void mostrarCidade() {
        JPanel painel = new JPanel(new BorderLayout());

        Personagem protagonista = jogoAtual.getGrupo()
                .getMembros().get(0);

        JTextArea informacoes = new JTextArea();
        informacoes.setEditable(false);

        informacoes.setText(
                "CIDADE INICIAL\n\n"
                + "Moedas: " + jogoAtual.getMoedas() + "\n\n"
                + "PROTAGONISTA\n"
                + "Nome: " + protagonista.getNome() + "\n"
                + "Nível: " + protagonista.getNivel() + "\n"
                + "HP: " + protagonista.getHpAtual() + "/"
                + protagonista.getHpMaximoTotal() + "\n"
                + "MP: " + protagonista.getMpAtual() + "/"
                + protagonista.getMpMaximo() + "\n"
                + "Ataque: " + protagonista.getAtaqueTotal() + "\n"
                + "Defesa: " + protagonista.getDefesaTotal() + "\n"
                + "Velocidade: " + protagonista.getVelocidade()
        );

        painel.add(new JScrollPane(informacoes), BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout());

        JButton escolherArea = new JButton("Escolher Área");
        JButton taverna = new JButton("Taverna");
        JButton loja = new JButton("Loja");
        JButton inventario = new JButton("Inventário");
        JButton salvar = new JButton("Salvar Jogo");
        JButton menu = new JButton("Menu Principal");

        escolherArea.addActionListener(evento -> escolherArea());

        taverna.addActionListener(evento -> abrirTaverna());
        salvar.addActionListener(evento -> salvarJogo());

        loja.addActionListener(evento -> abrirLoja());
        inventario.addActionListener(evento -> abrirInventario());

        menu.addActionListener(evento -> mostrarMenuPrincipal());

        botoes.add(escolherArea);
        botoes.add(taverna);
        botoes.add(loja);
        botoes.add(inventario);
        botoes.add(salvar);
        botoes.add(menu);

        painel.add(botoes, BorderLayout.SOUTH);

        setContentPane(painel);
        revalidate();
        repaint();
    }

    private void abrirLoja() {
        while (true) {
            List<Item> catalogo = jogoAtual.getCidade().getLoja().listarCatalogo(jogoAtual);
            String[] descricoes = new String[catalogo.size()];
            for (int i = 0; i < catalogo.size(); i++)
                descricoes[i] = catalogo.get(i) + " — " + catalogo.get(i).getPreco() + " moedas";
            String escolha = (String) JOptionPane.showInputDialog(this,
                    "Moedas: " + jogoAtual.getMoedas() + " | Inventário: "
                    + jogoAtual.getGrupo().getInventario().tamanho() + "/20\nEscolha o item para comprar:",
                    "Loja", JOptionPane.PLAIN_MESSAGE, null, descricoes, descricoes[0]);
            if (escolha == null) break;
            int indice = java.util.Arrays.asList(descricoes).indexOf(escolha);
            try {
                jogoAtual.getCidade().getLoja().comprar(jogoAtual, catalogo.get(indice));
                JOptionPane.showMessageDialog(this, "Compra realizada!");
            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Compra não realizada", JOptionPane.WARNING_MESSAGE);
            }
        }
        mostrarCidade();
    }

    private void abrirInventario() {
        Inventario inventario = jogoAtual.getGrupo().getInventario();
        List<Personagem> membros = jogoAtual.getGrupo().getMembros();
        while (true) {
            StringBuilder resumo = new StringBuilder("Itens: " + inventario.tamanho() + "/20\n");
            for (Personagem membro : membros) {
                resumo.append("\n").append(membro.getNome()).append(": arma: ")
                        .append(membro.getArmaEquipada() == null ? "vazia" : membro.getArmaEquipada().getNome())
                        .append(" | armadura: ")
                        .append(membro.getArmaduraEquipada() == null ? "vazia" : membro.getArmaduraEquipada().getNome());
            }
            resumo.append("\n\nConsumíveis equipados (").append(inventario.getConsumiveisEquipados().size())
                    .append("/3): ").append(inventario.getConsumiveisEquipados());
            String[] opcoes = {"Equipar item", "Desequipar item", "Descartar item", "Voltar"};
            int opcao = JOptionPane.showOptionDialog(this, resumo, "Inventário", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]);
            if (opcao < 0 || opcao == 3) break;
            if (opcao == 0) equiparItem(inventario, membros);
            else if (opcao == 1) desequiparItem(inventario, membros);
            else descartarItem(inventario, membros);
        }
        mostrarCidade();
    }

    private Item selecionarItem(String titulo, List<? extends Item> itens) {
        if (itens.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum item disponível.");
            return null;
        }
        return (Item) JOptionPane.showInputDialog(this, "Selecione um item:", titulo,
                JOptionPane.PLAIN_MESSAGE, null, itens.toArray(), itens.get(0));
    }

    private Personagem selecionarMembro(List<Personagem> membros) {
        return (Personagem) JOptionPane.showInputDialog(this, "Selecione o personagem:", "Grupo",
                JOptionPane.PLAIN_MESSAGE, null, membros.toArray(), membros.get(0));
    }

    private void equiparItem(Inventario inventario, List<Personagem> membros) {
        Item item = selecionarItem("Equipar", inventario.getItens());
        if (item == null) return;
        try {
            if (item instanceof Consumivel) inventario.equiparConsumivel((Consumivel) item);
            else {
                Personagem membro = selecionarMembro(membros);
                if (membro != null) inventario.equiparItem(membro, item, membros);
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Não foi possível equipar", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void desequiparItem(Inventario inventario, List<Personagem> membros) {
        List<Item> equipados = new ArrayList<>();
        for (Personagem membro : membros) {
            if (membro.getArmaEquipada() != null) equipados.add(membro.getArmaEquipada());
            if (membro.getArmaduraEquipada() != null) equipados.add(membro.getArmaduraEquipada());
        }
        equipados.addAll(inventario.getConsumiveisEquipados());
        Item item = selecionarItem("Desequipar", equipados);
        if (item == null) return;
        if (item instanceof Consumivel) inventario.desequiparConsumivel((Consumivel) item);
        for (Personagem membro : membros) {
            if (membro.getArmaEquipada() == item) membro.equiparArma(null);
            if (membro.getArmaduraEquipada() == item) membro.equiparArmadura(null);
        }
    }

    private void descartarItem(Inventario inventario, List<Personagem> membros) {
        Item item = selecionarItem("Descartar", inventario.getItens());
        if (item != null && JOptionPane.showConfirmDialog(this, "Descartar " + item.getNome() + "?",
                "Descartar item", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            inventario.descartar(item, membros);
    }

    private void salvarJogo() {
        JFileChooser seletor = new JFileChooser();
        seletor.setDialogTitle("Salvar partida");
        seletor.setSelectedFile(new java.io.File("partida.rpg"));
        if (seletor.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        Path arquivo = seletor.getSelectedFile().toPath();
        if (Files.exists(arquivo) && JOptionPane.showConfirmDialog(this,
                "O arquivo já existe. Deseja substituí-lo?", "Confirmar substituição",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            new RepositorioJogo().salvar(jogoAtual, arquivo);
            JOptionPane.showMessageDialog(this, "Partida salva com sucesso.");
        } catch (PersistenciaException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro ao salvar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarJogo() {
        JFileChooser seletor = new JFileChooser();
        seletor.setDialogTitle("Carregar partida");
        if (seletor.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try {
            Jogo carregado = new RepositorioJogo().carregar(seletor.getSelectedFile().toPath());
            jogoAtual = carregado;
            batalhaAtual = null;
            mostrarCidade();
        } catch (PersistenciaException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro ao carregar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void escolherArea() {
        List<Area> areasLiberadas = new ArrayList<>();

        for (Area area : jogoAtual.getAreas()) {
            if (area.isDesbloqueada()) {
                areasLiberadas.add(area);
            }
        }

        String[] opcoes = new String[areasLiberadas.size()];

        for (int i = 0; i < areasLiberadas.size(); i++) {
            Area area = areasLiberadas.get(i);

            opcoes[i] = area.getNome()
                    + " - nível recomendado "
                    + area.getNivelRecomendado();
        }

        String escolha = (String) JOptionPane.showInputDialog(
                this,
                "Escolha uma área:",
                "Áreas disponíveis",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]);

        if (escolha == null) {
            return;
        }

        for (int i = 0; i < opcoes.length; i++) {
            if (opcoes[i].equals(escolha)) {
                jogoAtual.entrarNaArea(areasLiberadas.get(i));
                iniciarBatalhaComum();
                return;
            }
        }
    }

    private void iniciarBatalhaComum() {
        GeradorEncontros gerador = new GeradorEncontros();

        batalhaAtual = new Batalha(
                jogoAtual.getGrupo(),
                gerador.criarEncontroComum(jogoAtual.getAreaAtual())
        );

        mostrarBatalha(false);
    }

    private void iniciarBatalhaDeChefe() {
        List<Monstro> chefe = new ArrayList<>();

        chefe.add(jogoAtual.getAreaAtual().getChefe().criarCopia());

        batalhaAtual = new Batalha(jogoAtual.getGrupo(), chefe);

        mostrarBatalha(true);
    }

    private void mostrarBatalha(boolean batalhaDeChefe) {
        JPanel painel = new JPanel(new BorderLayout());

        JTextArea status = new JTextArea();
        status.setEditable(false);

        String texto = batalhaDeChefe
                ? "BATALHA CONTRA CHEFE\n\n"
                : "BATALHA COMUM\n\n";

        texto += "GRUPO\n";

        for (Personagem personagem : jogoAtual.getGrupo().getMembros()) {
            texto += personagem.getNome()
                    + " - HP " + personagem.getHpAtual()
                    + "/" + personagem.getHpMaximo()
                    + " - MP " + personagem.getMpAtual()
                    + "/" + personagem.getMpMaximo()
                    + "\n";
        }

        texto += "\nINIMIGOS\n";

        for (Monstro monstro : batalhaAtual.getMonstros()) {
            if (monstro.estaVivo()) {
                texto += monstro + "\n";
            }
        }
        texto += "\nREGISTRO DE AÇÕES\n";

        for (String mensagem : batalhaAtual.getRegistro()) {
            texto += mensagem + "\n";
        }
        status.setText(texto);

        painel.add(new JScrollPane(status), BorderLayout.CENTER);

        JPanel controles = new JPanel(new FlowLayout());

        Personagem protagonista = jogoAtual.getGrupo()
                .getMembros().get(0);

        JComboBox<AcaoCombate> acoes = new JComboBox<>();

        GeradorAcoes geradorAcoes = new GeradorAcoes();

        for (AcaoCombate acao : geradorAcoes.criarAcoes(protagonista)) {
            acoes.addItem(acao);
        }
        for (Consumivel item : jogoAtual.getGrupo().getInventario().getConsumiveisEquipados()) {
            acoes.addItem(new UsarConsumivel(item, jogoAtual.getGrupo().getInventario()));
        }

        JComboBox<Combatente> alvos = new JComboBox<>();

        atualizarAlvos(acoes, alvos, protagonista);

        acoes.addActionListener(evento ->
                atualizarAlvos(acoes, alvos, protagonista));

        JButton agir = new JButton("Executar Ação");

        agir.addActionListener(evento -> {
            AcaoCombate acao = (AcaoCombate) acoes.getSelectedItem();
            Combatente alvo = (Combatente) alvos.getSelectedItem();

            if (acao == null || alvo == null) {
                JOptionPane.showMessageDialog(this, "Não há alvo válido para esta ação.");
                return;
            }

            if (protagonista.estaVivo() && protagonista.getMpAtual() < acao.getCustoMp()) {
                JOptionPane.showMessageDialog(this, "MP insuficiente para esta ação.");
                return;
            }

            batalhaAtual.executarRodada(acao, alvo);

            if (batalhaAtual.grupoFoiDerrotado()) {
                JOptionPane.showMessageDialog(this,
                        "O grupo foi derrotado.");
                jogoAtual.getGrupo().restaurarGrupoAposBatalha();
                mostrarCidade();
                return;
            }

            if (batalhaAtual.todosMonstrosForamDerrotados()) {
                finalizarVitoria(batalhaDeChefe);
                return;
            }

            mostrarBatalha(batalhaDeChefe);
        });

        controles.add(new JLabel("Ação:"));
        controles.add(acoes);
        controles.add(new JLabel("Alvo:"));
        controles.add(alvos);
        controles.add(agir);

        painel.add(controles, BorderLayout.SOUTH);

        setContentPane(painel);
        revalidate();
        repaint();
    }

    private void finalizarVitoria(boolean batalhaDeChefe) {
        int experiencia = 0;
        int moedas = 0;

        for (Monstro monstro : batalhaAtual.getMonstros()) {
            experiencia += monstro.getExperienciaRecompensa();
            moedas += monstro.getMoedasRecompensa();
        }

        for (Personagem personagem : jogoAtual.getGrupo().getMembros()) {
            personagem.ganharExperiencia(experiencia);
        }

        jogoAtual.adicionarMoedas(moedas);
        jogoAtual.getGrupo().restaurarGrupoAposBatalha();

        if (batalhaDeChefe) {
            boolean chefeFinal = jogoAtual.getAreaAtual()
                    .getNome().equals("Montanha");

            jogoAtual.concluirAreaAtual();

            if (chefeFinal) {
                JOptionPane.showMessageDialog(this,
                        "Parabéns! Você derrotou o chefe final!");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Chefe derrotado! Uma nova área foi liberada.");
            }

            mostrarCidade();
            return;
        }

        jogoAtual.registrarVitoriaComum();

        if (jogoAtual.deveEnfrentarChefe()) {
            JOptionPane.showMessageDialog(this,
                    "Cinco batalhas vencidas. Agora enfrente o chefe!");
            iniciarBatalhaDeChefe();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Vitória! Próxima batalha da área.");
            iniciarBatalhaComum();
        }
    }
    private void atualizarAlvos(JComboBox<AcaoCombate> acoes,
            JComboBox<Combatente> alvos, Personagem protagonista) {

        alvos.removeAllItems();

        AcaoCombate acao = (AcaoCombate) acoes.getSelectedItem();

        if (acao instanceof UsarConsumivel) {
            Consumivel item = ((UsarConsumivel) acao).getItem();
            for (Personagem personagem : jogoAtual.getGrupo().getMembros()) {
                if (item.podeUsar(personagem)) alvos.addItem(personagem);
            }
        } else if (acao instanceof Cura) {
            for (Personagem personagem : jogoAtual.getGrupo()
                    .getMembrosVivos()) {

                alvos.addItem(personagem);
            }

        } else if (acao instanceof Furia
                || acao instanceof Santuario) {

            alvos.addItem(protagonista);

        } else {
            for (Monstro monstro : batalhaAtual.getMonstros()) {
                if (monstro.estaVivo()) {
                    alvos.addItem(monstro);
                }
            }
        }
    }
    private void abrirTaverna() {
        if (!jogoAtual.podeRecrutarAliado()) {
            JOptionPane.showMessageDialog(this,
                    "Nenhum aliado está disponível agora.\n"
                    + "Derrote o chefe da Planície ou da Floresta.");
            return;
        }

        List<Personagem> opcoes = jogoAtual.getCidade()
                .getTaverna()
                .gerarOpcoes(jogoAtual.getGrupo());

        Personagem escolhido = (Personagem) JOptionPane.showInputDialog(
                this,
                "Escolha um aliado:",
                "Taverna",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes.toArray(),
                opcoes.get(0));

        if (escolhido == null) {
            return;
        }

        boolean entrouNoGrupo = jogoAtual.getGrupo()
                .adicionarAliado(escolhido);

        if (entrouNoGrupo) {
            jogoAtual.registrarRecrutamento();

            JOptionPane.showMessageDialog(this,
                    escolhido.getNome() + " entrou no grupo!");

            mostrarCidade();
        }
    }
}
