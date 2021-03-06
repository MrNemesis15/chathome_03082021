package server;

import java.util.ArrayList;
import java.util.List;

public class SimpleAuthService implements AuthService {

    private class UserData {
        String login;
        String password;
        String nickname;

        public UserData(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }

    private List<UserData> users;

    public SimpleAuthService() {
        this.users = new ArrayList<> ();
        users.add (new UserData ("Sagi", "qwerty", "Sagi"));
        users.add (new UserData ("Kruzhilov", "qwerty", "Kruzhilov"));
        users.add (new UserData ("Kira", "qwerty", "Kira9ly"));
        for (int i = 0; i < 10; i++) {
            users.add (new UserData ("login" + i, "password" + i, "nickname" + i));
        }
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        for (UserData u: users){
            if (u.login.equals (login) && u.password.equals (password)){
                return u.nickname;
            }
        }
        return null;
    }
}
