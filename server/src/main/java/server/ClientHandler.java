package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    Socket socket;
    Server server;
    DataInputStream in;
    DataOutputStream out;

    private boolean authenticated;
    private String nickname;

    public ClientHandler(Socket socket, Server server) {
        try {
            this.socket = socket;
            this.server = server;

            in = new DataInputStream (socket.getInputStream ());
            out = new DataOutputStream (socket.getOutputStream ());

            new Thread (() -> {
                try {
                    //auth cycle
                    while (true) {
                        String str = in.readUTF ();

                        if (str.equals ("/end")) {
                            sendMsg ("/end");
                            System.out.println ("Client disconnected");
                            break;
                        }
                        if (str.startsWith ("/auth ")) {
                            String[] token = str.split ("\\s+");
                            nickname = server.getAuthService ()
                                    .getNicknameByLoginAndPassword (token[1], token[2]);
                            if (nickname != null) {
                                server.subscribe (this);
                                authenticated = true;
                                sendMsg ("/authok " + nickname);
                                break;
                            } else {
                                sendMsg ("Password or login not correct");
                            }
                        }
                    }

                    //work cycle
                    while (authenticated) {
                        String str = in.readUTF ();
                        if (str.startsWith ("/")) {
                            if (str.equals ("/end")) {
                                sendMsg ("/end");
                                System.out.println ("Client disconnected");
                                break;
                            }
                            if (str.startsWith ("/w")) {
                                String[] token = str.split ("\\s+", 3);
                                if (token.length < 3){
                                    continue;
                                }
                                server.privateMsg (this,token[1],token[2]);
                            }
                        } else {
                            server.broadcastMsg (this, str);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace ();
                } finally {
                    server.subscribe (this);
                    try {
                        socket.close ();
                    } catch (IOException e) {
                        e.printStackTrace ();
                    }
                }
            }).start ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF (msg);
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    public String getNickname() {
        return nickname;
    }
}
