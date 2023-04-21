package antoniJanson.client;

import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.Optional;

public class ClientService {
    @Getter
    private Client currentClient;

    private final ClientRepository clientRepository = new ClientRepository();

    public boolean checkIfUsernameExists(String username){
        return clientRepository.findByUsername(username).isPresent();
    }

    public boolean loginAuthentication(String username, String password){
        Optional<Client> client = clientRepository.findByUsername(username);
        if(client.isPresent()){
            currentClient = client.get();
            return Objects.equals(currentClient.getPassword(), encryptPass(password));
        }
        return false;
    }

    public Client createClient(String phone, String name, String surname, String email, String username, String password){
        return new Client(clientRepository.createClient(phone, name, surname, email, username, password),
                phone, name, surname, email, username, password);
    }

    private String encryptPass(String password) {
        try {
            //retrieve instance of the encryptor of SHA-256
            MessageDigest digestor = MessageDigest.getInstance("SHA-256");
            //retrieve bytes to encrypt
            byte[] encodedhash = digestor.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder encryptionValue = new StringBuilder(2 * encodedhash.length);
            //perform encryption
            for (int i = 0; i < encodedhash.length; i++) {
                String hexVal = Integer.toHexString(0xff & encodedhash[i]);
                if (hexVal.length() == 1) {
                    encryptionValue.append('0');
                }
                encryptionValue.append(hexVal);
            }
            //return encrypted value
            return encryptionValue.toString();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
}
