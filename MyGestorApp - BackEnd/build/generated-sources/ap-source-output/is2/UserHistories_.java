package is2;

import is2.Usuario;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-02T07:59:40")
@StaticMetamodel(UserHistories.class)
public class UserHistories_ { 

    public static volatile SingularAttribute<UserHistories, String> estado;
    public static volatile SingularAttribute<UserHistories, Date> fechaInicio;
    public static volatile SingularAttribute<UserHistories, Usuario> idUsuario;
    public static volatile SingularAttribute<UserHistories, Integer> idUs;
    public static volatile SingularAttribute<UserHistories, String> descripcionActividad;
    public static volatile SingularAttribute<UserHistories, String> tituloUs;
    public static volatile SingularAttribute<UserHistories, Date> fechaFin;

}