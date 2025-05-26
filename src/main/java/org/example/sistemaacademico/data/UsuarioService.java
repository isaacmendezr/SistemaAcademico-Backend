package org.example.sistemaacademico.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.database.Servicio;
import org.example.sistemaacademico.logic.Curso;
import org.example.sistemaacademico.logic.Usuario;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UsuarioService {
    private static final String insertarUsuario = "{call insertarUsuario (?,?,?)}";
    private static final String modificarUsuario = "{call modificarUsuario (?,?,?)}";
    private static final String eliminarUsuario = "{call eliminarUsuario(?)}";
    private static final String listarUsuarios = "{?=call listarUsuarios()}";
    private static final String buscarUsuarioPorCedula = "{?=call buscarUsuarioPorCedula(?)}";
    private static final String loginUsuario = "{call loginUsuario(?, ?, ?)}";
    private Servicio servicio;

    public UsuarioService() throws ClassNotFoundException, SQLException {
        this.servicio = org.example.sistemaacademico.database.Servicio.getInstancia();
    }
    public void insertarUsuario(Usuario usuario) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var5) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException var6) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(insertarUsuario);
            pstmt.setString(1, usuario.getCedula());
            pstmt.setString(2, usuario.getClave());
            pstmt.setString(3, usuario.getTipo());
            boolean var4 = pstmt.execute();
        } catch (SQLException var7) {
            var7.printStackTrace();
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var4) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }
    }
    public void modificarUsuario(Usuario usuario) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var5) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException var6) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(modificarUsuario);
            pstmt.setLong(1, usuario.getIdUsuario());
            pstmt.setString(2, usuario.getCedula());
            pstmt.setString(3, usuario.getTipo());
            boolean var4 = pstmt.execute();
        } catch (SQLException var7) {
            var7.printStackTrace();
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var4) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }
    }
    public void eliminarUsuario(Long usuario) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var5) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException var6) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(eliminarUsuario);
            pstmt.setLong(1, usuario);
            boolean var4 = pstmt.execute();
        } catch (SQLException var7) {
            var7.printStackTrace();
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var4) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }
    }
    public List<Usuario> listarUsuarios() throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var14) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException var15) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        ArrayList coleccion = new ArrayList();
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(listarUsuarios);
            pstmt.registerOutParameter(1, -10);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);

            while(rs.next()) {
                coleccion.add(new Usuario(
                        rs.getLong("id_usuario"),
                        rs.getString("cedula"),
                        rs.getString("tipo")));
            }
        } catch (SQLException var16) {
            var16.printStackTrace();
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var13) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }

        if (coleccion != null && coleccion.size() != 0) {
            return coleccion;
        } else {
            throw new NoDataException("No hay datos");
        }
    }
    public Usuario buscarUsuarioPorCedula(String cedula) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var5) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException var6) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        Usuario usuario = null;
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(buscarUsuarioPorCedula);
            pstmt.registerOutParameter(1, -10);
            pstmt.setString(2, cedula);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);
            if (rs.next()) {
                usuario = new Usuario(
                        rs.getLong("id_usuario"),
                        rs.getString("cedula"),
                        rs.getString("tipo"));
            }
        } catch (SQLException var7) {
            var7.printStackTrace();
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var4) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }

        if (usuario != null) {
            return usuario;
        } else {
            throw new NoDataException("No hay datos");
        }
    }
    public Usuario loginUsuario(String cedula, String clave) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        CallableStatement pstmt = null;
        Usuario usuario = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(loginUsuario);
            pstmt.setString(1, cedula);
            pstmt.setString(2, clave);
            pstmt.registerOutParameter(3,-10);

            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(3);
            if (rs.next()) {
                usuario = new Usuario(
                        rs.getLong("id_usuario"),
                        rs.getString("cedula"),
                        rs.getString("tipo")
                );
            }
            System.out.println("Usuario: " + usuario);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Sentencia no válida");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                this.servicio.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Estatutos inválidos o nulos");
            }
        }

        if (usuario == null) {
            throw new NoDataException("Credenciales inválidas o usuario no encontrado");
        }

        return usuario;
    }
}
