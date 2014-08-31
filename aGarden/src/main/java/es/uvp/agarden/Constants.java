package es.uvp.agarden;

/**
 * Created by Alberto on 06/08/14.
 */
public class Constants {

    //Estados de la petición del servicio
    public static final Integer FALSE           = 0;
    public static final Integer TRUE            = 1;
    public static final Integer ERROR_SERVICE   = -1;
    public static final Integer ERROR_APP       = -2;

    //Estados de la session
    public static final String CONECTADO       = "conectado";
    public static final String DESCONECTADO    = "desconectado";


    //urls
    public static final String  URL_LOGIN = ":8080/RESTfulJardin/services/session/authentication";
    public static final String  URL_AREAS = ":8080/RESTfulJardin/services/resources/getAreas";



}
