package Interface;

public class Token {

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
;
}
