import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class App {
    public static void main(String[] args) throws Exception {
        Path path = Paths.get("input4.xml");
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
        
        for(int i = 0, size = objetos.size(); i < size; i++){
            if((i + 1 < size && objetos.get(i).nome.equals(objetos.get(i + 1).nome)) || (i != 0 && i + 1 == size && objetos.get(i).nome.equals(objetos.get(i - 1).nome))){
                if(i == 0 || (i - 1 >= 0 && !objetos.get(i - 1).nome.equals(objetos.get(i).nome))){
                    json.append("\"" + objetos.get(i).nome + "\": ["); //se o próximo objeto do primeiro item da lista tiver o mesmo nome que esse e o objeto anterior for diferente é criado um array
                }
                //caso o item não seja o primeiro do array o objeto não tem o cabeçalho escrito
            }
            else{
                json.append("\"" + objetos.get(i).nome + "\":"); //caso o objeto não se repita não é criado o array
            }
            
            if(objetos.get(i).valor != null){
                json.append(" \"" + objetos.get(i).valor + "\"");
            }
            else{
                json.append("{\n");
                json.append(MontaObjeto(objetos.get(i).filho));
                json.append("\n}");
                //verifica se o nome do objeto anterior é igual a ele e se ou o posterior é diferente ou ele é o último objeto
                if((i - 1 >= 0 && objetos.get(i - 1).nome.equals(objetos.get(i).nome)) && (i + 1 == size || (i + 1 < size && !objetos.get(i).nome.equals(objetos.get(i + 1).nome)))){
                    json.append("]");
                }
            }
            if(objetos.size() > 1 && objetos.get(objetos.size() - 1) != objetos.get(i))
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
