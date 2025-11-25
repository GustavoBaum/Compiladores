package Interface;

public class Token {

    //public static final int TK_IDENTIFICADOR = 2;
    //public static final int TK_SIMBOLO_ESPECIAL = 3;
    //public static final int TK_PALAVRA_RESERVADA = 4;
    //public static final int TK_CONSTANT_int = 5;
    //public static final int TK_CONSTANT_float = 6;
    //public static final int TK_CONSTANT_string = 7;
    private int id;
    private String lexeme;
    private int position;

    public Token(int id, String lexeme, int position) {
        this.id = id;
        this.lexeme = lexeme;
        this.position = position;
    }
    
    public final int getId()
    {
        return id;
    }

    public final String getIdporExtenso() {
        if (id == 2) {
            return "identificador";
        } else if (id == 3) {
            return "constante_int";
        } else if (id == 4) {
            return "constante_float";
        } else if (id == 5) {
            return "constante_string";
        } else if (id >= 6 && id <= 28) {
            return "palavra reservada";
        } else if (id >= 29 && id <= 42) {
            return "simbolo especial";
        } else {
            return "desconhecido (" + id + ")";
        }
        /*switch (id) {
            case TK_IDENTIFICADOR:
                return "identificador";
            case TK_PALAVRA_RESERVADA:
                return "palavra reservada";
            case TK_SIMBOLO_ESPECIAL:
                return "simbolo especial";
            case TK_CONSTANT_int:
                return "constante_int";
            case TK_CONSTANT_float:
                return "constante_float";
            case TK_CONSTANT_string:
                return "constante_string";
            default:
                return "desconhecido (" + id + ")";
        }*/
    }

    public final String getLexeme() {
        return lexeme;
    }

    public final int getPosition() {
        return position;
    }

    public String toString() {
        return id + " ( " + lexeme + " ) @ " + position;
    }
}
