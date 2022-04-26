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
        byte[] bytes = Files.readAllBytes(path);



        Pattern openBracket = Pattern.compile("<(\\w+)+ ?([\\w ]+)?>");
        Pattern brackets = Pattern.compile("</?(.+?)>");
        
        
        
        List<Objeto> root = ConteudoDoObjeto(bytes, 0, bytes.length);
        
        System.out.println(root);
    }

    public static void MontaJSON(){
        StringBuilder json = new StringBuilder();
        
    }

    public static List<Objeto> ConteudoDoObjeto(byte[] bytes, int inicio, int fim){

        Pattern bothBrackets = Pattern.compile("<(.+?)>(.+)</?(.+?)>", Pattern.DOTALL);
        Pattern property = Pattern.compile("[^<>]*");

        List<Objeto> objetos = new ArrayList<>();

        StringBuilder xmlString = new StringBuilder();

        for(int i = inicio; i < fim; i++){
            xmlString.append((char) bytes[i]);
            Matcher m = bothBrackets.matcher(xmlString);
            if(m.find() && m.group(1).equals(m.group(3))){

                Objeto objeto = new Objeto();
                objeto.nome = m.group(1);

                Matcher n = property.matcher(m.group(2).toString());

                if(n.find()){
                    Propriedade prop = new Propriedade();
                    prop.chave = m.group(1);
                    prop.valor = m.group(2);
                    objeto.propriedades.add(prop);
                }
                else{
                    objeto.filho = ConteudoDoObjeto(bytes, xmlString.length(), i);
                }
                objetos.add(objeto);
                xmlString.setLength(0);
            }
        }
        
        return objetos;
    }
}
