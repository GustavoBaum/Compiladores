//1o passo: Implemantar ações #100 - #105;
//
//2o passo: Editar programa e salvar com o nome teste01.txt
//
//begin
// print(9, 3.5, "oi");
//end
//
//3o passo: compilado o programa, deve ser gerado arquivo teste01.il
package Interface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Semantico implements Constants {

    //pilha_tipos : Pilha
    //codigo : String
    private final StringBuilder codigo = new StringBuilder();
    private Stack<String> pilha_tipos = new Stack<>();

    private String operador_relacional = "";
    private String tipo_atual = "";
    private Integer contador_rotulos = 0;
    private List<String> variaveisLocais = new ArrayList<>();
    private List<String> lista_identificadores = new ArrayList<>();
    private Map<String, String> tabela_simbolos = new HashMap<>();
    private Stack<String> pilha_rotulos = new Stack();

    private File arquivoFonte;

    public void setArquivoFonte(File f) {
        this.arquivoFonte = f;
    }

    public void executeAction(int action, Token token) throws SemanticError {

        switch (action) {
            case 100:
                callAction100();
                break;
            case 101:
                callAction101();
                break;
            case 102:
                callAction102();
                break;
            case 103:
                callAction103(token);
                break;
            case 104:
                callAction104(token);
                break;
            case 105:
                callAction105(token);
                break;
            case 106:
                callAction106();
                break;
            case 107:
                callAction107();
                break;
            case 108:
                callAction108();
                break;
            case 109:
                callAction109();
                break;
            case 110:
                callAction110(token);
                break;
            case 111:
                callAction111(token);
                break;
            case 112:
                callAction112();
                break;
            case 113:
                callAction113();
                break;
            case 114:
                callAction114();
                break;
            case 115:
                callAction115(token);
                break;
            case 116:
                callAction116(token);
                break;
            case 117:
                callAction117(token);
                break;
            case 118:
                callAction118();
                break;
            case 119:
                callAction119();
                break;
            case 120:
                callAction120(token);
                break;
            case 121:
                callAction121(token);
                break;
            case 122:
                callAction122(token);
                break;
            case 123:
                callAction123(token);
                break;
            case 124:
                callAction124(token);
                break;
            case 125:
                callAction125();
                break;
            case 126:
                callAction126();
                break;
            case 127:
                callAction127();
                break;
            case 128:
                callAction128(token);
                break;
            case 129:
                callAction129(token);
                break;
            case 130:
                callAction130(token);
                break;
            default:
                break;
        }

        //escolha action
        //100: ação100;
        //101: ação101;
        //102: ação102;
        //103: ação103 (token);
        //104: ação104 (token);
        //105: ação105 (token);
        //fim
        //SALVAR AS AÇÕES EM UM ARQUIVO .txt!!!!!!!
        //QUANDO COMPILAR GERAR O .il NA MESMA PASTA DO .txt!!!!!
        System.out.println("Ação #" + action + ", Token: " + token);
    }

    private void callAction100() {
        codigo.setLength(0);
        codigo.append(".assembly extern mscorlib {}\n"
                + ".assembly programa{}\n"
                + ".module programa.exe\n"
                + "\n"
                + "class public _unica{\n"
                + "  .method static public void _principal(){\n"
                + "  .entrypoint\n");
    }

    private void callAction101() {
        // Inserir .locals após .entrypoint se houver variáveis
        if (!variaveisLocais.isEmpty()) {
            StringBuilder localsBuilder = new StringBuilder();
            localsBuilder.append("    .locals (");

            for (int i = 0; i < variaveisLocais.size(); i++) {
                if (i > 0) {
                    localsBuilder.append(", ");
                }
                localsBuilder.append(variaveisLocais.get(i));
            }
            localsBuilder.append(")\n");

            String codigoCompleto = codigo.toString();
            int posEntrypoint = codigoCompleto.indexOf(".entrypoint") + ".entrypoint".length();

            String novoCodigo = codigoCompleto.substring(0, posEntrypoint) + "\n"
                    + localsBuilder.toString()
                    + codigoCompleto.substring(posEntrypoint);

            codigo.setLength(0);
            codigo.append(novoCodigo);
        }

        codigo.append("    ret\n");
        codigo.append("  }\n");
        codigo.append("}\n");
    }

    private void callAction102() {
        if (pilha_tipos.isEmpty()) {
            return;
        }

        String tipo = pilha_tipos.pop();

        switch (tipo) {
            case "int64":
                codigo.append("    conv.i8\n");
                codigo.append("    call void [mscorlib]System.Console::Write(int64)\n");
                break;
            case "float64":
                codigo.append("    call void [mscorlib]System.Console::Write(float64)\n");
                break;
            case "string":
                codigo.append("    call void [mscorlib]System.Console::Write(string)\n");
                break;
            case "bool":
                codigo.append("    call void [mscorlib]System.Console::Write(bool)\n");
                break;
        }
        codigo.append("    ldstr \"\\n\"\n");
        codigo.append("    call void [mscorlib]System.Console::Write(string)\n");
    }

    private void callAction103(Token token) {
        pilha_tipos.push("int64");
        codigo.append("    ldc.i8 ").append(token.getLexeme()).append("\n");
        codigo.append("    conv.r8\n");
    }

    private void callAction104(Token token) {
        pilha_tipos.push("float64");
        codigo.append("    ldc.r8 ").append(token.getLexeme()).append("\n");
    }

    private void callAction105(Token token) {
        pilha_tipos.push("string");
        codigo.append("    ldstr ").append(token.getLexeme()).append("\n");
    }

    private void callAction106() {
        verificarPilhaTipos(2);
        String tipo2 = pilha_tipos.pop();
        String tipo1 = pilha_tipos.pop();
        String tipoResultante = determinaTipoResultado(tipo1, tipo2, "+");
        pilha_tipos.push(tipoResultante);
        codigo.append("    add\n");
    }

    private void callAction107() {
        String tipo2 = pilha_tipos.pop();
        String tipo1 = pilha_tipos.pop();
        String tipoResultante = determinaTipoResultado(tipo1, tipo2, "-");
        pilha_tipos.push(tipoResultante);
        codigo.append("    sub\n");
    }

    private void callAction108() {
        String tipo2 = pilha_tipos.pop();
        String tipo1 = pilha_tipos.pop();
        String tipoResultante = determinaTipoResultado(tipo1, tipo2, "*");
        pilha_tipos.push(tipoResultante);
        codigo.append("    mul\n");
    }

    private void callAction109() {
        String tipo2 = pilha_tipos.pop();
        String tipo1 = pilha_tipos.pop();

        String tipoResultado = determinaTipoResultado(tipo1, tipo2, "/");
        pilha_tipos.push(tipoResultado);

        codigo.append("     div\n");
    }

    private void callAction110(Token token) {
        codigo.append("     ldc.i8 -1\n");
        codigo.append("     conv.r8\n");
        codigo.append("     mul\n");
    }

    private void callAction111(Token token) {
        operador_relacional = token.getLexeme();
    }

    private void callAction112() {
        String tipo1 = pilha_tipos.pop();
        String tipo2 = pilha_tipos.pop();
        pilha_tipos.push("bool");

        switch (operador_relacional) {
            case "==":
                codigo.append("     ceq\n");
                break;
            case "~=":
                codigo.append("     ceq\n");
                codigo.append("     ldc.i4.1\n");
                codigo.append("     xor\n");
                break;
            case "<":
                codigo.append("     clt\n");
                break;
            case ">":
                codigo.append("     cgt\n");
                break;
            default:
                break;
        }
        operador_relacional = "";
    }

    private void callAction113() {
        String tipo2 = pilha_tipos.pop();
        String tipo1 = pilha_tipos.pop();
        pilha_tipos.push("bool");
        codigo.append("     and\n");
    }

    private void callAction114() {
        String tipo2 = pilha_tipos.pop();
        String tipo1 = pilha_tipos.pop();
        pilha_tipos.push("bool");
        codigo.append("     or\n");
    }

    private void callAction115(Token token) {
        pilha_tipos.push("bool");
        codigo.append("    ldc.i4.1\n");
    }

    private void callAction116(Token token) {
        pilha_tipos.push("bool");
        codigo.append("    ldc.i4.0\n");
    }

    private void callAction117(Token token) {
        codigo.append("     ldc.i4.1\n");
        codigo.append("     xor\n");
    }

    private void callAction118() {
        codigo.append("\n");
    }

    private void callAction119() {
        if (!lista_identificadores.isEmpty()) {
            for (String id : lista_identificadores) {
                String tipoIL = converterTipoIL(tipo_atual);
                tabela_simbolos.put(id, tipoIL);

                String declaracao = tipoIL + " " + id;
                if (!variaveisLocais.contains(declaracao)) {
                    variaveisLocais.add(declaracao);
                }
            }
            lista_identificadores.clear();
        }
        tipo_atual = "";
    }

    private void callAction120(Token token) {
        tipo_atual = token.getLexeme();
    }

    private void callAction121(Token token) {
        lista_identificadores.add(token.getLexeme());
    }

    private void callAction122(Token token) {
        String tipoExpressao = pilha_tipos.pop();
        String id = lista_identificadores.remove(0);

        if ("int64".equals(tipoExpressao)) {
            codigo.append("    conv.i8\n");
        }

        codigo.append("    stloc ").append(id).append("\n");
        lista_identificadores.clear();
    }

    private void callAction123(Token token) throws SemanticError {
        String id = token.getLexeme();
        String tipoVar = tabela_simbolos.get(id);

        if (tipoVar == null) {
            throw new SemanticError("identificador não declarado: " + id, token.getPosition());
        }

        if ("bool".equals(tipoVar)) {
            throw new SemanticError(id + " inválido para comando de entrada", token.getPosition());
        }

        codigo.append("    call string [mscorlib]System.Console::ReadLine()\n");

        switch (tipoVar) {
            case "int64":
                codigo.append("    call int64 [mscorlib]System.Int64::Parse(string)\n");
                break;
            case "float64":
                codigo.append("    call float64 [mscorlib]System.Double::Parse(string)\n");
                break;
            case "string":
                break;
        }

        codigo.append("    stloc ").append(id).append("\n");
    }

    private void callAction124(Token token) {
        pilha_tipos.push("string");
        codigo.append("    ldstr ").append(token.getLexeme()).append("\n");
        codigo.append("    call void [mscorlib]System.Console::Write(string)\n");
    }

    private void callAction125() throws SemanticError {
        if (pilha_tipos.isEmpty()) {
            throw new SemanticError("expressão esperada em comando de seleção", -1);
        }

        String tipoExpressao = pilha_tipos.pop();

        if (!"bool".equals(tipoExpressao)) {
            throw new SemanticError("expressão incompatível em comando de seleção", -1);
        }

        String novoRotulo = "L" + (contador_rotulos++);
        codigo.append("    brfalse ").append(novoRotulo).append("\n");
        pilha_rotulos.push(novoRotulo);
    }

    private void callAction126() {
        String rotulo = pilha_rotulos.pop();
        codigo.append(rotulo).append(":\n");
    }

    private void callAction127() {
        String novoRotulo2 = "L" + (contador_rotulos++);
        codigo.append("br ").append(novoRotulo2).append("\n");

        String rotuloIf = pilha_rotulos.pop();
        codigo.append(rotuloIf).append(":\n");

        pilha_rotulos.push(novoRotulo2);
    }

    private void callAction128(Token token) {
        String novoRotulo = "rotulo_" + (contador_rotulos++);
        codigo.append(novoRotulo).append(":\n");
        pilha_rotulos.push(novoRotulo);
    }

    private void callAction129(Token token) throws SemanticError {
        if (pilha_tipos.isEmpty()) {
            throw new SemanticError("expressão esperada em comando de repetição", -1);
        }

        String tipoExpressao = pilha_tipos.pop();

        if (!"bool".equals(tipoExpressao)) {
            throw new SemanticError("expressão incompatível em comando de repetição", -1);
        }

        String rotulo = pilha_rotulos.pop();
        codigo.append("brfalse ").append(rotulo).append("\n");
    }

    private void callAction130(Token token) throws SemanticError {
        String id = token.getLexeme();
        String tipoVar = tabela_simbolos.get(id);

        if (tipoVar == null) {
            throw new SemanticError("identificador não declarado: " + id, token.getPosition());
        }

        pilha_tipos.push(tipoVar);
        codigo.append("    ldloc ").append(id).append("\n");

        if ("int64".equals(tipoVar)) {
            codigo.append("    conv.r8\n");
        }
    }

    //método ação100() {
    //codigo.adiciona(".assembly extern mscorlib {} " +
    //".assembly programa{}" +
    //".module programa.exe" +
    //"" +
    //"class public _unica{" +
    //".method static public void _principal(){" +
    //".entrypoint");
    //}
    //método ação101() {
    //codigo.adiciona("ret" + 
    //"}" + 
    //"}");
    //}
    //método ação103(Token token) {
    //pilha_tipos.empilha("int64);
    //codigo.adiciona("ldc.i8" + token.getLexeme());
    //codigo.adiciona("conv.r8");
    //}
    //método ação104(Token token) {
    //pilha_tipos.empilha("float64);
    //codigo.adiciona("ldc.i8" + token.getLexeme());
    //}
    public void gerarArquivoIL() throws IOException {
        if (arquivoFonte == null) {
            throw new IOException("Arquivo não foi salvo. Salve o arquivo antes de compilar.");
        }
        String caminho = arquivoFonte.getParent();
        String nome1 = arquivoFonte.getName().replace(".txt", "");
        File destino = new File(caminho, nome1 + ".il");

        try (FileWriter fw = new FileWriter(destino)) {
            fw.write(codigo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String determinaTipoResultado(String tipo1, String tipo2, String operador) {
        if ("/".equals(operador)) {
            return "float64";
        }
        if ("float64".equals(tipo1) || "float64".equals(tipo2)) {
            return "float64";
        }

        return "int64";
    }

    private String converterTipoIL(String tipoFonte) {
        switch (tipoFonte) {
            case "bool":
                return "bool";
            case "int":
                return "int64";
            case "float":
                return "float64";
            case "string":
                return "string";
            default:
                return tipoFonte;
        }
    }
    
    private void verificarPilhaTipos(int elementos) {
        if (pilha_tipos.size() < elementos) {
            throw new AbstractMethodError();
        }
    }
}
