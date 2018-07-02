package is2;

import is2.Usuario;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-02T07:59:40")
@StaticMetamodel(Proyecto.class)
public class Proyecto_ { 

    public static volatile SingularAttribute<Proyecto, Integer> idProyecto;
    public static volatile SingularAttribute<Proyecto, Date> fechaInicio;
    public static volatile SingularAttribute<Proyecto, String> alias;
    public static volatile CollectionAttribute<Proyecto, Usuario> usuarioCollection;
    public static volatile SingularAttribute<Proyecto, String> nombre;
    public static volatile SingularAttribute<Proyecto, Date> fechaFin;

}