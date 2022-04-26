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
        Path path = Paths.get("input3.xml");
        //byte[] bytes = Files.readAllBytes(path);

        StringBuilder file = new StringBuilder();
        file.append(Files.readAllLines(path));
        String s2 = file.toString();

        Pattern openBracket = Pattern.compile("<(\\w+)+ ?([\\w ]+)?>");
        Pattern brackets = Pattern.compile("</?(.+?)>");
        
        
        
        List<Objeto> root = ConteudoDoObjeto(file);
        //PrintaObjetos(root);
        
        Path saida = Paths.get("output.json");
        StringBuilder build = new StringBuilder();

        build = MontaObjeto(root);

        byte[] bytes = build.toString().getBytes();
        Files.write(saida, bytes);
    }

    public static void PrintaObjetos(List<Objeto> objetos){
        System.out.println();
        for (Objeto objeto : objetos) {
            System.out.println(objeto.nome);
            System.out.println("Propriedades:");
            for (Propriedade p : objeto.propriedades) {
                System.out.println("chave: " + p.valor + " valor: " + p.valor);
            }
            System.out.println("Objetos:");
            PrintaObjetos(objeto.filho);
            System.out.println();
        }
    }

    public static StringBuilder MontaObjeto(List<Objeto> objetos){
        StringBuilder json = new StringBuilder();
        json.append("{ \n\t");
        for (Objeto objeto : objetos) {
            json.append("\"" + objeto.nome + "\"");
            if(objeto.propriedades.size() != 0){
                json.append(" : ");
                json.append(objeto.propriedades.get(0).valor);
            }
            else{
                json.append(MontaObjeto(objeto.filho));
            }
        }
        json.append("\n\t}");
        return json;
    }

    public static List<Objeto> ConteudoDoObjeto(StringBuilder text){

        Pattern bothBrackets = Pattern.compile("<(.+?)>(.+)</?(.+?)>", Pattern.DOTALL);
        Pattern property = Pattern.compile("[^<>]*");

        List<Objeto> objetos = new ArrayList<>();

        StringBuilder xmlString = new StringBuilder();

        for(int i = 0; i < text.length(); i++){
            String s1 = text.toString();
            xmlString.append(text.toString().charAt(i));
            String s = xmlString.toString();
            Matcher m = bothBrackets.matcher(xmlString);
            if(m.find() && m.group(1).equals(m.group(3))){

                Objeto objeto = new Objeto();
                objeto.nome = m.group(1);

                Matcher n = property.matcher(m.group(2).toString());

                if(n.matches()){
                    Propriedade prop = new Propriedade();
                    prop.chave = m.group(1);
                    prop.valor = m.group(2);
                    objeto.propriedades.add(prop);
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
