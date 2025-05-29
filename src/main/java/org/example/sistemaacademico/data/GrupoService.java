package org.example.sistemaacademico.data;

import org.example.sistemaacademico.database.GlobalException;
import org.example.sistemaacademico.database.NoDataException;
import org.example.sistemaacademico.database.Servicio;
import org.example.sistemaacademico.logic.Grupo;
import org.example.sistemaacademico.logic.dto.GrupoDto;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GrupoService {
    private static final String insertarGrupo = "{call insertarGrupo (?,?,?,?)}";
    private static final String modificarGrupo= "{call modificarGrupo(?,?,?,?,?)}";
    private static final String eliminarGrupo = "{call eliminarGrupo(?)}";
    private static final String listarGrupos = "{?=call listarGrupos()}";
    private static final String buscarGruposPorCarreraCurso = "{?= call buscarGruposPorCarreraCurso(?,?)}";
    private static final String buscarGruposPorCursoCicloCarrera = "{?= call buscarGruposPorCursoCicloCarrera(?,?,?)}";
    private Servicio servicio;

    public GrupoService() throws ClassNotFoundException, SQLException {
        this.servicio = Servicio.getInstancia();
    }
    public void insertarGrupo(Grupo grupo) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(insertarGrupo);
            pstmt.setLong(1, grupo.getIdCarreraCurso());
            pstmt.setLong(2, grupo.getNumeroGrupo());
            pstmt.setString(3, grupo.getHorario());
            pstmt.setLong(4,grupo.getIdProfesor());

            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizo la inserci�n");
            }
        } catch (SQLException var15) {
            var15.printStackTrace();
            throw new GlobalException("Llave duplicada");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var12) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }

    }
    public void modificarGrupo(Grupo grupo) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(modificarGrupo);
            pstmt.setLong(1, grupo.getIdGrupo());
            pstmt.setLong(2, grupo.getIdCarreraCurso());
            pstmt.setLong(3, grupo.getNumeroGrupo());
            pstmt.setString(4, grupo.getHorario());
            pstmt.setLong(5, grupo.getIdProfesor());

            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizo la actualizaci�n");
            }

            //System.out.println("\nModificaci�n Satisfactoria!");
        } catch (SQLException var15) {
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var12) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }
    }
    public void eliminarGrupo(Long grupo) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var13) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException var14) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        PreparedStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareStatement(eliminarGrupo);
            pstmt.setLong(1, grupo);
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new NoDataException("No se realizo el borrado");
            }

            //System.out.println("\nEliminaci�n Satisfactoria!");
        } catch (SQLException var15) {
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                this.servicio.desconectar();
            } catch (SQLException var12) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }

    }
    public List<Grupo> listarGrupos() throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException var14) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException var15) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        ArrayList<Grupo> coleccion = new ArrayList<>();
        CallableStatement pstmt = null;

        try {
            pstmt = this.servicio.conexion.prepareCall(listarGrupos);
            pstmt.registerOutParameter(1, -10);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);

            while(rs.next()) {
                coleccion.add(new Grupo(
                        rs.getLong("id_grupo"),
                        rs.getLong("pk_carrera_curso"),
                        rs.getLong("numero_grupo"),
                        rs.getString("horario"),
                        rs.getLong("pk_profesor")));
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

    public List<GrupoDto> buscarGruposPorCarreraCurso(Long idCarrera, Long idCurso) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException | SQLException e) {
            throw new GlobalException("No se ha localizado el driver o base de datos no disponible");
        }

        CallableStatement cs = null;
        ResultSet rs = null;
        List<GrupoDto> listaGrupos = new ArrayList<>();

        try {
            cs = this.servicio.conexion.prepareCall(buscarGruposPorCarreraCurso);
            cs.registerOutParameter(1, -10);
            cs.setLong(2, idCarrera);
            cs.setLong(3, idCurso);
            cs.execute();

            rs = (ResultSet) cs.getObject(1);

            while (rs.next()) {
                GrupoDto grupo = new GrupoDto(
                        rs.getLong("id_grupo"),
                        rs.getLong("pk_carrera_curso"),
                        rs.getLong("numero_grupo"),
                        rs.getString("horario"),
                        rs.getLong("pk_profesor"),
                        rs.getString("nombre_profesor")
                );
                listaGrupos.add(grupo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en sentencia SQL");
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                this.servicio.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }

        if (listaGrupos.isEmpty()) {
            throw new NoDataException("No hay datos");
        }

        return listaGrupos;
    }
    public List<GrupoDto> buscarGruposPorCursoCicloCarrera(Long curso, Long ciclo, Long carrera) throws GlobalException, NoDataException {
        try {
            this.servicio.conectar();
        } catch (ClassNotFoundException | SQLException e) {
            throw new GlobalException("No se ha localizado el driver o base de datos no disponible");
        }

        CallableStatement cs = null;
        ResultSet rs = null;
        List<GrupoDto> listaGrupos = new ArrayList<>();

        try {
            cs = this.servicio.conexion.prepareCall(buscarGruposPorCursoCicloCarrera);
            cs.registerOutParameter(1, -10);
            cs.setLong(2, curso);
            cs.setLong(3, ciclo);
            cs.setLong(4, carrera);
            cs.execute();

            rs = (ResultSet) cs.getObject(1);

            while (rs.next()) {
                GrupoDto grupo = new GrupoDto(
                        rs.getLong("id_grupo"),
                        rs.getLong("pk_carrera_curso"),
                        rs.getLong("numero_grupo"),
                        rs.getString("horario"),
                        rs.getLong("id_profesor"),
                        rs.getString("nombre_profesor")
                );
                listaGrupos.add(grupo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en sentencia SQL");
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                this.servicio.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos");
            }
        }

        if (listaGrupos.isEmpty()) {
            throw new NoDataException("No hay datos");
        }

        return listaGrupos;
    }
}
