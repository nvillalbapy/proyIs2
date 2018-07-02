package helper;

public class URLs {

    private static final String  ROOT_URL = "http://192.168.0.17:8080/MyGestorApp/webresources/";

    public static final String URL_USER = ROOT_URL + "usuario/";
    public static final String URL_LOGIN = ROOT_URL + "usuario/login";
    public static final String URL_REGISTRATION = ROOT_URL + "usuario/registration";
    public static final String URL_EDIT = ROOT_URL + "usuario/edit/";
    public static final String URL_FIND = ROOT_URL + "usuario/findByUsuario/";

    public static final String URL_UH = ROOT_URL + "userhistories/";
    public static final String URL_CREAR_UH = ROOT_URL + "userhistories/crearUh";
    public static final String URL_LISTA_UH = ROOT_URL + "userhistories/findByUsuario";

    public static final String URL_ROL = ROOT_URL + "rol/";
}