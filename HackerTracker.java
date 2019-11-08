import java.io.*;
import java.util.*;

public class HackerTracker {

    Map<String, List<String>> ipsByUser = new HashMap<>();
    BufferedReader reader; 
    BufferedWriter writer;

    /**
     * Extraire les noms des utilisateurs et les définir
     * comme clef de la hashmap ipsByUser.
     * @throws IOException
     */
    public void getUsersFromFile() throws IOException {
        reader = new BufferedReader(new FileReader("connexion.log"));
        String currentLine = "";
        while((currentLine = reader.readLine()) != null) {
            this.ipsByUser.put(currentLine.split(";")[1], new ArrayList<String>());
        }
        
        reader.close();
    }
    
    /**
     * Ecrire le nom de chaque utilisateur dans le fichier utilisateurs.txt
     * en bouclant sur les clefs de la hashmap ipsByUser.
     * @throws IOException
     */
    public void writeUsersToFile() throws IOException {
        writer = new BufferedWriter(new FileWriter("utilisateurs.txt"));
        
        Set<String> usersSet = ipsByUser.keySet();
        
        for(String username: usersSet) {
        	writer.write(username + "\n");
        }
        
        writer.close();
    }
    
    /**
     * Extraire les adresses ips du fichier connexion.log 
     * puis les insérer dans la liste d'ip associé à chaque utilisateurs 
     * dans la hashmap ipsByUser.
     * @throws IOException
     */
    public void getIpsFromFile() throws IOException {
    	reader = new BufferedReader(new FileReader("connexion.log"));
        String currentLine = "";
        
        while((currentLine = reader.readLine()) != null){
            String name = currentLine.split(";")[1];
            String ip = currentLine.split(";")[0];
            
            List<String> ipList = this.ipsByUser.get(name);
            ipList.add(ip);
            this.ipsByUser.put(name, ipList);
        }
        
        reader.close();
    }
    
    /**
     * Ecrire les connexions dangereuses en comparant les ips de warning.txt
     * et les ips visitées par chaque utilisateur dans la hashmap ipsByUser;
     * @throws IOException
     */
    public void writeDangerousUsersConnectionsToFile() throws IOException {
    	reader = new BufferedReader(new FileReader("warning.txt"));
        String currentLine = "";
        List<String> dangerousIPs = new ArrayList<>();
        
        while((currentLine = reader.readLine()) != null){
            dangerousIPs.add(currentLine);
        }
        
        reader.close();

        writer = new BufferedWriter(new FileWriter("suspect.txt"));
        Set<String> keys = ipsByUser.keySet();
        
        for(String username: keys){
            int count = 0;
            List<String> visitedIPs = ipsByUser.get(username);
            
            for(String ip: visitedIPs){
                if(dangerousIPs.contains(ip))
                    count++;
            }
            if(count > 0)
                writer.write(username + ";" + count + "\n");
        }
        
        writer.close();

    }
    
    /** Trouver le suspect en vérifiant les heures de connexion 
     *  dans le fichier connexion.log
     * 
     * @throws IOException
     */
    public void findSuspect() throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader("connexion.log"));
        String currentLine = "";
        String suspectName = "";
        String suspectIp = "";
        
        while((currentLine = reader.readLine()) != null){
            String hour = currentLine.split(";")[2].split(" ")[1].split(":")[0];
            
            if(Integer.parseInt(hour) < 8 || Integer.parseInt(hour) > 19){
                suspectName = currentLine.split(";")[1];
                suspectIp = currentLine.split(";")[0];
            }
        }
        
        reader.close();
        System.out.println( "Le suspect se nomme " + suspectName + "\nL'ip qu'il a visité est " + suspectIp );
    }
    

    
    public static void main(String[] args) throws IOException {
        HackerTracker test = new HackerTracker();
        test.getUsersFromFile();
        test.writeUsersToFile();
        test.getIpsFromFile();
        test.writeDangerousUsersConnectionsToFile();
        test.findSuspect();

    }

}
