/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interface;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author User
 */
public class Controle {

    private final Tela t;
    private final JTextArea editor;
    private final JTextArea mensagem;
    private final JLabel status;
    private File arquivoAtual = null;
    private boolean arquivoModificado = false;

    JFileChooser fcSalvarArquivo;
    JFileChooser fcAbrirArquivo;

    public Controle(Tela t, JTextArea editor, JTextArea mensagem, JLabel status) {
        this.t = t;
        this.editor = editor;
        this.mensagem = mensagem;
        this.status = status;

        editor.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                arquivoModificado = true;
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                arquivoModificado = true;
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                arquivoModificado = true;
            }
        });
    }

    public void buttonNovo() {
        editor.setText("");
        mensagem.setText("");
        status.setText("");
        arquivoAtual = null;
    }

    public void buttonAbrir() {
        fcAbrirArquivo = new JFileChooser();
        fcAbrirArquivo.setFileFilter(new FileNameExtensionFilter("Arquivos de texto (*.txt)", "txt"));
        int res = fcAbrirArquivo.showOpenDialog(this.t);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fcAbrirArquivo.getSelectedFile();
            try {
                String content = new String(java.nio.file.Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                editor.setText(content);
                mensagem.setText("");
                arquivoAtual = file;
                status.setText(file.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this.t, "Erro ao abrir: " + ex.getMessage());
            }
        }
    }

    public void buttonSalvar(boolean salvarComo) {
        try {
            if (arquivoAtual == null || salvarComo) {
                fcSalvarArquivo = new JFileChooser();
                fcSalvarArquivo.setFileFilter(new FileNameExtensionFilter("Arquivos de texto (*.txt)", "txt"));
                int res = fcSalvarArquivo.showSaveDialog(this.t);
                if (res == JFileChooser.APPROVE_OPTION) {
                    File file = fcSalvarArquivo.getSelectedFile();
                    if (!file.getName().endsWith(".txt")) {
                        file = new File(file.getAbsolutePath() + ".txt");
                    }
                    java.nio.file.Files.write(file.toPath(), editor.getText().getBytes(StandardCharsets.UTF_8));
                    arquivoAtual = file;
                    mensagem.setText("");
                    status.setText(file.getAbsolutePath());
                    arquivoModificado = false;
                }
            } else {
                java.nio.file.Files.write(arquivoAtual.toPath(), editor.getText().getBytes(StandardCharsets.UTF_8));
                mensagem.setText("");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.t, "Erro ao salvar: " + ex.getMessage());

        }
    }

    public void buttonCopiar() {
        editor.copy();
    }

    public void buttonColar() {
        editor.paste();
    }

    public void buttonRecortar() {
        editor.cut();
    }

    public void buttonCompilar(String codigoFonte) throws IOException {
        mensagem.setText("");
        
        Lexico lexico = new Lexico();
        lexico.setInput(codigoFonte);
        
        Sintatico sintatico = new Sintatico();

        Semantico semantico = new Semantico();
        semantico.setArquivoFonte(arquivoAtual);
        
        if (arquivoAtual == null) {
            int opcao = JOptionPane.showConfirmDialog(this.t,
                    "O arquivo ainda não foi salvo. Obrigatório salvo-lo para compilar",
                    "Salvar arquivo",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (opcao == JOptionPane.YES_OPTION) {
                buttonSalvar(arquivoModificado);
            } else {
                return;
            }
        }
        
        /*if (arquivoModificado) {
            int opcao = JOptionPane.showConfirmDialog(this.t, 
                    "O arquivo foi modificado. Deseja salvar as alterações",
                    "Arquivo modificado",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (opcao == JOptionPane.YES_OPTION) {
                buttonSalvar(arquivoModificado);
            } else {
                return;
            }
        }*/

        try {
            System.out.println("Chamando parser...");
            sintatico.parse(lexico, semantico);    // tradução dirigida pela sintaxe
            System.out.println("Parser executado com sucesso!");

            System.out.println("Gerando arquivo IL...");
            semantico.gerarArquivoIL();
            
            mensagem.setText("Programa compilado com sucesso.");
            
        } // mensagem: programa compilado com sucesso - na área reservada para mensagens
        catch (LexicalError e) {
            int linha = getLinhaPosicao(codigoFonte, e.getPosition());

            char simbolo = codigoFonte.charAt(e.getPosition());

            if (e.getMessage() == "símbolo inválido") {
                mensagem.setText("linha " + linha + ":  " + simbolo + "  " + e.getMessage());
            } else {
                mensagem.setText("linha " + linha + ":  " + e.getMessage());
            }

        } catch (SyntaticError e) {
            int linha = getLinhaPosicao(codigoFonte, e.getPosition());

            String tokenEncontrado = getTokenEncontrado(codigoFonte, e.getPosition());

            mensagem.setText("linha " + linha + ": encontrado  " + tokenEncontrado + "  " + e.getMessage());

            // e.getMessage() são os símbolos esperados
            // e.getMessage() - retorna a mensagem de erro de PARSER_ERROR (ver ParserConstants.java)
            // necessário adaptar conforme o enunciado da parte 3
            // e.getPosition() - retorna a posição inicial do erro 
            // necessário adaptar para mostrar a linha  
            // necessário mostrar também o símbolo encontrado 
        } catch (SemanticError e) {
            int linha = getLinhaPosicao(codigoFonte, e.getPosition());
            
            mensagem.setText("linha " + linha + ": " + e.getMessage());
        } catch (IOException e) {
            
        }
    }

    public void buttonEquipe() {
        mensagem.setText("");
        mensagem.setText("Equipe: Guilherme Bez e Gustavo Baum.");
    }

    public int getLinhaPosicao(String codigo, int pos) {
        int linha = 1;
        for (int i = 0; i < pos && i < codigo.length(); i++) {
            if (codigo.charAt(i) == '\n') {
                linha++;
            }
        }
        return linha;
    }

    public String getDocumento() {
        return arquivoAtual.getName();
    }

    private String getTokenEncontrado(String codigoFonte, int position) {
        if (position >= codigoFonte.length()) {
            return "EOF";
        }

        try {
            Lexico lexicoAux = new Lexico();
            lexicoAux.setInput(codigoFonte.substring(position));
            Token token = lexicoAux.nextToken();

            if (token != null) {
                return formatarTokenParaMensagem(token);
            }
        } catch (Exception ex) {

        }
        char c = codigoFonte.charAt(position);
        return "'" + c + "'";
    }

    private String formatarTokenParaMensagem(Token token) {
        switch (token.getIdporExtenso()) {
            case "constante_string":
                return "constante_string";
            case "constante_int":
                return token.getLexeme();
            case "constante_float":
                return token.getLexeme();
            case "identificador":
                return token.getLexeme();
            default:
                return token.getLexeme(); // para demais símbolos
        }
    }
}
