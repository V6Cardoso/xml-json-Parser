import java.util.List;
import java.util.ArrayList;

public class Objeto {
    public String nome;
    public List<Objeto> filho = new ArrayList<>();
    public List<Propriedade> propriedades = new ArrayList<>();

    public Objeto(){
    }
}
