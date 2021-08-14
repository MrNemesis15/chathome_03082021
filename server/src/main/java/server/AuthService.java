package server;

public interface AuthService {
    /**
     * Метод проверки наличия учетной записи
     *
     * @param login логин, не должен содержать пробелов.
     * @param password пароль, не должен содержать пробелов.
     * @return nickname если учтеная запись существует, null если нет
     */

    String getNicknameByLoginAndPassword(String login, String password);

    /**
     * Метод для попытки регистрации новой учетной записи
     *
     * @return true если регистрация прошла успешно.
     * false если логин и никнейм уже заняты.
     */
    boolean registration(String login,String password,String nickname);

}
