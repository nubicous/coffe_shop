package antoniJanson.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Client {
    private int client_id;
    private String phone_number;
    private String name;
    private String surname;
    private String email;
    private String username;
    private String password;
}
