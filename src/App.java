import java.io.Console;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.GroupLayout.Group;

public class App {
    public static void main(String[] args) throws Exception {
        Path path = Paths.get("input.xml");
        //byte[] bytes = Files.readAllBytes(path);

        StringBuilder file = new StringBuilder();
        file.append(Files.readAllLines(path));
        
        List<Objeto> root = ConteudoDoObjeto(file);
        
        Path saida = Paths.get("output.json");
        StringBuilder build = new StringBuilder();

        build = MontaObjeto(root);
        build.insert(0, "{\n");
        build.append("\n}");

        byte[] bytes = build.toString().getBytes();
        Files.write(saida, bytes);
    }

    public static StringBuilder MontaObjeto(List<Objeto> objetos){
        StringBuilder json = new StringBuilder();
        
        for (Objeto objeto : objetos) {
            json.append("\"" + objeto.nome + "\":");
            if(objeto.valor != null){
                json.append(" \"" + objeto.valor + "\"");
            }
            else{
                json.append("{\n");
                json.append(MontaObjeto(objeto.filho));
                json.append("\n}");
            }
            if(objetos.size() > 1 && objetos.get(objetos.size() - 1) != objeto)
                    json.append(",\n");
        }
        return json;
    }

    public static List<Objeto> ConteudoDoObjeto(StringBuilder text){

        Pattern bothBrackets = Pattern.compile("<(.+?)>(.+)</?(.+?)>", Pattern.DOTALL);
        Pattern property = Pattern.compile("[^<>]*");

        List<Objeto> objetos = new ArrayList<>();

        StringBuilder xmlString = new StringBuilder();

        for(int i = 0; i < text.length(); i++){
            xmlString.append(text.toString().charAt(i));
            Matcher m = bothBrackets.matcher(xmlString);
            if(m.find() && m.group(1).equals(m.group(3))){

                Objeto objeto = new Objeto();
                objeto.nome = m.group(1);

                Matcher n = property.matcher(m.group(2).toString());

                if(n.matches()){
                    objeto.valor = m.group(2);
                }
                else{
                    StringBuilder novoTexto = new StringBuilder(m.group(2));
                    objeto.filho = ConteudoDoObjeto(novoTexto);
                }
                objetos.add(objeto);
                xmlString.setLength(0);
            }
        }
        
        return objetos;
    }
}
