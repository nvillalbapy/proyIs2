/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is2.service;

import is2.UserHistories;
import is2.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Micaela
 */
@Stateless
@Path("/usuario")
public class UsuarioFacadeREST extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "MyGestorAppPU")
    private EntityManager em;

    public UsuarioFacadeREST() {
        super(Usuario.class);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void create(Usuario entity) {
        super.create(entity);
    }
    
    @POST
    @Path("/registration")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Usuario registration(Usuario user) {
        TypedQuery<Usuario> query =getEntityManager().createNamedQuery("Usuario.findByUsuario", Usuario.class);
        query.setParameter("usuario", user.getUsuario());
        try {
            // Valida si ya existe ese usuario, retorna null
            Usuario usuario = query.getSingleResult();
            return null;
        } catch (NoResultException e) {
            // Si aun no existe el usuario, carga en la base de datos y retorna el nuevo usuario
            super.create(user);
            return user;
        }
    }
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
     public Usuario login(Usuario user) {
        TypedQuery<Usuario> query =getEntityManager().createNamedQuery("Usuario.findByLogin", Usuario.class);
        query.setParameter("usuario", user.getUsuario());
        query.setParameter("password", user.getPassword());
     
        try {
            Usuario usuario = query.getSingleResult();
            return usuario;
        } catch (NoResultException e) {
            return null;
        }
        
    }

    @PUT
    @Path("/edit/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Usuario editar(@PathParam("id") Integer id, Usuario user) {
        Boolean disponible = usuarioDisponible(user);
        if (disponible) {
            super.edit(user);
            return user;
        }
        else {
            return null;
        }
    }
    
    @POST
    @Path("/findByUsuario")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Usuario findUser(Usuario user) {
        TypedQuery<Usuario> query =getEntityManager().createNamedQuery("Usuario.findByUsuario", Usuario.class);
        query.setParameter("usuario", user.getUsuario());
        try {
            Usuario usuario = query.getSingleResult();
            return usuario;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void edit(@PathParam("id") Integer id, Usuario entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Usuario find(@PathParam("id") Integer id) {
        return super.find(id);
    }
    
    @GET
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public List<Usuario> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    protected Boolean usuarioDisponible(Usuario user) {
        TypedQuery<Usuario> query =getEntityManager().createNamedQuery("Usuario.findByUsuario", Usuario.class);
        query.setParameter("usuario", user.getUsuario());
        try {
            // Valida si ya existe ese usuario, retorna false
            Usuario usuario = query.getSingleResult();
            return false;
        } catch (NoResultException e) {
            // Si aun no existe el usuario, retorna true
            return true;
        }
       
    }
            
}
